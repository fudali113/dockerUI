package ren.doob.common.sshwebproxy;

import ren.doob.common.Mc;

import static ren.doob.common.CommonField.*;
import static ren.doob.common.Mc.*;

/**
 * @author fudali
 * @package ren.doob.common.sshwebproxy
 * @class MySsh
 * @date 2016-1-21
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

public class MySsh {

    public static FileChannel getFileChannel(){
        String channelid = (String) getSes().getAttribute(SESSION_FILECHANNELID);
        String connectionInfo = (String) getSes().getAttribute(SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(getSes());
        FileChannel fileChannel = null;
        try {
            fileChannel = sshSession.getSshConnection(connectionInfo).getFileChannel(channelid);
        }catch (Exception e){
            System.out.println("用户还没有登陆你的ssh账号！");
        }

        return fileChannel;
    }

    /**
     * 获取当前session中连接的shellChannel
     * 并返回包含运行命令后的信息的shellChannel
     *
     * @return
     */
    public static ShellChannel getShellChannel(){

        String channelid = (String) Mc.getSes().getAttribute(SESSION_SHELLCHANNELID);
        String connectionInfo = (String) Mc.getSes().getAttribute(SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(Mc.getSes());
        ShellChannel shellChannel = sshSession.getSshConnection(connectionInfo).getShellChannel(channelid);

        return shellChannel;
    }
}
