package ren.doob.controller.login;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.ShellChannel;
import ren.doob.sshwebproxy.SshConnectException;
import ren.doob.sshwebproxy.SshConnection;
import ren.doob.sshwebproxy.SshSession;

import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.controller
 * @class SSHLogin
 * @date 2016-1-11
 */
@Controller
public class SshLogin extends SshBaseController {

    @ResponseBody
    @RequestMapping("/SSHLogin")
    public HashMap SSHLogin(){
        this.localField();
        ShellChannel shellChannel = connection();
        result.put(CommonField.SSH_INFORMATION, shellChannel.getScreen());
        return result;
    }

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
}
