package ren.doob.controller.sshconn;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;
import static ren.doob.common.Mc.*;
import static ren.doob.util.sshwebproxy.MySsh.*;
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
        ShellChannel shellChannel = connection();
        putR(CommonField.SSH_INFORMATION, shellChannel.getScreen());
        putR("loginInfo" , getPara());
        log.info("用户"+getUserinfo().getName()+"登陆了shell终端");
        return Mc.getR();
    }


}
