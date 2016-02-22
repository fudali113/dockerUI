package ren.doob.controller.sshconn;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;
import static ren.doob.common.Mc.*;
import ren.doob.common.SshBaseController;
import ren.doob.util.sshwebproxy.*;

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

    private Log log = LogFactory.getLog(FileExecute.class);


    @ResponseBody
    @RequestMapping("/connection")
    public HashMap SSHLogin(){
        this.localField();
        ShellChannel shellChannel = connection();
        putR(CommonField.SSH_INFORMATION, shellChannel.getScreen());
        putR("loginInfo" , getPara());
        log.info("用户"+getUserinfo().getName()+"登陆了shell终端");
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
            sshConnection = new SshConnection(getPara().get("ssh_ip"),Integer.parseInt(getPara().get("ssh_host")),getPara().get("ssh_name"),getPara().get("ssh_pass"));
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
