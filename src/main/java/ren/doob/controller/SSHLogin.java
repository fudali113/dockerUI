package ren.doob.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.BaseController;
import ren.doob.sshwebproxy.ShellChannel;
import ren.doob.sshwebproxy.SshConnectException;
import ren.doob.sshwebproxy.SshConnection;
import ren.doob.sshwebproxy.SshSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fudali
 * @package ren.doob.controller
 * @class SSHLogin
 * @date 2016-1-11
 */
@Controller
public class SSHLogin extends BaseController{

    @ResponseBody
    @RequestMapping("/SSHLogin")
    public HashMap SSHLogin(){
        HashMap mv = new HashMap();
        String ip = request.getParameter("ip");
        String host = request.getParameter("host");
        String name = request.getParameter("name");
        String pass = request.getParameter("pass");
        System.out.println(ip + host + name + pass);
        try{
            SshSession ss = new SshSession(session);
            SshConnection sshConnection = new SshConnection(ip,Integer.parseInt(host),name,pass);
            ss.addSshConnection(sshConnection);
            ShellChannel shellChannel = sshConnection.openShellChannel();
            shellChannel.read();
            mv.put("xinxi",shellChannel.getScreen());
            mv.put("channelid",shellChannel.getChannelId());
            mv.put("biaozhi",1);
            mv.put("ConnectionInfo",sshConnection.getConnectionInfo());
        }catch (SshConnectException sce){
            sce.printStackTrace();
            mv.put("biaozhi",0);
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping("/SSHcaozuo")
    public HashMap SSHml(){
        SshSession sshSession = new SshSession(session);
        HashMap mv = new HashMap();
        String channelid = request.getParameter("channelid");
        String mingl = request.getParameter("mingl");
        String ConnectionInfo = request.getParameter("ConnectionInfo");
        ShellChannel shellChannel = sshSession.getSshConnection(ConnectionInfo).getShellChannel(channelid);
        shellChannel.write(mingl,true);
        shellChannel.read();
        mv.put("xinxi",shellChannel.getScreen());
        return mv;
    }
}
