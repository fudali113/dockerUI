<%--<%@ page import="org.apache.commons.logging.Log,
                 org.apache.commons.logging.LogFactory,
                 com.ericdaugherty.sshwebproxy.*,
                 java.util.List,
                 com.sshtools.j2ssh.sftp.SftpFile"%>
<%@ include file="nocache.jsp" %>
<%@ include file="security.jsp" %>

&lt;%&ndash;
   | File Window.  Performs all UI display for a FileChannel
&ndash;%&gt;

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>

<head>
    <title>SshWebProxy - File</title>
    <link type="text/css" href="sshwebproxy.css" rel="stylesheet">
</head>

<%
    Log log = LogFactory.getLog( "com.ericdaugherty.sshwebproxy.jsp.file-jsp" );

    String connectionInfo = request.getParameter( SshConstants.PARAMETER_CONNECTION );
    String channelId = request.getParameter( SshConstants.PARAMETER_CHANNEL );

    log.debug( "Displaying file page for: " + connectionInfo + " " + channelId );

    sshSession = new SshSession( session );

    log.debug( "Getting Connection and Channel" );
    SshConnection sshConnection = sshSession.getSshConnection( connectionInfo );
    FileChannel fileChannel = null;
    boolean valid = false;
    if( sshConnection != null )
    {
        fileChannel = sshConnection.getFileChannel( channelId );
        if( fileChannel != null )
        {
            valid = true;
        }
    }
%>

<body>

<jsp:include page="displayerror.jsp" />

<p class="links">
<a href="<%=SshConstants.PAGE_HOME%>">Home</a>
</p>

<%
if( !valid )
{
%>
Invalid Channel
<%
}
else
{
%>

Current Directory:
<br/>
<%= fileChannel.encodeHTML( fileChannel.getCurrentDirectory() ) %>

<p/>
Click on a file to download.  Click on a directory to navigate.<br/>
Current Directory Listing:
<br/>
<p class="code">
<%
    List directoryListing = fileChannel.getCurrentDirectoryListing();
    if( directoryListing == null )
    {
%>
Error Getting Directory Listing!
<%
    }
    else
    {
%>
<table>
<%
        SftpFile file;
        for( int index = 0; index < directoryListing.size(); index++ )
        {
            file = (SftpFile) directoryListing.get( index );

            // Handle it as a directory
            if( file.isDirectory() )
            {
%>
    <tr>
        <td><a href="<%=SshConstants.SERVLET_FILE%>?<%=SshConstants.PARAMETER_ACTION%>=<%=SshConstants.ACTION_CHANGE_DIRECTORY%>&<%=SshConstants.PARAMETER_CONNECTION%>=<%=connectionInfo%>&<%=SshConstants.PARAMETER_CHANNEL%>=<%=channelId%>&<%=SshConstants.PARAMETER_DIRECTORY%>=<%=file.getFilename()%>"><%= file.getFilename() %></a></td>
        <td><%= fileChannel.encodeHTML( file.getLongname() )%></td>
    </tr>
<%
            }
            // Handle it as a file.
            else
            {
%>
    <tr>
        <td><a href="<%=SshConstants.SERVLET_FILE%>/<%=file.getFilename()%>?<%=SshConstants.PARAMETER_ACTION%>=<%=SshConstants.ACTION_DOWNLOAD%>&<%=SshConstants.PARAMETER_CONNECTION%>=<%=connectionInfo%>&<%=SshConstants.PARAMETER_CHANNEL%>=<%=channelId%>&<%=SshConstants.PARAMETER_FILENAME%>=<%=file.getFilename()%>"><%= file.getFilename() %></a></td>
        <td><%= fileChannel.encodeHTML( file.getLongname() ) %></td>
    </tr>
                <%
            }
        }
    }
%>
</table>
</p>

Upload a file to this directory:<br/>
<form ENCTYPE="multipart/form-data" name="uploadFile" method="post" action="<%=SshConstants.SERVLET_FILE%>">
    <input type="hidden" name="<%=SshConstants.PARAMETER_ACTION%>" value="<%=SshConstants.ACTION_UPLOAD%>" />
    <input type="hidden" name="<%=SshConstants.PARAMETER_CONNECTION%>" value="<%=connectionInfo%>" />
    <input type="hidden" name="<%=SshConstants.PARAMETER_CHANNEL%>" value="<%=channelId%>" />
    Remote Name: <input type="text" name="<%=SshConstants.PARAMETER_FILENAME%>" />
    <br/>
    File to Upload: <input type="file" name="<%=SshConstants.PARAMETER_FILE%>" />
    <input type="submit" value="Upload" />
</form>


<%
}
%>

</body>

</html>--%>
