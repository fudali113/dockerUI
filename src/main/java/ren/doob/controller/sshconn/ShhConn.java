package ren.doob.controller.sshconn;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.ShellChannel;

import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.controller.login
 * @class ShhConn
 * @date 2016-1-14
 */

public class ShhConn extends SshBaseController {

    @ResponseBody
    @RequestMapping("/SSHcaozuo")
    public HashMap SSHml(){
        ShellChannel shellChannel = getShellChannel();
        result.put(CommonField.SSH_INFORMATION,shellChannel.getScreen());
        return result;
    }

}
