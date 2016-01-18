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

    protected HashMap<String ,Object> result = new HashMap<String, Object>();//返回数据储存map
    protected Parameter p ;
    //储存当前用户
    protected User userinfo;
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
        p = new Parameter();
        //清空result
        result.clear();
        //清空parameter的存放参数map
        p.clear();

        this.request = request ;
        this.response = response ;
        this.session = request.getSession() ;

        ReqAndResContext.setRequest(request);
        ReqAndResContext.setResponse(response);

        Enumeration en = request.getParameterNames();
        if(en != null){
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                p.put(nms, request.getParameter(nms).trim());
            }
        }

    }
    protected User getNU(){
        if (userinfo == null) {
            userinfo = (User) session.getAttribute(CommonField.SESSION_USERINFO);
        }
        return userinfo;
    }

}
