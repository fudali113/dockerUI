package ren.doob.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.serivces.UserShellService;
import ren.doob.serivces.model.Shell;
import ren.doob.serivces.model.User;
import ren.doob.serivces.UserMapperService;
import ren.doob.util.sshwebproxy.MySsh;
import ren.doob.util.sshwebproxy.SshConnectException;

import java.io.IOException;
import java.util.ArrayList;
import static ren.doob.common.Mc.*;
/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class SysLogin extends BaseController{

    private static Logger log = Logger.getLogger(SysLogin.class);


    @Autowired
    private UserMapperService userMapperService;
    @Autowired
    private UserShellService userShellService;

    @ResponseBody
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public Object login() throws SshConnectException {
        User user = userMapperService.getUser(Mc.getPara());
        ArrayList<Shell> shells = userShellService.getMyTerminal(Mc.putP("nowUserId" , user.getId().toString()));
        if(shells.size() > 0){
            MySsh.reconnection(shells,shells.get(0).getId());
            Mc.getSes().setAttribute(CommonField.SESSION_SHELLS , shells);
            Mc.getSes().setAttribute(CommonField.SESSION_SHELLID , shells.get(0).getId());
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

    @RequestMapping("/out")
    public void out() throws IOException {
        getSes().invalidate();
        getRes().sendRedirect("/doob/");
    }

    @ResponseBody
    @RequestMapping(value = "/signup" ,method = RequestMethod.POST)
    public Object signin(){
        int insert = userMapperService.signin(Mc.getPara());
        putR("signup" , insert);
        return Mc.getR();
    }

    @ResponseBody
    @RequestMapping("/userinfo")
    public Object userinfo(){
        Mc.putR("userinfo" , getUserinfo());
        return Mc.getR();
    }
}
