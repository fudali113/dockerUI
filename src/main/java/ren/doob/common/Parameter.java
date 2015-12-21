package ren.doob.common;

import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.common
 * @class Parameter
 * @date 2015-12-21
 */

public class Parameter {
    //装入请求参数
    public static HashMap<String,String> accept = new HashMap<String, String>();

    //请求人的id
    private String id;

    //请求人的name
    private String name;

    public HashMap<String, String> getAccept() {
        return accept;
    }

    public void setAccept(HashMap<String, String> accept) {
        this.accept = accept;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
