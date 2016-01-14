package ren.doob.controller.sshconn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.ShellChannel;
import ren.doob.sshwebproxy.SshSession;

import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.controller.login
 * @class ShhExecute
 * @date 2016-1-14
 */

@Controller
public class ShhExecute extends SshBaseController {

    @ResponseBody
    @RequestMapping("/SSHcaozuo")
    public HashMap sshml(){
        this.localField();
        ShellChannel shellChannel = getShellChannel();
        result.put(CommonField.SSH_INFORMATION,shellChannel.getScreen());
        return result;
    }

    /**
     * 获取当前session中连接的shellChannel
     * 并返回包含运行命令后的信息的shellChannel
     *
     * @return
     */
    public ShellChannel getShellChannel(){

        String channelid = (String) session.getAttribute(CommonField.SESSION_CHANNELID);
        String connectionInfo = (String) session.getAttribute(CommonField.SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(session);
        ShellChannel shellChannel = sshSession.getSshConnection(connectionInfo).getShellChannel(channelid);
        shellChannel.write(ssh_mingl,true);
        shellChannel.read();

        return shellChannel;
    }

}
