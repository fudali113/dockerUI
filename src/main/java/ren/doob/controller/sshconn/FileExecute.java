package ren.doob.controller.sshconn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static ren.doob.common.Mc.*;

import ren.doob.common.CommonField;
import static ren.doob.common.Mc.*;

//import ren.doob.common.Mc;
import ren.doob.common.SshBaseController;
import ren.doob.sshwebproxy.FileChannel;
import ren.doob.sshwebproxy.SshSession;

/**
 * @author fudali
 * @package ren.doob.controller.sshconn
 * @class FileExecute
 * @date 2016-1-20
 *
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
 *
 * ━━━━━━感觉萌萌哒━━━━━━
 */

@Controller
@RequestMapping("/ssh/file")
public class FileExecute extends SshBaseController{

    @RequestMapping("/cd")
    public Object changeDirectory(){

        FileChannel fileChannel = getFileChannel();
        System.out.println(fileChannel.getCurrentDirectory());

        putR(CommonField.SSH_INFORMATION , fileChannel.getCurrentDirectoryListing());

        return getR();
    }

    public FileChannel getFileChannel(){
        String channelid = (String) getSes().getAttribute(CommonField.SESSION_SHELLCHANNELID);
        String connectionInfo = (String) getSes().getAttribute(CommonField.SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(getSes());
        FileChannel fileChannel = sshSession.getSshConnection(connectionInfo).getFileChannel(channelid);

        return fileChannel;
    }
}
