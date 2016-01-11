package ren.doob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.doob.common.BaseController;
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
        if (p.getAccept().get("pass").equals(user.getPass())){
                session.setAttribute("nowUser",user);
            return "login";
        }
        return "error";
    }
}
