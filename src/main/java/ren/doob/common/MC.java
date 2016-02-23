package ren.doob.common;

import ren.doob.serivces.model.User;

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


    /**
     * 快速取值
     * @return
     */
    public static HashMap<String ,Object> getR(){
        return resultThreadLocal.get();
    }

    /**
     * 直接向resultThreadLocal中的HashMap进行put操作
     * @param string
     * @param object
     * @return 返回put操作之后的HashMap
     */
    public static HashMap<String ,Object> putR(String string , Object object){
        HashMap<String ,Object> hashMap = resultThreadLocal.get();
        hashMap.put(string , object);
        resultThreadLocal.set(hashMap);
        return hashMap;
    }

    /**
     * 快速取值
     * @return
     */
    public static Parameter getPara(){
        return parameterThreadLocal.get();
    }
    public static Parameter putP(String s,String o){
        Parameter p = Mc.getPara();
        p.put(s , o);
        parameterThreadLocal.set(p);
        return p;
    }
    /**
     * 快速取值
     * @return
     */
    public static HttpServletResponse getRes(){
        return responseThreadLocal.get();
    }

    /**
     * 快速取值
     * @return
     */
    public static HttpServletRequest getReq(){
        return requestThreadLocal.get();
    }

    /**
     * 快速取值
     * @return
     */
    public static HttpSession getSes(){
        return sessionThreadLocal.get();
    }



}
