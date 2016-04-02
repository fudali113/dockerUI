package ren.doob.util.dockerapi;

import ren.doob.common.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fudali
 * @package ren.doob.util.dockerapi
 * @class CreateDockerShellString
 * @date 2016-3-10
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

public class CreateDockerShellString {

    public static String createRunString(Parameter parameter){
        HashMap<String,String> runPara = new HashMap();
        HashMap<String,String> portPara = new HashMap();
        HashMap<String,String> map = parameter.getAccept();

        StringBuffer runParaString = new StringBuffer("");
        StringBuffer portParaString = new StringBuffer("");
        StringBuffer runString = new StringBuffer("");
        String conNameString = " --name " + parameter.get("conName");
        String imageName = " "+parameter.get("imageName");

        for(Map.Entry<String,String> para : map.entrySet()){
            String key = para.getKey();
            String value = key.replaceAll("key","value");
            if(key.contains("para_key_")){
                runPara.put(map.get(key),map.get(value));
            }else if(key.contains("port_key_")){
                portPara.put(map.get(key),map.get(value));
            }
        }
        if(runPara.containsKey("--name")) runPara.remove("--name");
        if(runPara.containsKey("-d")) runPara.remove("-d");
        for (String parakey : runPara.keySet()){
            String value = runPara.get(parakey);
            if(parakey.trim().contains(" ") || value.trim().contains(" ")) return "运行参数中不能出现空格！！！";
            runParaString.append(parakey + " " + value + " ");
        }
        if(!portPara.isEmpty()) {
            for (String parakey : portPara.keySet()) {
                String value = portPara.get(parakey);
                try {
                    Integer.parseInt(parakey);
                    Integer.parseInt(value);
                }catch (NumberFormatException e){
                    continue;
                }
                if(parakey != null && !parakey.equals("") && value != null && !value.equals("")) {
                    portParaString.append(" -p ");
                    portParaString.append(parakey + ":" + value + " ");
                }
            }
        }

        runString.append("docker run -d ").append(runParaString).append(portParaString).append(conNameString).append(imageName);
        System.out.println(runString);
        return runString.toString();
    }

}
