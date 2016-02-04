package ren.doob.common;

import org.springframework.web.bind.annotation.ModelAttribute;
import ren.doob.serivces.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.serivces
 * @class UserMapperService
 * @date 2015-12-21
*/
public class BaseController {



    /**
     * 利用@ModelAttribute在每个controller方法之前调用这个方法
     * 从而接收resuest，response等对象并操作
     * @param request
     * @param response
     */
    @ModelAttribute
    public void init(HttpServletRequest request , HttpServletResponse response){
        Parameter p = new Parameter();

        Mc.resultThreadLocal.set(new HashMap<String, Object>());
        Mc.requestThreadLocal.set(request);
        Mc.responseThreadLocal.set(response);
        Mc.sessionThreadLocal.set(request.getSession());

        Enumeration en = request.getParameterNames();
        if(en != null){
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                p.put(nms, request.getParameter(nms).trim());
            }
        }

        Mc.parameterThreadLocal.set(p);

    }

    /**
     * 从ThreadLocal中获取当前线程的userinfo，方便子类调用
     * @return
     */
    protected User getUserinfo(){
        if (Mc.userThreadLocal.get() == null) {
            User user = (User) Mc.sessionThreadLocal.get().getAttribute(CommonField.SESSION_USERINFO);
            Mc.userThreadLocal.set(user);
        }
        return Mc.userThreadLocal.get();
    }


}
