package ren.doob.sshwebproxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class SshSession {



    private static final String USER = "User";

    private static final String RESTRICTED_MODE_HOST = "RestrictedModeHost";

    private static final String ERROR_MESSAGE = "ErrorMessage";

    private static final String SSH_CONNECTIONS = "SshConnections";


    /** The HttpSession this class is wrapping. */
    private HttpSession session;

    /** Logger */
    private static final Log log = LogFactory.getLog( ShellChannel.class );


    /**
     * Helper constructor.  Creates a new SSHWebProxy using
     * the current HttpServletRequest.
     *
     * @param request the current HttpServletRequest
     */
    public SshSession( HttpServletRequest request )
    {
        this( request.getSession() );
    }

    /**
     * Creates a wrapper for the current HttpSession.
     *
     * @param session user's HttpSession
     */
    public SshSession( HttpSession session )
    {
        this.session = session;
    }


    /**
     * Checks to makes sure that a user has logged into the system.
     *
     * @return true if a user is logged in.
     */
    public boolean isValid()
    {
        // If we are in restricted mode, the session is valid.
        if( isRestrictedMode() )
        {
            return true;
        }

        // Otherwise, check for a user.
        String username = (String) session.getAttribute( USER );
        if( log.isDebugEnabled() ) log.debug( "Verifying user is logged in.  Username: " + username );
        if( username == null )
        {
            return false;
        }
        return true;
    }

    /**
     * Returns the name of the current logged-in user,
     * or null if no user is logged in.
     *
     * @return Username, or null.
     */
    public String getUser()
    {
        return (String) session.getAttribute( USER );
    }

    /**
     * Sets a username into the session.
     *
     * @param user
     */
    public void setUser( String user )
    {
        session.setAttribute( USER, user );
    }

    public boolean isRestrictedMode()
    {
        String restrictedModeHost = getRestrictedModeHost();
        if( restrictedModeHost != null && restrictedModeHost.length() > 0 )
        {
            return true;
        }
        return false;
    }

    public String getRestrictedModeHost()
    {
        return (String) session.getAttribute( RESTRICTED_MODE_HOST );
    }

    public void setRestrictedModeHost( String host )
    {
        session.setAttribute( RESTRICTED_MODE_HOST, host );
    }

    public String getErrorMessage()
    {
        return (String) session.getAttribute( ERROR_MESSAGE );
    }

    public void setErrorMessage( String errorMessage )
    {
        session.setAttribute( ERROR_MESSAGE, errorMessage );
    }

    /**
     * Returns the SshConnection for the given connectionInfo.
     * Returns null if the connection does not exist or has been closed.
     *
     * @param connectionInfo the connectionInfo unique to this connection.
     * @return the SshConnection or null if it does not exist or has been closed.
     */
    public synchronized SshConnection getSshConnection( String connectionInfo )
    {
        Map sshConnections = getConnectionMap();
        SshConnection sshConnection = (SshConnection) sshConnections.get( connectionInfo );

        // If it is unknown, or open, return it.
        if( sshConnection == null || sshConnection.isOpen() )
        {
            return sshConnection;
        }
        // If it has been closed, remove it and return null.
        else
        {
            if( log.isDebugEnabled() ) log.debug( connectionInfo + " connection is closed, removing from session." );
            sshConnections.remove( connectionInfo );
            return null;
        }
    }

    /**
     * Stores a new SshConnection in the session.
     *
     * @param sshConnection the connection to store.
     * @return returns false if this SshConnection is a duplicate.
     */
    public synchronized boolean addSshConnection( SshConnection sshConnection )
    {
        String connectionInfo = sshConnection.getConnectionInfo();
        Map sshConnections = getConnectionMap();
        if( sshConnections.containsKey( connectionInfo ) )
        {
            log.warn( "Error Adding new SshConnection. A connection already exists with the same connection info: " + connectionInfo );
            return false;
        }

        sshConnections.put( connectionInfo, sshConnection );
        if( log.isDebugEnabled() ) log.debug( connectionInfo + " connection added to session.");
        return true;
    }

    /**
     * Removes an SshConnection from the session.
     *
     * @param connectionInfo
     */
    public synchronized void removeConnection( String connectionInfo )
    {
        if( log.isDebugEnabled() ) log.debug( connectionInfo + " connection removed from session.");
        Map sshConnections = getConnectionMap();
        sshConnections.remove( connectionInfo );
    }

    /**
     * Returns an Collection for the current connections.
     *
     * @return null if no connections exist, or an Collection for the current
     * connections.
     */
    public Collection getConnections()
    {
        Map connections = getConnectionMap();
        return (connections == null) ? null : connections.values();
    }

    /**
     * Removes the ConnectionMap from the Session (actually, the ServletContext).
     */
    void removeConnectionMap()
    {
        ServletContext context = session.getServletContext();
        context.removeAttribute( session.getId() + SSH_CONNECTIONS );
    }

    /**
     * Retrieve the ConnectionMap for this session.  The map is actually stored
     * in the ServletContext so it will be accessible by the SessionCleanup class
     * after the session is destroyed.
     *
     * @return A Map of SshConnections, keyed by the connectionInfo String.
     */
    private Map getConnectionMap()
    {
        ServletContext context = session.getServletContext();
        Map sshConnections = (Map)  context.getAttribute( session.getId() + SSH_CONNECTIONS );

        if( sshConnections == null )
        {
            sshConnections = new HashMap();
            context.setAttribute( session.getId() + SSH_CONNECTIONS, sshConnections );
        }

        return sshConnections;
    }
}
