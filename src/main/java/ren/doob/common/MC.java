package ren.doob.common;

import ren.doob.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.common
 * @class Mc --> MyContext
 * @date 2016-1-19
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */

public class Mc {

    /**
     * 使用ThreadLocal保证线程安全
     */
    public static ThreadLocal<HashMap<String ,Object>> resultThreadLocal = new ThreadLocal<HashMap<String, Object>>();
    public static ThreadLocal<Parameter> parameterThreadLocal = new ThreadLocal<Parameter>();
    public static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    public static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();
    public static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
    public static ThreadLocal<HttpSession> sessionThreadLocal = new ThreadLocal<HttpSession>();


    public static HashMap<String ,Object> getR(){
        return resultThreadLocal.get();
    }

    public static void putR(String string , Object object){
        HashMap<String ,Object> hashMap = resultThreadLocal.get();
        hashMap.put(string , object);
        resultThreadLocal.set(hashMap);
    }

    public static Parameter getPara(){
        return parameterThreadLocal.get();
    }

    public static HttpServletResponse getRes(){
        return responseThreadLocal.get();
    }

    public static HttpServletRequest getReq(){
        return requestThreadLocal.get();
    }

    public static HttpSession getSes(){
        return sessionThreadLocal.get();
    }



}
