

package ren.doob.sshwebproxy;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
