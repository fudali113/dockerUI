package ren.doob.util.dockerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Mc;
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

    synchronized public int createCon(Parameter parameter ){//同步保证数据正确

        ArrayList<String> result = SystemShell.createCon(parameter);

        return addIdToMysql(result,parameter);
    }

    public int createDatabase(String runString , Parameter parameter){

        ArrayList<String> result = SystemShell.runShell(runString);
        parameter.put("whethrtdatabase","1");
        return addIdToMysql(result , parameter);
    }

    /**
     * 从返回数据中检索出id,并添加到数据中
     * @param result
     * @param parameter
     * @return 返回插入的条数
     */
    private int addIdToMysql(ArrayList<String> result ,Parameter parameter){
        int addnum = 0;
        String ConID = null;
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
