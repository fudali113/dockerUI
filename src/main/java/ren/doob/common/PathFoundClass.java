package ren.doob.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/**
 * @author fudali
 * @package ren.doob.common
 * @class PathFoundClass
 * @date 2016-2-14
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓    Code
 * 　　┏┛┻━━━┛┻┓  is
 * 　　┃　　　　　　　┃  far
 * 　　┃　　　━　　　┃  away
 * 　　┃　┳┛　┗┳　┃  from
 * 　　┃　　　　　　　┃  bug
 * 　　┃　　　┻　　　┃  with
 * 　　┃　　　　　　　┃  the
 * 　　┗━┓　　　┏━┛  animal
 * 　　　　┃　　　┃      protecting
 * 　　　　┃　　　┃神兽保佑,代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * 根据请求路径找到相应方法并运行
 * @param <T> 参数类型
 */
public class PathFoundClass<T> {

    private final String firstPackageName = "ren.doob.";

    @SuppressWarnings("rawtypes")
    public Object run(String path ,T t) throws Exception {
        String[] pathList = path.split("/");

        Class foundClass = getClass(pathList[0] , pathList[1]) ;
        if(foundClass == null) return null ;

        Method rightMethod = getMethod(foundClass , pathList[2] , t) ;
        if(rightMethod == null) return null ;

        Object obj = getObject(foundClass) ;
        if(obj == null) return null ;

        return rightMethod.invoke(obj, t) ;
    }

    /**
     * 根据类的完整路径名生成Class实例
     * @param sonPackagePath 子包名
     * @param className 类名
     * @return Class实例变量
     */
    @SuppressWarnings("rawtypes")
    private Class getClass(String sonPackagePath , String className) {

        String fullClassPath = firstPackageName + sonPackagePath + "." + className ;
        Class foundClass = null ;

        try {
            foundClass = Class.forName(fullClassPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return foundClass;
    }

    /**
     * 根据getClass找到的Class变量的getMethod方法找到相关方法
     * 并把相应的参数类传入其中
     *
     * @param foundClass
     * @param methodName
     * @param t 此变量为参数类实例变量
     * @return 返回一个方法
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Method getMethod(Class foundClass , String methodName , T t) {

        Method rightMethod = null ;

        try {
            rightMethod = foundClass.getMethod(methodName, t.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rightMethod ;
    }

    /**
     * 根据找到的Class实例化此类
     * @param foundClass
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object getObject(Class foundClass) {

        Constructor cons = null ;
        Object object = null ;

        try {
            cons = foundClass.getDeclaredConstructor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(cons != null) {
            try {
                object = cons.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return object ;
    }

}
