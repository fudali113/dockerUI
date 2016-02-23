package ren.doob.controller.sshconn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static ren.doob.common.CommonField.*;
import static ren.doob.common.Mc.*;
import ren.doob.common.*;
import ren.doob.util.sshwebproxy.ShellChannel;
import static ren.doob.util.sshwebproxy.MySsh.*;

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

    private Log log = LogFactory.getLog(FileExecute.class);


    @ResponseBody
    @RequestMapping(value = "/handle", produces="application/json;charset=UTF-8")
    public HashMap sshml(){
        String ssh_mingl = getPara().get("ssh_mingl");
        ShellChannel shellChannel = getShellChannel();
        if(!"".equals(ssh_mingl.trim())) {
            shellChannel.write(ssh_mingl,true);
        }
        shellChannel.read();

        log.info("用户"+getUserinfo().getName()+"进行了操作："+ ssh_mingl);

        Mc.putR(SSH_INFORMATION,shellChannel.getScreen());
        return Mc.getR();
    }


}
