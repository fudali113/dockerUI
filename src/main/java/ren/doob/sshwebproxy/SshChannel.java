/******************************************************************************
 * $Source: /cvsroot/sshwebproxy/src/java/com/ericdaugherty/sshwebproxy/SshChannel.java,v $
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

/**
 * Provides a base class for all Ssh Channels.
 *
 * @author Eric Daugherty
 */
public abstract class SshChannel {

    //***************************************************************
    // Variables
    //***************************************************************

    /** The type of channel, Shell, SCP, etc */
    private String channelType;

    /** The unique id of this ShellChannel */
    private String channelId;

    /** The SshConnection this channel is using */
    protected SshConnection sshConnection;

    //***************************************************************
    // Constructor
    //***************************************************************

    /**
     * Create a new SshChannel of the specified type.
     *
     * @param channelType the type of channel.
     * @param sshConnection the connection to the SSH Server.
     */
    public SshChannel( String channelType, SshConnection sshConnection )
    {
        this.channelType = channelType;
        this.sshConnection = sshConnection;
    }

    //***************************************************************
    // Abstract Methods
    //***************************************************************

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

    //***************************************************************
    // Parameter Access Methods
    //***************************************************************

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

    //***************************************************************
    // Public methods
    //***************************************************************

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
