package ren.doob.controller.sshconn;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.*;

import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.controller
 * @class SSHLogin
 * @date 2016-1-11
 */
@Controller
@RequestMapping("/ssh")
public class SshLogin extends SshBaseController {

    @ResponseBody
    @RequestMapping("/connection")
    public HashMap SSHLogin(){
        this.localField();
        ShellChannel shellChannel = connection();
        Mc.putR(CommonField.SSH_INFORMATION, shellChannel.getScreen());
        return Mc.getR();
    }

    /**
     * 根据连接信息连接ssh
     * 并返回带有连接信息的已read的shellChannel对象
     *
     * @return 返回连接后可操作的一个shell
     */
    public  ShellChannel connection(){

        ShellChannel shellChannel = null;
        FileChannel fileChannel = null;
        SshConnection sshConnection = null;

        try{
            SshSession ss = new SshSession(Mc.getSes());
            sshConnection = new SshConnection(ssh_ip,Integer.parseInt(ssh_host),ssh_name,ssh_pass);
            ss.addSshConnection(sshConnection);
            shellChannel = sshConnection.openShellChannel();
            fileChannel = sshConnection.openFileChannel();

        }catch (SshConnectException sce){
            sce.printStackTrace();
        }

        if (shellChannel != null && sshConnection != null && fileChannel != null) {
            Mc.getSes().setAttribute(CommonField.SESSION_SHELLCHANNELID , shellChannel.getChannelId());
            Mc.getSes().setAttribute(CommonField.SESSION_CONNECTIONINFO , sshConnection.getConnectionInfo());
            Mc.getSes().setAttribute(CommonField.SESSION_FILECHANNELID , fileChannel.getChannelId());
            shellChannel.read();
        }

        return shellChannel;
    }
}