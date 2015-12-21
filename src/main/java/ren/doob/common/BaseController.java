package ren.doob.common;

import org.springframework.web.bind.annotation.ModelAttribute;
import ren.doob.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.serivces
 * @class UserMapperService
 * @date 2015-12-21
 */
public class BaseController {

    protected HashMap<String ,Object> result = new HashMap<String, Object>();
    protected Parameter p = new Parameter();
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected HttpSession session;

    /**
     * 利用@ModelAttribute在每个controller方法之前调用这个方法
     * 从而接收resuest，response等对象并操作
     * @param request
     * @param response
     */
    @ModelAttribute
    public void init(HttpServletRequest request , HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        //清空result
        result.clear();
        //清空parameter的手机参数map
        p.accept.clear();

        Enumeration en = request.getParameterNames();
        if(en != null){
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                p.accept.put(nms, request.getParameter(nms).trim());
            }
        }

    }
    //储存当前用户
    private User nowUser;
    protected User getNU(){
        if (nowUser == null) {
            nowUser = (User) session.getAttribute("nowUser");
        }
        return nowUser;
    }

}
