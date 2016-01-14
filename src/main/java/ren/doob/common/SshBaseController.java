package ren.doob.common;

import ren.doob.sshwebproxy.ShellChannel;
import ren.doob.sshwebproxy.SshConnectException;
import ren.doob.sshwebproxy.SshConnection;
import ren.doob.sshwebproxy.SshSession;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fudali
 * @package ren.doob.common
 * @class SshBaseController
 * @date 2016-1-14
 */

public class SshBaseController extends BaseController{

    protected String ssh_ip;
    protected String ssh_host;
    protected String ssh_name;
    protected String ssh_pass;
    protected String ssh_mingl;

    /**
     * 根据连接信息连接ssh
     * 并返回带有连接信息的已read的shellChannel对象
     *
     * @return 返回连接后可操作的一个shell
     */
    public  ShellChannel connection(){

        ShellChannel shellChannel = null;
        SshConnection sshConnection = null;

        try{
            SshSession ss = new SshSession(session);
            sshConnection = new SshConnection(ssh_ip,Integer.parseInt(ssh_host),ssh_name,ssh_pass);
            ss.addSshConnection(sshConnection);
            shellChannel = sshConnection.openShellChannel();

        }catch (SshConnectException sce){
            sce.printStackTrace();
        }

        if (shellChannel != null && sshConnection != null) {
            session.setAttribute(CommonField.SESSION_CHANNELID , shellChannel.getChannelId());
            session.setAttribute(CommonField.SESSION_CONNECTIONINFO , sshConnection.getConnectionInfo());
            shellChannel.read();
        }

        return shellChannel;
    }


    /**
     * 获取当前session中连接的shellChannel
     * 并返回包含运行命令后的信息的shellChannel
     *
     * @return
     */
    public ShellChannel getShellChannel(){

        String channelid = (String) session.getAttribute("channelid");
        String connectionInfo = (String) session.getAttribute("ConnectionInfo");

        SshSession sshSession = new SshSession(session);
        ShellChannel shellChannel = sshSession.getSshConnection(connectionInfo).getShellChannel(channelid);
        shellChannel.write(ssh_mingl,true);
        shellChannel.read();

        return shellChannel;
    }

    public String getSsh_pass() {
        return ssh_pass;
    }

    public void setSsh_pass(String ssh_pass) {
        this.ssh_pass = ssh_pass;
    }

    public String getSsh_name() {

        return ssh_name;
    }

    public void setSsh_name(String ssh_name) {
        this.ssh_name = ssh_name;
    }

    public String getSsh_ip() {

        return ssh_ip;
    }

    public void setSsh_ip(String ssh_ip) {
        this.ssh_ip = ssh_ip;
    }

    public String getSsh_host() {

        return ssh_host;
    }

    public void setSsh_host(String ssh_host) {
        this.ssh_host = ssh_host;
    }

    public String getSsh_mingl() {
        return ssh_mingl;
    }

    public void setSsh_mingl(String ssh_mingl) {
        this.ssh_mingl = ssh_mingl;
    }
}
