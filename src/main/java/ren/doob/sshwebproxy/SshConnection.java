
package ren.doob.sshwebproxy;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.transport.HostKeyVerification;
import com.sshtools.j2ssh.transport.TransportProtocolException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.InvalidSshKeyException;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.configuration.SshConnectionProperties;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.configuration.ConfigurationException;

import java.text.MessageFormat;
import java.io.IOException;
import java.util.*;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SshConnection implements SshConstants {

    /** The SSHClient instance */
    private SshClient sshClient;

    /** Information about the current connection */
    private String connectionInfo;

    /** Stores all active SshChannels. */
    private Map channelMap;

    /** The next ID to assign to a channel */
    private int nextChannelId = 0;

    /** Logger */
    private static final Log log = LogFactory.getLog( SshConnection.class );

    //***************************************************************
    // Static Initialization
    //***************************************************************

    // Initialize the SSH Library
    static
    {
        try
        {
            ConfigurationLoader.initialize( false );
        }
        catch (ConfigurationException e)
        {
            log.error( "Error configuring SSH Library: " + e, e );
            throw new RuntimeException( "Unable to initialize SSH Library: " + e, e );
        }
    }

    //***************************************************************
    // Constructors
    //***************************************************************

    /**
     * Performs common constructor logic.
     */
    private SshConnection()
    {
        channelMap = new HashMap();
    }

    /**
     * Initialize a new SshConnection with the SshClient connection.
     *
     * @param sshClient the sshClient that represents the connection.
     */
    public SshConnection( SshClient sshClient, String connectionInfo )
    {
        this();

        this.connectionInfo = connectionInfo;

        this.sshClient = sshClient;
    }

    /**
     * Create a new SshConnection to the specified location
     * with the specified username and password.
     *
     * @param host the remote host to connect to.
     * @param port the port to connect to.
     * @param username the username to login with.
     * @param password the password to login with.
     * @throws SshConnectException thrown if the connection attempt failes for any reason.
     */
    public SshConnection( String host, int port, String username, String password )
        throws SshConnectException
    {
        this();

        // Verify the parameters are not null or invalid.
        if( host == null || host.trim().length() == 0 || port < 1 ||
            username == null || username.trim().length() == 0 ||
            password == null || password.trim().length() == 0 )
        {
            throw new SshConnectException( "Missing parameter.  All parameters must be at least one character." );
        }

        connectionInfo = getConnectionInfo( host, port, username );

        if( log.isDebugEnabled() ) log.debug( connectionInfo + " - Attempting to Open Connection." );

        // Initialize the SSH library
        sshClient = new SshClient();
        sshClient.setSocketTimeout( 30000 );
        SshConnectionProperties properties = new SshConnectionProperties();
        properties.setHost( host );
        properties.setPort( port );
        properties.setPrefPublicKey("ssh-dss");

        // Connect to the host
        try
        {
            sshClient.connect(properties, new HostKeyVerificationImpl() );

            log.debug( "Connect Successful." );

            // Initialize the authentication data.
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername(username);
            pwd.setPassword(password);

            // Authenticate
            int result = sshClient.authenticate(pwd);
            if( result != AuthenticationProtocolState.COMPLETE )
            {
                throw new SshConnectException( "Authentication Error.  Invalid username or password." );
            }

            log.debug( "Authentication Successful." );
        }
        catch( UnknownHostException unknownHostException )
        {
            throw new SshConnectException( "Unable to connect.  Unknown host." );
        }
        catch( IOException ioException )
        {
            log.warn( "IOException occured in SshConnection constructor.  " + ioException, ioException );
            throw new SshConnectException( "Unable to connect to host." );
        }

        // Success!
        if( log.isInfoEnabled() ) log.info( connectionInfo + " - Connection opened successfully." );
    }

    /**
     * Create a new SshConnection to the specified location
     * with the specified username and key.
     *
     * @param host the remote host to connect to.
     * @param port the port to connect to.
     * @param username the username to login with.
     * @param key the SSH Key as a byte array.
     * @param keyPassPhrase the passPharse for the key (optional)
     * @throws SshConnectException thrown if the connection attempt failes for any reason.
     */
     public SshConnection( String host, int port, String username, byte[] key, String keyPassPhrase )
             throws SshConnectException
    {
        this();

        // Verify the parameters are not null or invalid.
        if( host == null || host.trim().length() == 0 || port < 1 ||
            username == null || username.trim().length() == 0 ||
            key == null )
        {
            throw new SshConnectException( "Missing parameter.  All parameters must be at least one character." );
        }

        connectionInfo = getConnectionInfo( host, port, username );

        if( log.isDebugEnabled() ) log.debug( connectionInfo + " - Attempting to Open Connection." );

        // Initialize the SSH library
        sshClient = new SshClient();
        sshClient.setSocketTimeout( 30000 );
        SshConnectionProperties properties = new SshConnectionProperties();
        properties.setHost( host );
        properties.setPort( port );

        // Connect to the host
        try
        {
            sshClient.connect(properties, new HostKeyVerificationImpl() );

            log.debug( "Connect Successful." );

            // Initialize the authentication data.
            PublicKeyAuthenticationClient publicKeyAuth = new PublicKeyAuthenticationClient();

            publicKeyAuth.setUsername(username);

            SshPrivateKeyFile file = SshPrivateKeyFile.parse( key );
            SshPrivateKey privateKey = file.toPrivateKey( keyPassPhrase );
            publicKeyAuth.setKey( privateKey );

            // Authenticate
            int result = sshClient.authenticate( publicKeyAuth );
            if( result != AuthenticationProtocolState.COMPLETE )
            {
                throw new SshConnectException( "Authentication Error.  Invalid username or password." );
            }

            log.debug( "Authentication Successful." );
        }
        catch( InvalidSshKeyException invalidSshKeyException )
        {
            throw new SshConnectException( "Unable to connect.  Invalid SSH Key.  " + invalidSshKeyException.getMessage() );
        }
        catch( UnknownHostException unknownHostException )
        {
            throw new SshConnectException( "Unable to connect.  Unknown host." );
        }
        catch( IOException ioException )
        {
            log.warn( "IOException occured in SshConnection constructor.  " + ioException, ioException );
            throw new SshConnectException( "Unable to connect to host." );
        }

        // Success!
        if( log.isInfoEnabled() ) log.info( connectionInfo + " - Connection opened successfully." );
    }
    //***************************************************************
    // Parameter Access Methods
    //***************************************************************

    /**
     * Returns information about this connection.  The information
     * consists of the username, the host, and the port.  The result
     * is formatted as: username@host:port
     *
     * @return formated string: username@host:port
     */
    public String getConnectionInfo()
    {
        return connectionInfo;
    }

    /**
     * Helper method to return the connection info.
     *
     * @param host
     * @param port
     * @param username
     * @return a propertly formatted connection info string.
     */
    public static String getConnectionInfo( String host, String port, String username )
    {
        return MessageFormat.format( "{0}@{1}:{2}", new String[] { username.trim(), host.trim(), port.trim() } );
    }

    /**
     * Helper method to return the connection info.
     *
     * @param host
     * @param port
     * @param username
     * @return a propertly formatted connection info string.
     */
    public static String getConnectionInfo( String host, int port, String username )
    {
        return getConnectionInfo( host, String.valueOf( port ), username );
    }

    //***************************************************************
    // Public Methods
    //***************************************************************

    /**
     * Returns true if this SshConnection is open.
     *
     * @return true if it is open.
     */
    public boolean isOpen()
    {
        return sshClient.isConnected();
    }

    /**
     * Closes all open channels and the current SshConnection.
     */
    public void close()
    {
        if( log.isInfoEnabled() ) log.info( connectionInfo + " - Closing Connection." );

        Iterator shellChannels = channelMap.values().iterator();
        SshChannel shellChannel;
        while( shellChannels.hasNext() )
        {
            shellChannel = (SshChannel) shellChannels.next();
            shellChannel.close();
        }

        channelMap.clear();

        sshClient.disconnect();
    }

    /**
     * Returns the requested channel.
     *
     * @param channelId the channel's unique id.
     * @return the requested channel, or null if it does not exist.
     */
    public SshChannel getChannel( String channelId )
    {
        return (SshChannel) channelMap.get( channelId );
    }

    /**
     * Returns all channels
     */
    public Collection getChannels()
    {
        return channelMap.values();
    }

    /**
     * Open a new Shell Channel for this connection.
     *
     * @return a newly opened ShellChannel
     * @throws SshConnectException if the channel could not be opened.
     */
    public ShellChannel openShellChannel() throws SshConnectException
    {
        if( log.isInfoEnabled() ) log.info( connectionInfo + " - Opening new ShellChannel" );

        try
        {
            SessionChannelClient sessionChannelClient = sshClient.openSessionChannel();
            ShellChannel shellChannel = new VT100ShellChannel( this, sessionChannelClient );

            // Generate a channelId for the channel and add it to the local map.
            String channelId = String.valueOf( nextChannelId++ );
            shellChannel.setChannelId( channelId );
            channelMap.put( channelId, shellChannel );

            return shellChannel;
        }
        catch (IOException ioException) {
            log.warn( "openShellChannel failed, unable to open Session Channel: " + ioException, ioException );
            throw new SshConnectException( "Unable to open SessionChannel." );
        }
    }


    /**
     * Returns the requested channel.
     *
     * @param channelId the channel's unique id.
     * @return the requested channel, or null if it does not exist.
     */
    public ShellChannel getShellChannel( String channelId )
    {
        SshChannel channel = (SshChannel) channelMap.get( channelId );

        // Return null if it does not exist or is the wrong type of channel.
        if( channel == null || !( channel instanceof ShellChannel ) )
        {
            return null;
        }

        return (ShellChannel) channel;
    }

    /**
     * Returns a collection of all ShellChannels associated with this
     * connection.
     *
     * @return will never be null.
     */
    public Collection getShellChannels()
    {
        ArrayList shellChannels = new ArrayList();
        Iterator channelIterator = channelMap.values().iterator();
        SshChannel sshChannel;
        while( channelIterator.hasNext() )
        {
            sshChannel = (SshChannel) channelIterator.next();
            if( CHANNEL_TYPE_SHELL.equals( sshChannel.getChannelType() ) )
            {
                shellChannels.add( sshChannel );
            }

        }

        return shellChannels;
    }


    /**
     * Close a specfic channel.  This calls channel.close()
     * and removes it from the channel list.
     *
     * @param channelId the channel to remove.
     */
    public void closeChannel( String channelId )
    {
        SshChannel sshChannel = getChannel( channelId );
        if( sshChannel != null )
        {
            sshChannel.close();
            channelMap.remove( sshChannel.getChannelId() );
        }
    }

    /**
     * Close a specfic channel.  This calls channel.close()
     * and removes it from the channel list.
     *
     * @param sshChannel the channel to remove.
     */
    public void closeChannel( SshChannel sshChannel )
    {
        sshChannel.close();
        channelMap.remove( sshChannel.getChannelId() );
    }

    //***************************************************************
    // Object Methods
    //***************************************************************

    /**
     * Return a string representation of this connection.
     * @return
     */
    public String toString() {
        String[] args = new String[] { getConnectionInfo(), String.valueOf( sshClient.getActiveChannelCount() ) };
        return MessageFormat.format( "Connected to {0} with {1} open channels.", args );
    }

    //***************************************************************
    // Inner Classes
    //***************************************************************

    /**
     * Handles the HostKeyVerification.  Current implementation accepts
     * all keys.
     *
     * @author Eric Daugherty
     */
    private class HostKeyVerificationImpl implements HostKeyVerification
    {
        /**
         * Determines if the host key should be accepted.
         * @param string
         * @param sshPublicKey
         * @return
         * @throws TransportProtocolException
         */
        public boolean verifyHost(String string, SshPublicKey sshPublicKey) throws TransportProtocolException {
            //TODO: Add real logic here to handle Host Key Validation.
            log.debug( "Verifying Host: " + string );
            return true;
        }
    }
}
