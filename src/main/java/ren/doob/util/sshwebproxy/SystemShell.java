package ren.doob.util.sshwebproxy;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public static ArrayList<String> createCon(String imageName, String ConName){
        String createOrder = "docker run -it -d --name "+ConName+" "+imageName;
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

}
