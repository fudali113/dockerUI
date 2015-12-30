/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SshConnectionServlet.java,v $
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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.fileupload.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.io.*;
import java.util.List;
import java.util.Iterator;

/**
 * Handles the managment of SsshConnections, and the creation
 * and closing of SshChannels.
 *
 * @author Eric Daugherty
 */
public class SshConnectionServlet extends HttpServlet implements SshConstants {

    //***************************************************************
    // Variables
    //***************************************************************

    /** Logger */
    private static final Log log = LogFactory.getLog( SshConnectionServlet.class );

    //***************************************************************
    // HTTPServlet Methods
    //***************************************************************

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // Validate login.
        SshSession sshSession = new SshSession( request );
        if( !sshSession.isValid() )
        {
            response.sendRedirect( SshConstants.PAGE_LOGIN );
            return;
        }

        String action = request.getParameter( PARAMETER_ACTION );
        List multiPartItems = null;

        if( FileUpload.isMultipartContent( request  ) )
        {
            try
            {
                DiskFileUpload upload = new DiskFileUpload();
                // Parse the parts into Parameters and the file.
                multiPartItems = upload.parseRequest( request );
            }
            catch( FileUploadException fileUploadException )
            {
                log.error( "Error parsing MultiPart Upload!", fileUploadException );
            }

            action = getParameter( multiPartItems, PARAMETER_ACTION );
        }

        // Verify we received an action to perform.
        if( action == null || action.trim().length() == 0 )
        {
            log.warn( "POST Request received without an action parameter." );
            response.sendRedirect( PAGE_HOME );
        }

        action = action.trim();

        if( ACTION_OPEN_CONNECTION.equals( action ) )
        {
            openConnection( request, response, multiPartItems );
        }
        else if( ACTION_CLOSE_CONNECTION.equals( action ) )
        {
            closeConnection( request, response );
        }
        else if( ACTION_OPEN_SHELL_CHANNEL.equals( action ) )
        {
            openShellChannel( request, response);
        }
        else if( ACTION_OPEN_FILE_CHANNEL.equals( action ) )
        {
            openFileChannel( request, response );
        }
        else if( ACTION_CLOSE_CHANNEL.equals( action ) )
        {
            closeChannel( request, response );
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

    /**
     * Attempts to create a new SSH Connection.
     *
     * @param request original HttpServletRequest
     * @param response original HttpServletResponse
     */
    private void openConnection( HttpServletRequest request, HttpServletResponse response, List multiPartItems )
        throws IOException
    {
        log.debug( "Connection request received." );

        SshSession session = new SshSession( request );

        String error = null;
        String host = getParameter( multiPartItems, PARAMETER_HOST );
        String port = getParameter( multiPartItems, PARAMETER_PORT );

        // If we are in restricted mode, override whatever may have come
        // from the client.
        if( session.isRestrictedMode() )
        {
            host = session.getRestrictedModeHost();
            port = "22";
        }

        String username = getParameter( multiPartItems, PARAMETER_USERNAME );
        String authenticationType = getParameter( multiPartItems, PARAMETER_AUTHENTICATION_TYPE );

        // Determine which authentication method to use.
        boolean isKeyAuthentication = false;
        if( authenticationType != null && authenticationType.equals( AUTHENTICATION_TYPE_KEY ) )
        {
            isKeyAuthentication = true;
        }

        // Parse the authentication parameters
        String password = null;
        FileItem keyFileItem = null;
        String keyPassPhrase = null;
        if( !isKeyAuthentication )
        {
            password = getParameter( multiPartItems, PARAMETER_PASSWORD );
        }
        else
        {
            keyFileItem = getFile( multiPartItems );
            keyPassPhrase = getParameter( multiPartItems, PARAMETER_KEY_PASSWORD );
        }

        String channelType = getParameter( multiPartItems, PARAMETER_CHANNEL_TYPE );

        // Validate all input parameters exist.
        if( host == null || host.trim().length() == 0 ||
                port == null || port.trim().length() == 0 )
        {
            error = "Please specify a valid host and port.";
        }
        else if( !isKeyAuthentication && ( username == null || username.trim().length() == 0 ||
                password == null || password.trim().length() == 0 ) )
        {
            error = "Please specify a valid username and password.";
        }
        else if( isKeyAuthentication && ( keyFileItem == null ) )
        {
            error = "Please specify a key file.";
        }

        int portInt = 22;
        try
        {
            portInt = Integer.parseInt( port );
        }
        catch (NumberFormatException numberFormatException )
        {
            error = "The port number must be an integer.";
        }

        if( error == null )
        {
            try {
                // Look for an existing open connection.
                String connectionInfo = SshConnection.getConnectionInfo( host, port, username );
                SshConnection sshConnection = session.getSshConnection( connectionInfo );

                // If the connection does not exist yet, open a new one.
                if( sshConnection == null )
                {
                    log.debug( "Connection does not exist, opening new connection." );
                    if( isKeyAuthentication )
                    {
                        sshConnection = new SshConnection( host, portInt, username, keyFileItem.get(), keyPassPhrase );
                    }
                    else
                    {
                        sshConnection = new SshConnection( host, portInt, username, password );
                    }

                    // If there is a collision adding it to the session, then one
                    // must have been created while we were creating ours.  Close ours
                    // and use the other one.
                    if( !session.addSshConnection( sshConnection ) )
                    {
                        log.warn( "addSshConnection race condition occured.  Closing duplicate session!" );
                        sshConnection.close();
                        sshConnection = session.getSshConnection( connectionInfo );
                        if( sshConnection == null )
                        {
                            log.error( "Failed to get connection from session after closing new connection!  Giving up!" );
                            session.setErrorMessage( "Error Opening Connection." );
                            response.sendRedirect( PAGE_HOME );
                            return;
                        }
                    }
                }

                // Open the requested SshChannel
                if( channelType != null && CHANNEL_TYPE_SHELL.equals( channelType ) )
                {
                    openShellChannel( sshConnection, response );
                }
                else if( channelType != null && CHANNEL_TYPE_FILE.equals( channelType ) )
                {
                    openFileChannel( sshConnection, response );
                }
                else
                {
                    response.sendRedirect( PAGE_HOME );
                }
            }
            catch( SshConnectException sshConnectException )
            {
                if( log.isInfoEnabled() ) log.info( "Connection request failed: " + sshConnectException );
                session.setErrorMessage( sshConnectException.getMessage() );
                response.sendRedirect( PAGE_HOME );
            }
        }
        else
        {
            session.setErrorMessage( error );
            if( log.isInfoEnabled() ) log.info( "Connection request failed due to input validation error: " + error );
            response.sendRedirect( PAGE_HOME );
        }
    }

    /**
     * Handles requests to close an SshConnection.
     *
     * @param request
     * @param response
     */
    private void closeConnection( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Close Connection request received." );

        SshSession sshSession = new SshSession( request );

        SshConnection sshConnection = getConnection( request, sshSession );

        // If the sshConnection could not be found, give up.
        if( sshConnection == null )
        {
            sshSession.setErrorMessage( "Requested sshConnection could not be found.");
        }
        else
        {
            // Close the Ssh Connection.
            String connectionInfo = sshConnection.getConnectionInfo();
            sshConnection.close();
            sshSession.removeConnection( connectionInfo );
        }

        response.sendRedirect( PAGE_HOME );
        return;
    }

    /**
     * Handles requests to open a ShellChannel on an existing
     * SshConnection.
     *
     * @param request
     * @param response
     */
    private void openShellChannel( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Open Shell Channel request received." );

        SshSession sshSession = new SshSession( request );

        SshConnection connection = getConnection( request, sshSession );

        // If the connection could not be found, give up.
        if( connection == null )
        {
            sshSession.setErrorMessage( "Requested connection could not be found.");
            response.sendRedirect( PAGE_HOME );
        }
        // Try to open a ShellChannel
        else
        {
            openShellChannel( connection, response );
        }
    }

    /**
     * Handles requests to open a ShellChannel on an existing
     * SshConnection.
     *
     * @param sshConnection
     * @param response
     * @throws IOException
     */
    private void openShellChannel( SshConnection sshConnection, HttpServletResponse response )
        throws IOException
    {
        try
        {
            ShellChannel shellChannel = sshConnection.openShellChannel();
            response.sendRedirect( shellChannel.getPage() );
        }
        catch (SshConnectException sshConnectException)
        {
            log.warn( "Error opening new ShellChannel for sshConnection: " + sshConnection.getConnectionInfo() + ": " + sshConnectException, sshConnectException );
            response.sendRedirect( PAGE_HOME );
        }
    }

    /**
     * Handles requests to open a FileChannel on an existing
     * SshConnection.
     *
     * @param request
     * @param response
     */
    private void openFileChannel( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Open File Channel request received." );

        SshSession sshSession = new SshSession( request );

        SshConnection connection = getConnection( request, sshSession );

        // If the connection could not be found, give up.
        if( connection == null )
        {
            sshSession.setErrorMessage( "Requested connection could not be found.");
            response.sendRedirect( PAGE_HOME );
        }
        // Try to open a ShellChannel
        else
        {
            openFileChannel( connection, response );
        }
    }

    /**
     * Handles requests to open a FileChannel on an existing
     * SshConnection.
     *
     * @param sshConnection
     * @param response
     * @throws IOException
     */
    private void openFileChannel( SshConnection sshConnection, HttpServletResponse response )
        throws IOException
    {
        try
        {
            FileChannel fileChannel = sshConnection.openFileChannel();
            response.sendRedirect( fileChannel.getPage() );
        }
        catch (SshConnectException sshConnectException)
        {
            log.warn( "Error opening new FileChannel for sshConnection: " + sshConnection.getConnectionInfo() + ": " + sshConnectException, sshConnectException );
            response.sendRedirect( PAGE_HOME );
        }
    }

    /**
     * Handles requests to open a close an existing channel.
     *
     * @param request
     * @param response
     */
    private void closeChannel( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Close Channel request received." );

        SshSession sshSession = new SshSession( request );

        SshConnection connection = getConnection( request, sshSession );

        // If the connection could not be found, give up.
        if( connection == null )
        {
            sshSession.setErrorMessage( "Requested connection could not be found.");
        }
        // Close a channel.
        else
        {
            String channelId = request.getParameter( PARAMETER_CHANNEL );
            connection.closeChannel( channelId );
        }

        response.sendRedirect( PAGE_HOME );
    }


    /**
     * Returns the SshConnection that is associated with this request, or null
     * if the session can not be found.
     *
     * @param request
     * @param sshSession
     * @return the requested SshSession, or null.
     */
    private SshConnection getConnection( HttpServletRequest request, SshSession sshSession )
    {
        String connectionInfo = request.getParameter( PARAMETER_CONNECTION );
        SshConnection sshConnection = null;
        if( connectionInfo != null && connectionInfo.trim().length() > 0 )
        {
            sshConnection = sshSession.getSshConnection( connectionInfo );
        }

        return sshConnection;
    }

    private String getParameter( List multiPartItems, String parameterName )
    {
        Iterator iter = multiPartItems.iterator();
        while (iter.hasNext())
        {
            FileItem fileItem = (FileItem) iter.next();
            if( fileItem.isFormField() )
            {
                if( parameterName.equals( fileItem.getFieldName() ) )
                {
                    return fileItem.getString();
                }
            }
        }
        return null;
    }

    private FileItem getFile( List multiPartItems )
    {
        Iterator iter = multiPartItems.iterator();
        while (iter.hasNext())
        {
            FileItem fileItem = (FileItem) iter.next();
            if( !fileItem.isFormField() )
            {
                return fileItem;
            }
        }
        return null;
    }
}
