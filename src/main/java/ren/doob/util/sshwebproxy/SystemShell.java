package ren.doob.util.sshwebproxy;

import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fudali
 * @package ren.doob.util.sshwebproxy
 * @class SystemShell
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
public class SystemShell {

    private static final SshConnection sshConnection = new SshConnection("139.129.4.187", 22, "root", "fudali133B");
    private static final   ShellChannel shellChannel = sshConnection.openShellChannel();


    public static ShellChannel getSystemShell(){
        return shellChannel;
    }
    public static SshConnection getSshConnection(){
        return sshConnection;
    }

    public static ArrayList<String> createCon(Parameter parameter){
        String createOrder = createRunString(parameter);
        shellChannel.read();
        String[] before = shellChannel.getScreen();
        shellChannel.write(createOrder , true);
        shellChannel.read();
        String[] after = shellChannel.getScreen();
        ArrayList<String> result = new ArrayList<String>();
        for(String i : after){
            int count = 0;
            for(String j : before){
                if(i.equals(j)){
                    count++;
                }
            }
            if(count == 0) result.add(i);
        }
        return result;
    }

    private static String createRunString(Parameter parameter){
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
        for (String parakey : runPara.keySet()){
            String value = runPara.get(parakey);
            if(parakey.trim().contains(" ") || value.trim().contains(" ")) return "运行参数中不能出现空格！！！";
            runParaString.append(parakey + " " + value + " ");
        }
        if(!portPara.isEmpty()) {
            portParaString.append(" -p ");
            for (String parakey : portPara.keySet()) {
                String value = portPara.get(parakey);
                try {
                    Integer.parseInt(parakey);
                    Integer.parseInt(value);
                }catch (NumberFormatException e){
                    continue;
                }
                if(parakey != null && !parakey.equals("") && value != null && !value.equals(""))
                    portParaString.append(parakey + ":" + value + " ");
            }
        }

        runString.append("docker run ").append(runParaString).append(portParaString).append(conNameString).append(imageName);
        return runString.toString();
    }

}
