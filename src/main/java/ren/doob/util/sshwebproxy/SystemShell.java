package ren.doob.util.sshwebproxy;

import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;
import ren.doob.util.dockerapi.CreateDockerShellString;

import java.util.ArrayList;
import java.util.Arrays;
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

    synchronized public static ArrayList<String> runShell(String shell){
        shellChannel.read();
        String[] before = shellChannel.getScreen();
        System.out.println(shell);
        System.out.println(before);
        shellChannel.write(shell , true);

        try {
            Thread.sleep(310);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shellChannel.read();
            String[] after = shellChannel.getScreen();
            System.out.println(after);
            return arrayDiff(after,before);
        }

    }

    synchronized public static ArrayList<String> createCon(Parameter parameter){//同步保证数据正确
        String createOrder = CreateDockerShellString.createRunString(parameter);
        return runShell(createOrder);
    }

    private static ArrayList<String> arrayDiff(String[] after , String[] before ){
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

}
