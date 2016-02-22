package ren.doob.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.serivces.model.User;
import ren.doob.serivces.UserMapperService;

/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class SysLogin extends BaseController{

    private Log log = LogFactory.getLog(SysLogin.class);


    @Autowired
    private UserMapperService userMapperService;

    @ResponseBody
    @RequestMapping("/login")
    public Object login(){
        User user = userMapperService.getUser(Mc.getPara());
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
