package ren.doob.util.sshwebproxy;

import ren.doob.common.CommonField;
import ren.doob.common.Mc;

import static ren.doob.common.CommonField.*;
import static ren.doob.common.Mc.*;

/**
 * @author fudali
 * @package ren.doob.util.sshwebproxy
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

    /**
     * 根据连接信息连接ssh
     * 并返回带有连接信息的已read的shellChannel对象
     *
     * @return 返回连接后可操作的一个shell
     */
    public static   ShellChannel connection(){

        ShellChannel shellChannel = null;
        FileChannel fileChannel = null;
        SshConnection sshConnection = null;

        try{
            SshSession ss = new SshSession(Mc.getSes());
            sshConnection = new SshConnection(getPara().get("ssh_ip"),Integer.parseInt(getPara().get("ssh_host")),getPara().get("ssh_name"),getPara().get("ssh_pass"));
            ss.addSshConnection(sshConnection);
            shellChannel = sshConnection.openShellChannel();
            fileChannel = sshConnection.openFileChannel();

        }catch (SshConnectException sce){
            sce.printStackTrace();
        }

        if (shellChannel != null && sshConnection != null && fileChannel != null) {
            Mc.getSes().setAttribute(CommonField.SESSION_SHELLCHANNELID , shellChannel.getChannelId());
            Mc.getSes().setAttribute(CommonField.SESSION_CONNECTIONINFO , sshConnection.getConnectionInfo());
            Mc.getSes().setAttribute(CommonField.SESSION_FILECHANNELID , fileChannel.getChannelId());
            Mc.getSes().setAttribute("loginInfo",getPara());
            shellChannel.read();
        }

        return shellChannel;
    }

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
        ShellChannel shellChannel = null;
        SshSession sshSession = new SshSession(Mc.getSes());
        SshConnection sshConnection = sshSession.getSshConnection(connectionInfo);
        if (sshConnection != null){
            shellChannel = sshConnection.getShellChannel(channelid);
        }
        return shellChannel;
    }
}
