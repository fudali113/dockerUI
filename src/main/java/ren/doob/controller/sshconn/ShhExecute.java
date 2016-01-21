package ren.doob.controller.sshconn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static ren.doob.common.CommonField.*;
import static ren.doob.common.Mc.*;
import ren.doob.common.*;
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
@RequestMapping("/ssh/shell")
public class ShhExecute extends SshBaseController {

    @ResponseBody
    @RequestMapping("/handle")
    public HashMap sshml(){
        this.localField();
        ShellChannel shellChannel = getShellChannel();
        Mc.putR(SSH_INFORMATION,shellChannel.getScreen());
        return Mc.getR();
    }

    /**
     * 获取当前session中连接的shellChannel
     * 并返回包含运行命令后的信息的shellChannel
     *
     * @return
     */
    public ShellChannel getShellChannel(){

        String channelid = (String) Mc.getSes().getAttribute(SESSION_SHELLCHANNELID);
        String connectionInfo = (String) Mc.getSes().getAttribute(SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(Mc.getSes());
        ShellChannel shellChannel = sshSession.getSshConnection(connectionInfo).getShellChannel(channelid);
        shellChannel.write(ssh_mingl,true);
        shellChannel.read();

        return shellChannel;
    }

}
