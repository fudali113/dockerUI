package ren.doob.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ren.doob.common.BaseController;
import ren.doob.util.dockerapi.DockerUserNameSpace;

import java.util.HashMap;

import static ren.doob.common.Mc.*;

/**
 * @author fudali
 * @package ren.doob.controller.login
 * @class Database
 * @date 2016-3-29
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
 */
@Controller
@RequestMapping("database")
public class Database extends BaseController{

    final static HashMap<String,String> hm = new HashMap<String,String>(){{
        put("mysql" , "");
        put("postgresql" , "");
        put("redis" , "");
        put("mongodb" , "");
    }};

    @Autowired
    private DockerUserNameSpace dockerUserNameSpace;

    @RequestMapping("create/{name}")
    public Object createDatabase(@PathVariable("name") String name){

        putP("userid",getUserinfo().getId().toString());
        int result = dockerUserNameSpace.createDatabase(hm.get(name),getPara());

        return putR("result" , result);
    }

}
