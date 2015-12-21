package ren.doob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.doob.common.BaseController;

/**
 * Created by Administrator on 2015-12-21.
 */

@Controller
public class login extends BaseController{

    @RequestMapping("/login")
    public String login(){
        String pass = request.getParameter("pass");
        String name = request.getParameter("name");
        if (pass.equals("123456") && name.equals("fudali")){
            return "login";
        }
        return "error";
    }
}
