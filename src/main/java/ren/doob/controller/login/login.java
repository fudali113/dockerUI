package ren.doob.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.doob.common.BaseController;
import ren.doob.common.CommonField;
import ren.doob.common.Parameter;
import ren.doob.model.User;
import ren.doob.serivces.UserMapperService;

/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class login extends BaseController{

    @Autowired
    private UserMapperService userMapperService;

    @RequestMapping("/login")
    public String login(){
        User user = userMapperService.getUser(p);
        if (p.get("pass").equals(user.getPass())){
                session.setAttribute(CommonField.SESSION_USERINFO,user);
            return CommonField.PAGE_LOGIN;
        }
        return "error";
    }
}
