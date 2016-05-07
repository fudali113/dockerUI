package ren.doob.util.dockerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Mc;
import ren.doob.common.Parameter;
import ren.doob.serivces.UserConService;
import ren.doob.serivces.model.Container;
import ren.doob.util.sshwebproxy.SystemShell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private DockerApiUtil dockerApiUtil;

    synchronized public int createCon(Parameter parameter ){//同步保证数据正确

        ArrayList<String> result = SystemShell.createCon(parameter);

        return addIdToMysql(result,parameter);
    }

    public int createDatabase(String runString , Parameter parameter){

        ArrayList<String> result = SystemShell.runShell(runString);
        parameter.put("whethrtdatabase","1");
        return addIdToMysql(result , parameter);
    }

    public int deleteConOrIma(String Id , boolean isCon){

        String deleteStr = "";
        if(isCon) deleteStr = "docker rm -f " + Id;
        else deleteStr = "docker rmi -f " + Id;

        ArrayList<String> result = SystemShell.runShell(deleteStr);

        if (Id.equals(result.get(0))){
            Parameter p = new Parameter();
            p.put("name" , Id);
            userConService.deleteCon(p);
            return 1;
        }else{
            return 0;
        }

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
        if (ConID != null) { // 如果创建容器成功，添加到数据库
            parameter.put("id",ConID);
            addnum = userConService.addCon(parameter);
        }

        if (addnum == 0 ){ //如果添加数据库失败，删除此容器
            deleteConOrIma(parameter.get("name") , true);
        }
        return addnum;
    }

    /**
     * 根据用户id查找自己的容器
     * @param userId
     * @param select 选择是否是容器实例或者数据库实例；
     * @return docker remote API格式容器列表
     */
    public List getMyCon(Integer userId , Integer select){

        Parameter parameter = new Parameter();
        ArrayList arrayList = new ArrayList();

        parameter.put("userid" , userId.toString());

        if (select == 0 || select == 1){
            parameter.put("whetherdatabase" , select.toString());
        }

        ArrayList<Container> userCon = userConService.getCon(parameter);

        for (Container c: userCon) {

            String result = null;
            try {
                result = dockerApiUtil.getDockerApiJson("containers/"+c.getContainerid()+"/json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(result != null) arrayList.add(result);

        }

        return arrayList;
    }


}
