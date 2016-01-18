package ren.doob.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 把变量装入ThreadLocal保证线程安全
 * @author fudali
 * @package ren.doob.common
 * @class ReqAndResContext
 * @date 2016-1-18
 */

public class ReqAndResContext {

        private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

        private static ThreadLocal<HttpServletResponse> reponseThreadLocal = new ThreadLocal<HttpServletResponse>();

        public static void setRequest(HttpServletRequest request) {
            requestThreadLocal.set(request);
        }

        public static HttpServletRequest getRequest() {
            return requestThreadLocal.get();
        }

        public static void removeRequest() {
            requestThreadLocal.remove();
        }

        public static void setResponse(HttpServletResponse response) {
            reponseThreadLocal.set(response);
        }

        public static HttpServletResponse getResponse() {
            return reponseThreadLocal.get();
        }

        public static void removeResponse() {
            reponseThreadLocal.remove();
        }
}

