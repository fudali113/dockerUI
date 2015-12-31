
package ren.doob.sshwebproxy;

public interface SshConstants {

    //***************************************************************
    // Properties
    //***************************************************************

    /** Filename of the properties file used to store user information */
    static final String PROPERTIES_FILENAME = "sshwebproxy.properties";

    /** The header to add to the properties file when saving. */
    static final String PROPERTIES_HEADER =
            "# Defines users and passwords for the SSHWebProxy system.\r\n" +
            "# To add a user, add an entry username=password.\r\n" +
            "# The password will be encrypted the first time the user\r\n" +
            "# logs in.\r\n" +
            "#\r\n" +
            "# This file will be reloaded automaticlly after changes, so a\r\n" +
            "# user can be edited or added without restaring or redeploying.";

    /** Indicates restricted mode host to connect to. */
    static final String PROPERTIES_RESTRICTED = "sshwebproxy.restricted";

    //***************************************************************
    // Page URLs.
    //***************************************************************

    /** Home page for the application */
    static final String PAGE_HOME = "index.jsp";

    /** Login page for the application */
    static final String PAGE_LOGIN = "login.jsp";

    /** Shell Channel page */
    static final String PAGE_SHELL_HOME = "shell.jsp";

    /** File Channel page */
    static final String PAGE_FILE_HOME = "file.jsp";

    //***************************************************************
    // Servlet URLs.
    //***************************************************************

    /** The Admin Servlet, used to handle User and Config setup */
    static final String SERVLET_ADMIN = "admin";

    /** The SshConnectionServlet */
    static final String SERVLET_CONNECTION = "connection";

    /** The SshShellServlet */
    static final String SERVLET_SHELL = "shell";

    /** The SshFileServlet */
    static final String SERVLET_FILE = "file";

    //***************************************************************
    // Parameter Name Constants
    //***************************************************************

    /** The action to perform */
    static final String PARAMETER_ACTION = "action";

    /** The unique connection info */
    static final String PARAMETER_CONNECTION = "connection";

    /** The Channel to open after connection is established. */
    static final String PARAMETER_CHANNEL_TYPE = "channelType";

    /** The unique channel id for a given connection */
    static final String PARAMETER_CHANNEL = "channel";

    /** The host to connect to */
    static final String PARAMETER_HOST = "host";

    /** The port to connect to */
    static final String PARAMETER_PORT = "port";

    /** The username to use to connect */
    static final String PARAMETER_USERNAME = "username";

    /** The Type of authentication to use (radio button) */
    static final String PARAMETER_AUTHENTICATION_TYPE = "authenticationType";

    /** The password to use to connect */
    static final String PARAMETER_PASSWORD = "password";

    /** The SSH Key to use to authenticate. */
    static final String PARAMETER_KEY_FILE = "keyfile";

    /** The passphrase for the key */
    static final String PARAMETER_KEY_PASSWORD = "keypassword";

    /** The data to write to the channel */
    static final String PARAMETER_DATA = "data";

    /** The name of the file to work with. */
    static final String PARAMETER_FILENAME = "filename";

    /** The uploaded file. */
    static final String PARAMETER_FILE = "file";

    /** The name of the file to work with. */
    static final String PARAMETER_DIRECTORY = "directory";

    //***************************************************************
    // Action Constants
    //***************************************************************

    // Admin Servlet Actions

    /** A user request to login */
    static final String ACTION_LOGIN = "login";

    // SshConnectionServlet and common Actions

    /** Open a new SSH Connection */
    static final String ACTION_OPEN_CONNECTION = "openConnection";

    /** Close an existing SSH Connection */
    static final String ACTION_CLOSE_CONNECTION = "closeConnection";

    /** Open a new Shell Channel on an existing Connection */
    static final String ACTION_OPEN_SHELL_CHANNEL = "openShell";

    /** Open a new File Channel on an existing Connection */
    static final String ACTION_OPEN_FILE_CHANNEL = "openFile";

    /** Close an SshChannel */
    static final String ACTION_CLOSE_CHANNEL = "closeChannel";

    // SshShellServlet Actions

    /** Write to the Channel */
    static final String ACTION_WRITE = "write";

    // SshFileServlet Actions

    /** Download a file */
    static final String ACTION_DOWNLOAD = "download";

    /** Upload a file */
    static final String ACTION_UPLOAD = "upload";

    /** Change to a different directory */
    static final String ACTION_CHANGE_DIRECTORY = "changeDirectory";

    //***************************************************************
    // Channel Types
    //***************************************************************

    /** Do not open any channel after connection */
    static final String CHANNEL_TYPE_NONE = "None";

    /** ShellChannel */
    static final String CHANNEL_TYPE_SHELL = "Shell";

    /** FileChannel */
    static final String CHANNEL_TYPE_FILE = "File";

    //***************************************************************
    // Authentication Types
    //***************************************************************

    static final String AUTHENTICATION_TYPE_PASSWORD = "passwordauthentication";

    static final String AUTHENTICATION_TYPE_KEY = "keyauthentication";


}
