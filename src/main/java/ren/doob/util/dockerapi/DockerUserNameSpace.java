package ren.doob.util.dockerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;
import ren.doob.serivces.UserConService;
import ren.doob.util.sshwebproxy.SystemShell;

import java.util.ArrayList;

/**
 * @author fudali
 * @package ren.doob.util.dockerapi
 * @class DockerUserNameSpace
 * @date 2016-2-29
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
@Service
public class DockerUserNameSpace {

    @Autowired
    private UserConService userConService;

    public int createCon(Parameter parameter ){
        int addnum = 0;
        String ConID = null;
        ArrayList<String> result = SystemShell.createCon(parameter);
        for(String i : result){
            System.out.println(i);
            if(i.trim().length() == 64 && i.toLowerCase().equals(i)){
                ConID = i;
            }
        }
        if (ConID != null) {
            parameter.put("id",ConID);
            addnum = userConService.addCon(parameter);
        }
        return addnum;
    }

}
