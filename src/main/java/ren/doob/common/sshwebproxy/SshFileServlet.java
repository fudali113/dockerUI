/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SshFileServlet.java,v $
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

package ren.doob.common.sshwebproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * Handles interaction between the FileChannel and the UI.
 *
 * @author Eric Daugherty
 */
public class SshFileServlet extends HttpServlet implements SshConstants {

    //***************************************************************
    // Variables
    //***************************************************************

    /** Logger */
    private static final Log log = LogFactory.getLog( SshFileServlet.class );

    //***************************************************************
    // HTTPServlet Methods
    //***************************************************************

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost( request, response );
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Validate login.
        SshSession sshSession = new SshSession( request );
        if( !sshSession.isValid() )
        {
            response.sendRedirect( SshConstants.PAGE_LOGIN );
            return;
        }

        if( FileUpload.isMultipartContent( request ) )
        {
            upload( request, response );
        }
        else
        {
            String action = request.getParameter( PARAMETER_ACTION );

            // Verify we received an action to perform.
            if( action == null || action.trim().length() == 0 )
            {
                log.warn( "POST Request received without an action parameter." );
                response.sendRedirect( PAGE_HOME );
            }

            action = action.trim();
            if( ACTION_DOWNLOAD.equals( action ) )
            {
                download( request, response );
            }
            else if( ACTION_CHANGE_DIRECTORY.equals( action ) )
            {
                changeDirectory( request, response );
            }
            else
            {
                log.warn( "POST Request received with an invalid action parameter: " + action );
                response.sendRedirect( PAGE_HOME );
            }
        }
    }

    //***************************************************************
    // Private Action Handlers
    //***************************************************************

    /**
     * Download a file to the client.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void download( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Download request received." );

        SshSession session = new SshSession( request );
        String connectionInfo = request.getParameter( PARAMETER_CONNECTION );
        String channelId = request.getParameter( PARAMETER_CHANNEL );
        String filename = request.getParameter( PARAMETER_FILENAME );

        // Get the Channel
        SshConnection sshConnection = session.getSshConnection( connectionInfo );
        FileChannel fileChannel = null;
        if( sshConnection != null )
        {
            fileChannel = sshConnection.getFileChannel( channelId );
            if( fileChannel != null )
            {
                // Setup the headers information
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);

                // Start writing the output.
                ServletOutputStream outputStream = response.getOutputStream();
                fileChannel.downloadFile( filename, outputStream );
            }
        }
    }

    /**
     * Upload a file from the client.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void upload( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Upload request received." );

        DiskFileUpload upload = new DiskFileUpload();

        SshSession session = new SshSession( request );
        String connectionInfo = "";
        String channelId = "";
        String fileName = "";
        FileItem file = null;
        String redirectPage = PAGE_HOME;

        try
        {
            // Parse the parts into Parameters and the file.
            List files = upload.parseRequest( request );
            Iterator iter = files.iterator();
            while (iter.hasNext())
            {
                FileItem fileItem = (FileItem) iter.next();
                String fieldName;
                if( fileItem.isFormField() )
                {
                    fieldName = fileItem.getFieldName();
                    if( PARAMETER_CONNECTION.equals( fieldName ) )
                    {
                        connectionInfo = fileItem.getString();
                    }
                    else if( PARAMETER_CHANNEL.equals( fieldName ) )
                    {
                        channelId = fileItem.getString();

                    }
                    else if( PARAMETER_FILENAME.equals( fieldName ) )
                    {
                        fileName = fileItem.getString();
                    }
                }
                else
                {
                    file = fileItem;
                }
            }

            // Get the Channel
            SshConnection sshConnection = session.getSshConnection( connectionInfo );
            FileChannel fileChannel = null;
            if( sshConnection != null )
            {
                fileChannel = sshConnection.getFileChannel( channelId );
                if( fileChannel != null )
                {
                    redirectPage = fileChannel.getPage();

                    // Verify the input parameters.
                    if( fileName == null || fileName.length() == 0 )
                    {
                        session.setErrorMessage( "The Remote Filename must be specified." );
                    }
                    // Upload the file.
                    else
                    {
                        fileChannel.uploadFile( fileName, file.getInputStream() );
                        redirectPage = fileChannel.getPage();
                    }
                }
            }
        }
        catch (FileUploadException fileUploadException)
        {
            session.setErrorMessage( "Unable to upload file: " + fileUploadException.getMessage() );
            log.warn( "Error uploading file from client: " + fileUploadException, fileUploadException );
        }

        response.sendRedirect( redirectPage );
    }


    /**
     * Change the current directory.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void changeDirectory( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        log.debug( "Download request received." );
        SshSession session = new SshSession( request );
        String connectionInfo = request.getParameter( PARAMETER_CONNECTION );
        String channelId = request.getParameter( PARAMETER_CHANNEL );
        String directory = request.getParameter( PARAMETER_DIRECTORY );
        String redirectPage = PAGE_HOME;

        // Get the Channel
        SshConnection sshConnection = session.getSshConnection( connectionInfo );
        FileChannel fileChannel = null;
        if( sshConnection != null )
        {
            fileChannel = sshConnection.getFileChannel( channelId );
            if( fileChannel != null )
            {
                if( !fileChannel.changeDirectory( directory ) )
                {
                    session.setErrorMessage( "Directory Change failed." );
                }
                redirectPage = fileChannel.getPage();
            }
        }

        // Redirect to the result page.
        response.sendRedirect( redirectPage );
    }
}
