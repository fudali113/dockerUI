package ren.doob.common;

import java.io.ObjectInput;
import java.util.HashMap;

/**
 * @author fudali
 * @package ren.doob.common
 * @class Parameter
 * @date 2015-12-21
 */

public class Parameter {
    //装入请求参数
    private  HashMap<String,String> accept = new HashMap<String, String>();

    //请求人的id
    private String id;

    //请求人的name
    private String name;

    public void put(String name, String value){
        this.accept.put(name , value);
    }

    public  String get(String name){
        return this.accept.get(name);
    }

    public void clear(){
        this.accept.clear();
    }

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
