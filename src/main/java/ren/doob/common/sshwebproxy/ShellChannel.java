
package ren.doob.common.sshwebproxy;


import java.util.ArrayList;
import java.io.*;

import com.sshtools.j2ssh.session.SessionChannelClient;

public class ShellChannel extends SshChannel implements SshConstants {


    /** The number of columns to display as a screen */
    protected int screenWidth = 80;

    /** The number of rows to display as a screen */
    protected int screenHeight = 24;

    /** The total number of rows to store */
    private int bufferMaxSize = 1000;

    /** The number of milliseconds to pause before reading */
    private int readPause = 250;

    /** The size of the buffer to read from the server */
    private int readBufferSize = 4048;

    /** The row that the cursor is currently on */
    private int cursorRow = -1;

    /** The column that the cursor is currently on */
    private int cursorColumn = -1;

    /** The Channel for the current Shell Connection */
    protected SessionChannelClient sshChannel;

    /** The Input Reader for the SSH shell connection. */
    private BufferedReader reader;

    /** The Output Writer for the SSH shell connection */
    private PrintWriter writer;

    /** The entire stored buffer. */
    private ArrayList buffer;


    /**
     * Opens a vt100 terminal session transfer session with the server.
     *
     * @param sshConnection 传入一个连接了的sshConnection
     * @param sshChannel SSH API 的channel
     * @throws SshConnectException 抛出异常
     * the connection.
     */
    public ShellChannel( SshConnection sshConnection, SessionChannelClient sshChannel )
        throws SshConnectException
    {
        super( CHANNEL_TYPE_SHELL, sshConnection );

        this.sshChannel = sshChannel;

        // Initialize the channel for a shell session.
        try
        {
            if( !sshChannel.requestPseudoTerminal("vt100", getScreenWidth(), getScreenHeight(), 0, 0, "") )
            {
                throw new SshConnectException( "Unable to establish PseudoTerminal for new ShellChannel." );
            }
            else if( !sshChannel.startShell() )
            {
                throw new SshConnectException( "Unable to start Shell for new ShellChannel." );
            }

            writer = new PrintWriter( new OutputStreamWriter( sshChannel.getOutputStream() ) );
            reader = new BufferedReader( new InputStreamReader( sshChannel.getInputStream() ) );

            buffer = new ArrayList( bufferMaxSize );
        }
        catch( IOException ioException )
        {
            throw new SshConnectException( "Unable to establish Shell Connection.  IOExeption occured: " + ioException );
        }
    }


    /**
     * 清空之前所有reader和writer储存的数据
     * 只对当前连接的sshconnection有效
     * This should only be called by the SshConnection
     * class and never directly called from this class.
     */
    public void close()
    {
        // Close Readers and Writers.
        if( reader != null )
        {
            try
            {
                reader.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            reader = null;
        }
        if( writer != null )
        {
            writer.close();
            writer = null;
        }

        // Close the channel if it is open.
        if( sshChannel.isOpen() )
        {
            try
            {
                sshChannel.close();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Indicates whether this connection is still active.
     *
     * @return true if this connection is still active.
     */
    public boolean isConnected()
    {
        return !sshChannel.isClosed();
    }

    /**
     * Returns the page that should be used to display this Channel.
     *
     * @return
     */
    public String getPage() {
        return PAGE_SHELL_HOME + "?connection=" + sshConnection.getConnectionInfo() + "&channel=" + getChannelId();
    }



    /**
     * The number of columns to display as a screen
     */
    public int getScreenWidth()
    {
        return screenWidth;
    }

    /**
     * The number of rows to display as a screen
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * The total number of rows to store in the buffer.
     *
     * @return number of rows to store.
     */
    public int getBufferMaxSize() {
        return bufferMaxSize;
    }

    /**
     * The number of milliseconds to pause before reading
     * data.  This helps reduce the number of read requests
     * after a write request.
     *
     * @return the number of milliseconds to pause.
     */
    public int getReadPause() {
        return readPause;
    }

    /**
     * The maximum amount of data to read from the server
     * for each read() call.
     *
     * @return the max read buffer size.
     */
    public int getReadBufferSize() {
        return readBufferSize;
    }

    /**
     * The index of the row the cursor is on.  The cursor location
     * is assumed to be after that last character from the server.
     *
     * @return the row index of the cursor.
     */
    public int getCursorRow() {
        return cursorRow;
    }

    /**
     * The index of the row the cursor is on.  The cursor location
     * is assumed to be after that last character from the server.
     *
     * @return the column index of the cursor.
     */
    public int getCursorColumn() {
        return cursorColumn;
    }



    /**
     * 执行一个读的输入数据和填充缓冲区。.
     * 这个方法应该在getScreen or getBuffer之前调用
     */
    public void read()
    {
        // We want to read even if the channel has been closed, because
        // the BufferedReader may have buffered some input, so do the
        // check after this read.  But if the reader is null, just
        // ignore the call.
        if( reader == null )
        {
            System.out.println( "read called on null reader.  Ignoring." );
            return;
        }

        // Read from the server
        try
        {
            // Initialize the input buffer.
            char[] inputBuffer = new char[readBufferSize];
            String input = null;

            // Sleep for the read pause.  This allows the server
            // to send us the 'full' data.  If we don't sleep,
            // the user may just have to do a refresh right away
            // anyway.
            try
            {
                Thread.sleep( getReadPause() );
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // If there is data ready, go ahead and read it.
            if( reader.ready() )
            {
                // read the data and run it through the processor.
                int count = reader.read( inputBuffer );
                input = process( inputBuffer, count );

                fillBuffer( input );

                // Check to see if the channel was closed.
                if( !isConnected() )
                {
                    if( !reader.ready() )
                    {

                        // Notify the sshConnection that this channel is closed.
                        sshConnection.closeChannel( this );
                    }
                    else
                    {
                    }
                }
            }
            else
            {
            }
        }
        catch( IOException ioException )
        {
            ioException.printStackTrace();
        }
    }

    /**
     * 将数据写入SSH服务器和发送一个换行符charecter
     * sendNewLine为true时发送"\n"
     *
     * @param data 把data发送到ssh server
     * @param sendNewLine 为true时发送一个换行符
     */
    public void write( String data, boolean sendNewLine )
    {
        // Don't write if the channel is closed.
        if( !isConnected() )
        {
            return;
        }

        // Verify the writer is not null.
        if( writer == null )
        {
            return;
        }

        // Encode the data for output.  Convert any control characters to
        // the correct char value.
        char[] output = encodeOutput( data );

        // Write the output, and send a new line if requested.
        writer.print( output );
        if( sendNewLine )
        {
            writer.print( "\n" );
        }
        writer.flush();
    }

    /**
     * 将读取的数据添加到缓冲区。
     * @param input the processed data read from the server 从服务器读取处理过的数据
     */
    public void fillBuffer( String input )
    {
        // Add data to the buffer.
        String[] lines = input.split( "\r\n" );
        int startIndex = 0;

        // Append the first line on the end of the last line.
        if( buffer.size() > 0 && lines.length > 0 )
        {
            int lastIndex = buffer.size() - 1;
            buffer.set( lastIndex, ((String) buffer.get( lastIndex ) ) + lines[0] );
            startIndex = 1;
        }

        // Add the rest of the new lines.
        for( int index = startIndex; index < lines.length; index++ )
        {
            buffer.add( lines[index] );
        }

        // Append a new empty line if the last line ended with a line feed.
        if( input.lastIndexOf( "\r\n" ) == input.length() - 2 )
        {
            buffer.add( "" );
        }

        // Remove any extra rows from the begining of the buffer.
        int currentBufferSize = buffer.size();
        if( currentBufferSize > bufferMaxSize )
        {
            int trimCount = currentBufferSize - bufferMaxSize;
            for( int index = 0; index < trimCount; index++ )
            {
                buffer.remove( 0 );
            }
        }
    }

    /**
     * 返回一个字符串数组当前可见的行。返回的行数总是与屏幕尺寸相匹配
     *
     * @return  字符串数组表示当前数据在屏幕上。
     */
    public String[] getScreen()
    {
        String[] screen = new String[screenHeight];

        int currentBufferSize = buffer.size();

        if( currentBufferSize <= screenHeight )
        {
            int index;
            // Fill the screen array with the buffer.
            for( index = 0; index < currentBufferSize; index++ )
            {
                screen[index] = (String) buffer.get( index );
            }
            // Fill out any remaining rows.
            for( ; index < screenHeight; index++ )
            {
                screen[index] = "";
            }

            cursorRow = currentBufferSize - 1;
            cursorColumn = -1;
        }
        else
        {
            int bufferIndex = currentBufferSize - screenHeight;
            int screenIndex = 0;
            for( ; bufferIndex < currentBufferSize; bufferIndex++ )
            {
                screen[screenIndex++] = (String) buffer.get( bufferIndex );
            }

            cursorRow = screenHeight - 1;
            cursorColumn = -1;
        }

        return screen;
    }

    /**
     * 返回一个字符串数组的行整个缓冲区。
     *
     * @return 代表整个缓冲区的字符串数组。
     */
    public String[] getBuffer()
    {
        int currentBufferSize = buffer.size();
        String[] bufferArray = new String[currentBufferSize];

        for( int index = 0; index < currentBufferSize; index++ )
        {
            bufferArray[index] = (String) buffer.get( index );
        }

        return bufferArray;
    }

    /**
     * 成一个字符串处理传入的请求
     * @return
     */
    protected String process( char[] inputBuffer, int count )
    {
        return String.valueOf( inputBuffer, 0, count );
    }

    /**
     * 解析数据写入服务器控制字符。
     *
     * @param input 从客户端读取的数据。
     * @return 一个char数组写入服务器。
     */
    private char[] encodeOutput( String input )
    {
        char[] translateBuffer = new char[input.length()];
        int originalCount = input.length();
        int outputCount = 0;
        boolean ctrlPressed = false;

        for( int index = 0; index < originalCount; index++ )
        {
            // Check if the last key was a control key.
            if( ctrlPressed == true )
            {
                ctrlPressed = false;
                String shiftedKey = String.valueOf( input.charAt( index ) );
                shiftedKey = shiftedKey.toUpperCase();
                char newChar = (char) ( shiftedKey.charAt(0) - 64 );
                translateBuffer[outputCount++] = newChar;
            }
            // Encode control characters.
            else if( input.charAt( index ) == '#' )
            {
                // Make sure we have a full sequence.
                if( input.length() < (index + 3) )
                {
                    return new char[0];
                }

                try
                {
                    String charNumber = input.substring( index + 1, index + 3 );
                    int charValue = Integer.parseInt( charNumber, 16 );
                    if( charValue == -1 )
                    {
                        ctrlPressed = true;
                    }
                    index = index + 2;
                    translateBuffer[outputCount++] = (char) charValue;
                }
                catch( NumberFormatException numberFormatException )
                {
                    return new char[0];
                }
            }
            else
            {
                translateBuffer[outputCount++] = input.charAt( index );
            }
        }

        char[] outputBuffer = new char[outputCount];
        for( int index = 0; index < outputCount; index++ )
        {
            outputBuffer[index] = translateBuffer[index];
        }

        return outputBuffer;
    }
}
