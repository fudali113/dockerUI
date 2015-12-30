/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SessionCleanup.java,v $
 * $Revision: 1.1 $
 * $Author: edaugherty $
 * $Date: 2003/12/07 20:48:18 $
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

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Closes any open connections for sessions that
 * have timed out.
 *
 * @author Eric Daugherty
 */
public class SessionCleanup implements HttpSessionListener {

    //***************************************************************
    // Variables
    //***************************************************************

    /** Logger */
    private static final Log log = LogFactory.getLog( SessionCleanup.class );

    //***************************************************************
    // HttpSessionListener Methods
    //***************************************************************

    public void sessionCreated(HttpSessionEvent event) {
        log.debug( "New Session Created" );
    }

    /**
     * Close any opoen SshConnections in the expired session.
     * @param event
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        log.info( "Session Expired" );

        HttpSession httpSession = event.getSession();
        SshSession sshSession = new SshSession( httpSession );

        Collection connections = sshSession.getConnections();
        Iterator connectionIterator = connections.iterator();
        while( connectionIterator.hasNext() )
        {
            SshConnection connection = (SshConnection) connectionIterator.next();
            connection.close();
        }

        sshSession.removeConnectionMap();
    }

}
