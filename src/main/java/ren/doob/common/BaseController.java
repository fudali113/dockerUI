package ren.doob.common;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by fudali on 2015-12-21.
 */
public class BaseController {

    protected HashMap<String ,Object> result = new HashMap<String, Object>();
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected HttpSession session;

    @ModelAttribute
    public void init(HttpServletRequest request , HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        result.clear();
    }
}
