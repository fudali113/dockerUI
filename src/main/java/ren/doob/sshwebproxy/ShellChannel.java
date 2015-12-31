
package ren.doob.sshwebproxy;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.io.*;

import com.sshtools.j2ssh.session.SessionChannelClient;

public class ShellChannel extends SshChannel implements SshConstants {

    //***************************************************************
    // Variables
    //***************************************************************

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

    /** Logger */
    private static final Log log = LogFactory.getLog( ShellChannel.class );

    //***************************************************************
    // Constructor
    //***************************************************************

    /**
     * Opens a vt100 terminal session transfer session with the server.
     *
     * @param sshConnection the connection to use.
     * @param sshChannel the SSH API channel.
     * @throws SshConnectException thrown if there is any error opening
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
                log.warn( "ShellChannel constructor failed, unable to open PseudoTerminal for connection: " + sshConnection.getConnectionInfo() );
                throw new SshConnectException( "Unable to establish PseudoTerminal for new ShellChannel." );
            }
            else if( !sshChannel.startShell() )
            {
                log.warn( "ShellChannel constructor failed, unable to start shell on new channel for connection: " + sshConnection.getConnectionInfo() );
                throw new SshConnectException( "Unable to start Shell for new ShellChannel." );
            }

            writer = new PrintWriter( new OutputStreamWriter( sshChannel.getOutputStream() ) );
            reader = new BufferedReader( new InputStreamReader( sshChannel.getInputStream() ) );

            buffer = new ArrayList( bufferMaxSize );
        }
        catch( IOException ioException )
        {
            log.warn( "ShellChannel constructor failed, IOException occured while setting up channel  for connection: " + sshConnection.getConnectionInfo() + ". IOException: " + ioException, ioException );
            throw new SshConnectException( "Unable to establish Shell Connection.  IOExeption occured: " + ioException );
        }
    }

    //***************************************************************
    // SshChannel Methods
    //***************************************************************

    /**
     * Closes the Reader and Writer after the Channel has been closed.
     * This should only be called by the SshConnection
     * class and never directly called from this class.
     */
    public void close()
    {
        // Close Readers and Writers.
        if( log.isInfoEnabled() ) log.debug( "Closing ShellChannel connected to: " + sshConnection.getConnectionInfo() );

        if( reader != null )
        {
            try
            {
                reader.close();
            }
            catch (IOException ioException)
            {
                log.warn( "Error closing BufferedReader for Shell Connection to: " + sshConnection.getConnectionInfo() + ".  IOException: " + ioException );
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
                log.warn( "Error closing SessionChannelClient  for Shell Connection to: " + sshConnection.getConnectionInfo() + ".  IOException: " + ioException );
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

    //***************************************************************
    // Public Parameter Access
    //***************************************************************


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

    //***************************************************************
    // Public Data Manipulation
    //***************************************************************

    /**
     * Performs a read of the input data and fills the buffer.
     * This should be called before getScreen or getBuffer.
     */
    public void read()
    {
        if( log.isDebugEnabled() ) log.debug( "read called for ShellConnection to: " + sshConnection.getConnectionInfo() );

        // We want to read even if the channel has been closed, because
        // the BufferedReader may have buffered some input, so do the
        // check after this read.  But if the reader is null, just
        // ignore the call.
        if( reader == null )
        {
            log.warn( "read called on null reader.  Ignoring." );
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
                log.warn( "Read Pause interrupted in read()." );
            }

            // If there is data ready, go ahead and read it.
            if( reader.ready() )
            {
                // read the data and run it through the processor.
                int count = reader.read( inputBuffer );
                input = process( inputBuffer, count );
                if( log.isDebugEnabled() ) log.debug( "Read " + count + " characters from server." );

                fillBuffer( input );

                // Check to see if the channel was closed.
                if( !isConnected() )
                {
                    if( !reader.ready() )
                    {
                        if( log.isDebugEnabled() ) log.debug( "ShellChannel for connecton: " + sshConnection.getConnectionInfo() + " Closed, closing streams." );

                        // Notify the sshConnection that this channel is closed.
                        sshConnection.closeChannel( this );
                    }
                    else
                    {
                        if( log.isDebugEnabled() ) log.debug( "Connection Closed but there is more data to be read." );
                    }
                }
            }
            else
            {
                log.debug( "ShellChannel for connection: " + sshConnection.getConnectionInfo() + " has no data to read." );
            }
        }
        catch( IOException ioException )
        {
            log.error( "Error reading ShellChannel for connection: " + sshConnection.getConnectionInfo() + ".  IOException while in read(): " + ioException, ioException );
        }
    }

    /**
     * Writes the data to the SSH server and sends a newline charecter
     * "\n" if the sendNewLine boolean is true.
     *
     * @param data the data to write.
     * @param sendNewLine true if a newline should be sent.
     */
    public void write( String data, boolean sendNewLine )
    {
        // Don't write if the channel is closed.
        if( !isConnected() )
        {
            log.info( "Write call on closed ShellChannel for connection: " + sshConnection.getConnectionInfo() + ".  Ignoring." );
            return;
        }

        // Verify the writer is not null.
        if( writer == null )
        {
            log.info( "Write call on closed ShellChannel Writer for connection: " + sshConnection.getConnectionInfo() + ".  Ignoring." );
            return;
        }

        // Encode the data for output.  Convert any control characters to
        // the correct char value.
        char[] output = encodeOutput( data );

        if( log.isDebugEnabled() ) log.debug( "Wrote " + output.length + " characters to ShellChannel for connection: " + sshConnection.getConnectionInfo() );

        // Write the output, and send a new line if requested.
        writer.print( output );
        if( sendNewLine )
        {
            writer.print( "\n" );
        }
        writer.flush();
    }

    /**
     * Adds the data that was read to the buffer.
     *
     * @param input the processed data read from the server
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
            if( log.isDebugEnabled() ) log.debug( "Removing " + trimCount + " rows from the buffer." );
            for( int index = 0; index < trimCount; index++ )
            {
                buffer.remove( 0 );
            }
        }
    }

    /**
     * Returns a String array of the currently visible
     * rows.  The number of rows returned will always match
     * the Screen Size.
     *
     * @return Array of Strings that represent the current data on the screen.
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
     * Returns a String array of the entire buffer.
     * rows.
     *
     * @return Array of Strings that represent the entire buffer.
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
     * Process the incoming request into a string.
     * @return
     */
    protected String process( char[] inputBuffer, int count )
    {
        return String.valueOf( inputBuffer, 0, count );
    }

    /**
     * Parse the data to write to the server for control characters.
     *
     * @param input the data read from the client.
     * @return a char array to write to the server.
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
                    log.error( "Invalid input data.  Failed encoding.  There must be 2 characters after the # character." );
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
                    if( log.isDebugEnabled() ) log.debug( "Encoded #" + charNumber + " to decimal: " + charValue );
                    index = index + 2;
                    translateBuffer[outputCount++] = (char) charValue;
                }
                catch( NumberFormatException numberFormatException )
                {
                    log.error( "Invalid input data.  failed encoding.  The control character did not contain a valid hex value." );
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
