package ren.doob.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.serivces.UserShellService;
import ren.doob.serivces.model.Shell;
import ren.doob.serivces.model.User;
import ren.doob.serivces.UserMapperService;
import ren.doob.util.sshwebproxy.MySsh;

import java.util.ArrayList;
import static ren.doob.common.Mc.*;
/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class SysLogin extends BaseController{

    private Log log = LogFactory.getLog(SysLogin.class);


    @Autowired
    private UserMapperService userMapperService;
    @Autowired
    private UserShellService userShellMapper;

    @ResponseBody
    @RequestMapping("/login")
    public Object login(){
        User user = userMapperService.getUser(Mc.getPara());
        System.out.println(user.getId());
        ArrayList<Shell> shellList = userShellMapper.getMyTerminal(Mc.putP("nowUserId" , user.getId().toString()));
        if(shellList.size() > 0){
            System.out.println("00");
            Shell shell = shellList.get(0);
            putP("ssh_ip",shell.getIp());
            putP("ssh_host",shell.getPort().toString());
            putP("ssh_name",shell.getName());
            putP("ssh_pass",shell.getPass());
            MySsh.connection();
        }

        if (Mc.getPara().get("pass").equals(user.getPass())){
            Mc.getSes().setAttribute(CommonField.SESSION_USERINFO,user);
            Mc.getSes().setAttribute(CommonField.NOWONLINE_USERNAME,user.getName());
            Mc.putR("userinfo" , user);
            log.info("用户"+user.getName()+"已经登陆！用户信息已存入session");
        } else {
            Mc.putR("result" , 0);
            log.warn("用户"+user.getName()+"登陆失败！" + "<<>>" +
            "登陆密码为："+Mc.getPara().get("pass")+ "--->正确密码为"+user.getPass());
        }
        return Mc.getR();
    }

    @ResponseBody
    @RequestMapping("/userinfo")
    public Object userinfo(){
        Mc.putR("userinfo" , getUserinfo());
        return Mc.getR();
    }
}
