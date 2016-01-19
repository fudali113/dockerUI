package ren.doob.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.model.User;
import ren.doob.serivces.UserMapperService;

/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class SysLogin extends BaseController{

    @Autowired
    private UserMapperService userMapperService;

    @ResponseBody
    @RequestMapping("/login")
    public Object login(){
        User user = userMapperService.getUser(Mc.getPara());
        if (Mc.getPara().get("pass").equals(user.getPass())){
            Mc.getSes().setAttribute(CommonField.SESSION_USERINFO,user);
            Mc.putR("userinfo" , user);
        } else {
            Mc.putR("result" , 0);
        }
        return Mc.getR();
    }
}
