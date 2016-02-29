package ren.doob.controller.login;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;
import static ren.doob.common.Mc.*;
import static ren.doob.util.sshwebproxy.MySsh.*;
import ren.doob.common.Parameter;
import ren.doob.common.SshBaseController;
import ren.doob.controller.login.FileExecute;
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
        User user = getUserinfo();
        Mc.putP("nowUserId" , user.getId().toString());
        ShellChannel shellChannel = connection();
        if(shellChannel != null){
            ArrayList<Shell> shells = (ArrayList<Shell>) Mc.getSes().getAttribute(CommonField.SESSION_SHELLS);
            if ( chuckShell( shells , Mc.getPara() ) == 0 ) {
                userShellService.addShellTerminal(getPara());
            }
            //判断刚加入的shell登陆，并将次shell的数据库id返回添加到session中以方便使用
            shells = userShellService.getMyTerminal(getPara());
            int onlineShellId = chuckShell(shells , Mc.getPara());
            if(onlineShellId != 0){
                getSes().setAttribute(CommonField.SESSION_SHELLS , shells);
                getSes().setAttribute(CommonField.SESSION_SHELLID , onlineShellId);
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
        Parameter shellInfo = (Parameter) getSes().getAttribute("loginInfo");
        putR("loginInfo",shellInfo);
        return Mc.getR();
    }

    @ResponseBody
    @RequestMapping("/hostInfo")
    public Object hostInfo(){
        ArrayList<Shell> shells = (ArrayList<Shell>) getSes().getAttribute(CommonField.SESSION_SHELLS);
        putR("hostInfo",shells);
        putR("onlineShell" ,getSes().getAttribute(CommonField.SESSION_SHELLID));
        return Mc.getR();
    }

    @ResponseBody
    @RequestMapping("/recon/{NO}")
    public Object relogin(@PathVariable("NO") int NO ) throws SshConnectException {
        ArrayList<Shell> shells = (ArrayList<Shell>) Mc.getSes().getAttribute(CommonField.SESSION_SHELLS);
        reconnection(shells,NO);
        putR("recon" , 1);
        getSes().setAttribute(CommonField.SESSION_SHELLID,NO);
        return Mc.getR();
    }

    private int chuckShell(ArrayList<Shell> shells, Parameter p){
        if (shells==null || shells.size() == 0) return 0;
        for(Shell s : shells){
            if(s.getIp().equals(p.get("ssh_ip")) && s.getPort().toString().equals(p.get("ssh_host")) && s.getName().equals(p.get("ssh_name"))){
                return s.getId();
            }
        }
        return 0;
    }


}
