/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SshAdminServlet.java,v $
 * $Revision: 1.2 $
 * $Author: edaugherty $
 * $Date: 2003/11/23 00:18:10 $
 ******************************************************************************
 * Copyright (c) 2003, Eric Daugherty (http://www.ericdaugherty.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Eric Daugherty nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 * *****************************************************************************
 * For current versions and more information, please visit:
 * http://www.ericdaugherty.com/dev/sshwebproxy
 *
 * or contact the author at:
 * web@ericdaugherty.com
 *****************************************************************************/

package ren.doob.sshwebproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Enumeration;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * Handles all the actions that are not
 * releated to actual SSH communciation.
 *
 * @author Eric Daugherty
 */
public class SshAdminServlet extends HttpServlet implements SshConstants {

    //***************************************************************
    // Variables
    //***************************************************************

    /** Reference to the file that contains the user login information */
    private File propertiesFile;

    /** The last time the propertiesFile was changed */
    private long propertiesFileLastModified;

    /** The loaded user login information */
    private Properties properties;

    /** Logger */
    private static final Log log = LogFactory.getLog( SshAdminServlet.class );

    //***************************************************************
    // HTTPServlet Methods
    //***************************************************************

    /**
     * Called when the application is deployed.  Loads the properties file
     *
     * @param servletConfig
     * @throws ServletException
     */
    public void init( ServletConfig servletConfig ) throws ServletException
    {
        super.init( servletConfig );

        // Load the properties.
        initializeProperties();

        // Start the watchdog thread.
        new ConfigurationFileWatcher().start();

        log.info( "SSHWebProxy Initialized using properties file: " + propertiesFile.getAbsolutePath() );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.warn( "doGet called, but is not implemented." );
        response.sendRedirect( PAGE_HOME );
    }

    /**
     * Handles requests from the SHH client JSP page.  All requests from
     * that page should be via POST.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter( PARAMETER_ACTION );

        // Verify we received an action to perform.
        if( action == null || action.trim().length() == 0 )
        {
            log.warn( "POST Request received without an action parameter." );
            response.sendRedirect( PAGE_HOME );
        }

        action = action.trim();
        if( ACTION_LOGIN.equals( action ) )
        {
            login( request, response );
        }
        else
        {
            log.warn( "POST Request received with an invalid action parameter: " + action );
            response.sendRedirect( PAGE_HOME );
        }
    }

    //***************************************************************
    // Private Action Handlers
    //***************************************************************

    private void login( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Login request received." );

        SshSession sshSession = new SshSession( request );

        String username = request.getParameter( PARAMETER_USERNAME );
        String password = request.getParameter( PARAMETER_PASSWORD );
        String redirectPage = PAGE_LOGIN;

        if( username == null || username.length() == 0 )
        {
            sshSession.setErrorMessage( "Please specify a vaild username." );
        }
        else if( password == null || password.length() == 0 )
        {
            sshSession.setErrorMessage( "Please specify a valid password." );
        }
        else
        {
            username = username.trim();
            password = password.trim();

            String correctPassword = properties.getProperty( username );
            if( correctPassword == null )
            {
                sshSession.setErrorMessage( "Unknown User." );
            }
            else
            {
                String encryptedPassword = encryptPassword( password );
                if( correctPassword.equals( encryptedPassword ) )
                {
                    sshSession.setUser( username );
                    redirectPage = PAGE_HOME;
                    if( log.isInfoEnabled() ) log.info( "User: " + username + " logged in successfully." );
                }
                else
                {
                    sshSession.setErrorMessage( "Incorrect Password." );
                }
            }
        }

        response.sendRedirect( redirectPage );
    }

    //***************************************************************
    // Private Util Methods
    //***************************************************************

    /**
     * Loads the properties file.  If the user specified the system
     * parameter sshwebproxy.properties, the file will be loaded from there.
     * Otherwise the file will be loaded from the default location.  If it
     * does not exist in the default location, a default file will be copied
     * to the default location.
     */
    private void initializeProperties() throws ServletException
    {
        properties = new Properties();

        String fileName = System.getProperty( PROPERTIES_FILENAME );
        if( fileName != null && !fileName.equals( "" ) )
        {
            // The user specified file path for the properties file.  Load it from there.
            propertiesFile = new File( fileName );
            if( propertiesFile.exists() && propertiesFile.isFile() )
            {
                // Load the properties file from the specified location.
                try
                {
                    loadProperties();
                }
                catch( IOException ioException )
                {
                    // If we got here, the file exists and is a file, but there
                    // was some error loading it.  Not much we can do, so just
                    // error out.  This should never happen.
                    throw new ServletException( "Unable to load the properties file from the specified location: " + propertiesFile.getAbsolutePath() + " due to an IOException: " + ioException );
                }
            }
            else
            {
                throw new ServletException( "The specified properties file: " + propertiesFile.getAbsolutePath() + " does not exist." );
            }
        }
        else
        {
            // If the sshwebproxy.properties location was not specified as a
            // system property, attempt to load it from the default
            // location.

            log.info( "The system property \"" + PROPERTIES_FILENAME + "\" was not specified.  Using the default location." );

            propertiesFile = new File( PROPERTIES_FILENAME );
            if( propertiesFile.exists() && propertiesFile.isFile() )
            {
                // Load the properties file from the default location.
                try
                {
                    loadProperties();
                }
                catch( IOException ioException )
                {
                    // If we got here, the file exists and is a file, but there
                    // was some error loading it.  Not much we can do, so just
                    // error out.  This should never happen.
                    throw new ServletException( "Unable to load the properties file from the default location: " + propertiesFile.getAbsolutePath() + " due to an IOException: " + ioException );
                }
            }
            else
            {
                // The default properties file does not exist, so attempt
                // to copy the properties file from the war to the default
                // file location.

                InputStream propertiesStream = getServletContext().getResourceAsStream("/WEB-INF/" + PROPERTIES_FILENAME );
                if( propertiesStream != null )
                {
                    try
                    {
                        // Load from the WAR
                        properties.load( propertiesStream );
                        // Save to the default location.
                        saveProperties();
                        // Load from the new location.
                        loadProperties();
                    }
                    catch( IOException ioException )
                    {
                        throw new ServletException( "Error copying the properties file from the WAR to the default location: " + propertiesFile.getAbsolutePath() + " due to an IOException: " + ioException );
                    }
                }
                else
                {
                    throw new ServletException( "Unable to load " + PROPERTIES_FILENAME + " from WAR.  SSHWebProxy will not function correctly!" );
                }
            }
        }
    }

    /**
     * Loads the properties from the propertiesFile
     * location.
     */
    private void loadProperties() throws IOException
    {
        InputStream propertiesStream = null;
        try
        {
            propertiesStream = new FileInputStream( propertiesFile );
            properties.load( propertiesStream );
            encryptProperties();
            propertiesFileLastModified = propertiesFile.lastModified();
        }
        finally
        {
            if( propertiesStream != null )
            {
                propertiesStream.close();
            }
        }
    }

    /**
     * Persists the properties to disk.  This should be called
     * after any changes to the configuration made by the user.
     */
    private void saveProperties() throws IOException
    {

        OutputStream propertiesStream = null;
        try
        {
            propertiesStream = new FileOutputStream( propertiesFile );
            properties.store( propertiesStream, PROPERTIES_HEADER );
        }
        finally
        {
            if( propertiesStream != null )
            {
                propertiesStream.close();
            }
        }
    }

    /**
     * Checks to see if the passwords are encrypted or not.  If not, it
     * encrypts them and updates and saves the properties file.
     *
     * @throws IOException thrown if an error occurs saving the updated properties.
     */
    private void encryptProperties()
        throws IOException
    {
        boolean changed = false;

        Enumeration usernames = properties.keys();
        while( usernames.hasMoreElements() )
        {
            String username = (String) usernames.nextElement();
            String password = properties.getProperty( username );
            // If the password is not hashed, hash it now.
            if( password.length() != 60 ) {
                password = encryptPassword( password );
                if( password == null ) {
                    log.error( "Error encrypting plaintext password from user.conf for user " + username );
                    throw new RuntimeException( "Error encrypting password for user: " + username );
                }
                properties.setProperty( username, password );
                changed = true;
            }
        }

        // Save the changes.
        if( changed ) saveProperties();
    }

    /**
     * Creates a one-way has of the specified password.  This allows passwords to be
     * safely stored without an easy way to retrieve the original value.
     *
     * @param password the string to encrypt.
     *
     * @return the encrypted password, or null if encryption failed.
     */
    private String encryptPassword( String password ) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");

            //Create the encrypted Byte[]
            md.update( password.getBytes() );
            byte[] hash = md.digest();

            //Convert the byte array into a String

            StringBuffer hashStringBuf = new StringBuffer();
            String byteString;
            int byteLength;

            for( int index = 0; index < hash.length; index++ ) {

                byteString = String.valueOf( hash[index ] + 128 );

                //Pad string to 3.  Otherwise hash may not be unique.
                byteLength = byteString.length();
                switch( byteLength ) {
                case 1:
                    byteString = "00" + byteString;
                    break;
                case 2:
                    byteString = "0" + byteString;
                    break;
                }
                hashStringBuf.append( byteString );
            }

            return hashStringBuf.toString();
        }
        catch( NoSuchAlgorithmException nsae ) {
            log.error( "Error getting password hash - " + nsae.getMessage() );
            return null;
        }
    }

    //***************************************************************
    // Watchdog Inner Class
    //***************************************************************

    /**
     * Checks the user configuration file and reloads it if it is new.
     */
    class ConfigurationFileWatcher extends Thread {

        /**
         * Initialize the thread.
         */
        public ConfigurationFileWatcher() {
            super( "SSHWebProxy Configuration Watchdog" );
            setDaemon( true );
        }

        /**
         * Check the timestamp on the file to see
         * if it has been updated.
         */
        public void run() {
            long sleepTime = 10 * 1000;
            while( true )
            {
                try {
                    Thread.sleep( sleepTime );
                    if( propertiesFile.lastModified() > propertiesFileLastModified ) {
                        log.info( "Configuration File Changed, reloading..." );
                        loadProperties();
                    }
                }
                catch( Throwable throwable ) {
                    log.error( "Error in ConfigurationWatcher thread.  Thread will continue to execute. " + throwable, throwable );
                }
            }
        }
    }
}