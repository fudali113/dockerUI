package ren.doob.common.sshwebproxy;

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
        if( username == null )
        {
            return false;
        }
        return true;
    }

    /**
     * 返回当前登录用户的名称, 或零如果没有登录用户。
     *
     * @return Username, or null.
     */
    public String getUser()
    {
        return (String) session.getAttribute( USER );
    }

    /**
     * 设置一个用户名到会话中。
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
     * 返回给定connectionInfo SshConnection。
     * 返回null如果连接不存在o closed.r
     *
     * @param connectionInfo connectionInfo独特的连接。
     * @return SshConnection或null如果它不存在或已被关闭。
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
            sshConnections.remove( connectionInfo );
            return null;
        }
    }

    /**
     * 将新的SshConnection存储在会话中。
     *
     * @param sshConnection the connection to store.
     * @return 如果这个SshConnection重复返回false。
     */
    public synchronized boolean addSshConnection( SshConnection sshConnection )
    {
        String connectionInfo = sshConnection.getConnectionInfo();
        Map sshConnections = getConnectionMap();
        if( sshConnections.containsKey( connectionInfo ) )
        {
            return false;
        }

        sshConnections.put( connectionInfo, sshConnection );
        return true;
    }

    /**
     * 从会话中删除一个SshConnection。
     *
     * @param connectionInfo
     */
    public synchronized void removeConnection( String connectionInfo )
    {
        Map sshConnections = getConnectionMap();
        sshConnections.remove( connectionInfo );
    }

    /**
     * 返回一个收集当前连接。
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
     * 检索的ConnectionMap会话。地图是存储　　*将访问ServletContext中所以SessionCleanup类　　*会议后销毁.
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
