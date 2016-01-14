package ren.doob.controller.login;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.ShellChannel;


import java.util.HashMap;
import java.util.Map;

/**
 * @author fudali
 * @package ren.doob.controller
 * @class SSHLogin
 * @date 2016-1-11
 */
@Controller
public class SSHLogin extends SshBaseController {

    @ResponseBody
    @RequestMapping("/SSHLogin")
    public HashMap SSHLogin(){
        ShellChannel shellChannel = connection();
        result.put(CommonField.SSH_INFORMATION, shellChannel.getScreen());
        return result;
    }
}
