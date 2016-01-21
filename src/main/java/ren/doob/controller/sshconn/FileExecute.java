package ren.doob.controller.sshconn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static ren.doob.common.Mc.*;
import static ren.doob.common.CommonField.*;
import ren.doob.common.*;
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

    @ResponseBody
    @RequestMapping("/cd")
    public Object changeDirectory(){

        FileChannel fileChannel = getFileChannel();
        String path = getPara().get(REQUEST_CDDIRECTORY);
        if (path != null && !"".equals(path.trim()))
            fileChannel.changeDirectory(path);

        getSes().setAttribute(SESSION_FILEPATH , fileChannel.getCurrentDirectory());
        putR(SSH_INFORMATION , fileChannel.getCurrentDirectoryListing());
        putR("nowpath" , fileChannel.getCurrentDirectory());

        return getR();
    }

    public FileChannel getFileChannel(){
        String channelid = (String) getSes().getAttribute(SESSION_FILECHANNELID);
        String connectionInfo = (String) getSes().getAttribute(SESSION_CONNECTIONINFO);

        SshSession sshSession = new SshSession(getSes());
        FileChannel fileChannel = sshSession.getSshConnection(connectionInfo).getFileChannel(channelid);

        return fileChannel;
    }
}
