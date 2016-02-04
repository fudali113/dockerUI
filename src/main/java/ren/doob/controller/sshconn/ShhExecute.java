package ren.doob.controller.sshconn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import static ren.doob.common.CommonField.*;
import static ren.doob.common.Mc.*;
import ren.doob.common.*;
import ren.doob.common.sshwebproxy.ShellChannel;
import static ren.doob.common.sshwebproxy.MySsh.*;

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
    @RequestMapping(value = "/handle" ,method = RequestMethod.GET , produces="application/json;charset=UTF-8")
    public HashMap sshml(){
        String ssh_mingl = getPara().get("ssh_mingl");
        ShellChannel shellChannel = getShellChannel();
        shellChannel.write(ssh_mingl,true);
        shellChannel.read();
        Mc.putR(SSH_INFORMATION,shellChannel.getScreen());
        return Mc.getR();
    }


}
