/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SshShellServlet.java,v $
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
import java.io.IOException;

/**
 * Handles interaction between the ShellChannel and the UI.
 *
 * @author Eric Daugherty
 */
public class SshShellServlet extends HttpServlet implements SshConstants {

    //***************************************************************
    // Variables
    //***************************************************************

    /** Logger */
    private static final Log log = LogFactory.getLog( SshShellServlet.class );

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Validate login.
        SshSession sshSession = new SshSession( request );
        if( !sshSession.isValid() )
        {
            response.sendRedirect( SshConstants.PAGE_LOGIN );
            return;
        }

        String action = request.getParameter( PARAMETER_ACTION );

        // Verify we received an action to perform.
        if( action == null || action.trim().length() == 0 )
        {
            log.warn( "POST Request received without an action parameter." );
            response.sendRedirect( PAGE_HOME );
        }

        action = action.trim();
        if( ACTION_WRITE.equals( action ) )
        {
            if( request.getParameter( "write" ) != null )
            {
                write( request, response, false );
            }
            else if( request.getParameter( "writeline" ) != null )
            {
                write( request, response, true );
            }
            else
            {
                log.warn( "Invalid write request recieved.  Request does not contain write or writeline parameter.");
                response.sendRedirect( PAGE_HOME );
            }
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
     * Write data to a channel.
     * @param request
     * @param response
     * @param sendNewLine
     * @throws IOException
     */
    private void write( HttpServletRequest request, HttpServletResponse response, boolean sendNewLine )
        throws IOException
    {
        log.debug( "Write request received." );

        SshSession session = new SshSession( request );
        String connectionInfo = request.getParameter( "connection" );
        String channelId = request.getParameter( "channel" );
        boolean valid = false;

        // Get the Channel and write to it.
        SshConnection sshConnection = session.getSshConnection( connectionInfo );
        ShellChannel shellChannel = null;
        if( sshConnection != null )
        {
            shellChannel = sshConnection.getShellChannel( channelId );
            if( shellChannel != null )
            {
                String data = request.getParameter( "data" );
                shellChannel.write( data, sendNewLine );
                valid = true;
            }
        }

        // Redirect to the result page.
        if( valid )
        {
            response.sendRedirect( shellChannel.getPage() );
            if( log.isDebugEnabled() ) log.debug( "Successful Write to " + connectionInfo + " " + channelId );
        }
        else
        {
            log.info( "Write request to invalid channel." );
            session.setErrorMessage( "Invalid connection or channel." );
            response.sendRedirect( PAGE_HOME );
        }
    }
}
