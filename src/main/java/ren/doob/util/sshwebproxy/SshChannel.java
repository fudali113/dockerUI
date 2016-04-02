
package ren.doob.util.sshwebproxy;

public abstract class SshChannel {


    /** The type of channel, Shell, SCP, etc */
    private String channelType;

    /** The unique id of this ShellChannel */
    private String channelId;

    /** The SshConnection this channel is using */
    protected SshConnection sshConnection;


    /**
     * 构造一个sshchannel对象
     *
     * @param channelType channel的状态
     * @param sshConnection 连接了的.SshConnection对象
     */
    public SshChannel( String channelType, SshConnection sshConnection )
    {
        this.channelType = channelType;
        this.sshConnection = sshConnection;
    }


    /**
     * Close the SessionChannelClient and all internal
     * streams.  This should only be called by the SshConnection
     * class and never directly called by a SshChannel implementation.
     * Channels should call SshConnection.closeChannel( this ) to close
     * itself.
     */
    public abstract void close();

    /**
     * Indicates whether this connection is still active.
     *
     * @return true if this connection is still active.
     */
    public abstract boolean isConnected();

    /**
     * Returns the page that should be used to display this Channel.
     *
     * @return URL of the page used to display this channel.
     */
    public abstract String getPage();



    /** The type of channel, Shell, SCP, etc */
    public String getChannelType() {
        return channelType;
    }

    /**
     * The unique ID of this connection
     *
     * @return
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * The unique ID of this connection
     *
     * @param channelId
     */
    protected void setChannelId(String channelId) {
        this.channelId = channelId;
    }



    /**
     * Replaces any HTML control characters with their escaped value.
     *
     * @param data the string to check
     * @return the encoded string.
     */
    public String encodeHTML( String data )
    {
        // Replace HTML Characters
        data = data.replaceAll( "<", "&lt;" );
        data = data.replaceAll( ">", "&gt;" );

        return data;
    }
}
