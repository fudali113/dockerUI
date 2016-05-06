package ren.doob.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.BaseController;
import ren.doob.serivces.UserConService;
import ren.doob.serivces.model.Container;
import ren.doob.util.dockerapi.DockerUserNameSpace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
@RequestMapping("/database")
public class Database extends BaseController{

    final static HashMap<String,String> setPasswdField = new HashMap<String,String>(){{
        put("mysql" , "MYSQL_ROOT_PASSWORD");
        put("postgresql" , "POSTGRES_PASSWORD");
        put("redis" , "REDIS_PASS");
        put("mongodb" , "");
    }};

    final static HashMap<String,String> imageName = new HashMap<String,String>(){{
        put("mysql" , "mysql:5.6");
        put("postgresql" , "daocloud.io/library/postgres:9.5.1");
        put("redis" , "redis");
        put("mongodb" , "daocloud.io/library/mongo:3.2.4");
    }};

    @Autowired
    private DockerUserNameSpace dockerUserNameSpace;
    @Autowired
    private UserConService userConService;

    @ResponseBody
    @RequestMapping(value = "/create/{name}" , method = RequestMethod.POST , produces="application/json;charset=UTF-8")
    public Object createDatabase(@PathVariable("name") String name){

        String conName = getUserinfo().getName()+"_d_"+getPara().get("databaseName");
        String passwd = setPasswdField.get(name) + "=" + getPara().get("databasePasswd");

        putP("userid",getUserinfo().getId().toString());
        putP("whetherData","1");
        putP("name",conName);

        StringBuilder runString = new StringBuilder("docker run -d --name ");
        runString.append(conName).append(" -e ").append(passwd).append(" "+imageName.get(name));

        System.out.println(runString);

        int result = dockerUserNameSpace.createDatabase(runString.toString(),getPara());

        return putR("result" , result);
    }

    @ResponseBody
    @RequestMapping(value = "/get" , method = RequestMethod.GET , produces="application/json;charset=UTF-8")
    public Object getMyDatabase(){

        putP("userid",getUserinfo().getId().toString());
        ArrayList<Container> al = userConService.getCon(getPara());

        Iterator<Container> iterator = al.iterator();

        while (iterator.hasNext()){
            Container container = iterator.next();
            if(container.getWhetherdatabase() == 0){
                iterator.remove();
            }
        }

        putR("result",al);

        return getR();
    }

}
