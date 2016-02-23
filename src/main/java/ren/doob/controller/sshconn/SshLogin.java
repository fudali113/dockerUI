package ren.doob.controller.sshconn;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;
import static ren.doob.common.Mc.*;
import static ren.doob.util.sshwebproxy.MySsh.*;

import ren.doob.common.Parameter;
import ren.doob.common.SshBaseController;
import ren.doob.serivces.UserShellService;
import ren.doob.serivces.model.Shell;
import ren.doob.serivces.model.User;
import ren.doob.util.sshwebproxy.*;

import java.util.ArrayList;
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
    @Autowired
    private UserShellService userShellService;


    @ResponseBody
    @RequestMapping("/connection")
    public HashMap SSHLogin(){
        ShellChannel shellChannel = connection();
        if(shellChannel != null){
            User userinfo = (User) Mc.getSes().getAttribute(CommonField.SESSION_USERINFO);
            ArrayList<Shell> shells = userShellService.getMyTerminal(Mc.putP("nowUserId", userinfo.getId().toString()));
            if (!chuckShell(shells, Mc.getPara())) {
                userShellService.addShellTerminal(Mc.getPara());
            }
            putR(CommonField.SSH_INFORMATION, shellChannel.getScreen());
            putR("loginInfo", getPara());
            log.info("用户" + getUserinfo().getName() + "登陆了shell终端");
            return Mc.getR();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/isCon")
    public Object isCon(){
        Parameter shellInfo = (Parameter) Mc.getSes().getAttribute("loginInfo");
        putR("loginInfo",shellInfo);
        return Mc.getR();
    }

    @ResponseBody
    @RequestMapping("/hostInfo")
    public Object hostInfo(){
        User userinfo = (User) Mc.getSes().getAttribute(CommonField.SESSION_USERINFO);
        ArrayList<Shell> shells = userShellService.getMyTerminal(Mc.putP("nowUserId", userinfo.getId().toString()));
        putR("hostInfo",shells);
        return Mc.getR();
    }

    private boolean chuckShell(ArrayList<Shell> shells, Parameter p){
        if (shells.size() == 0) return false;
        for(Shell s : shells){
            if(s.getIp().equals(p.get("ssh_ip")) && s.getPort().equals(p.get("ssh_host")) && s.getName().equals(p.get("ssh_name"))){
                return true;
            }
        }
        return false;
    }


}
