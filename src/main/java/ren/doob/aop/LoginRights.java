package ren.doob.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ren.doob.common.CommonField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.aop
 * @class LoginRights
 * @date 2016-1-14
 */

@Aspect
public class LoginRights {

    @Around("execution(* ren.doob.controller..*.*(..))")
    public Object loginRights(ProceedingJoinPoint jp){

        Object result = null ;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        if ( session.getAttribute(CommonField.SESSION_USERINFO) != null) {
            try {
                result = jp.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }else {

            result = new HashMap().put("loginRightsChucke" , 0);
            System.out.println("weidenglu");
        }
        return result ;

    }

}
