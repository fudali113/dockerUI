package ren.doob.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author fudali
 * @package ren.doob.common.aop
 * @class LoginRights
 * @date 2016-1-14
 */

@Aspect
public class LoginRights {

    @Around("execution(* ren.doob.controller..*.*(..))")
    public Object loginRights(ProceedingJoinPoint jp) throws IOException {

        Object result = null ;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        if ( session.getAttribute(CommonField.SESSION_USERINFO) != null) {
            try {
                result = jp.proceed();
                return result;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        HttpServletResponse response = Mc.getRes();
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().append("{\"noLogin\":\"1\"}");
        return result ;

    }

}
