package ren.doob.controller.sshconn;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import static ren.doob.common.Mc.*;
import static ren.doob.common.CommonField.*;
import ren.doob.common.*;
import ren.doob.common.sshwebproxy.FileChannel;
import ren.doob.common.sshwebproxy.ShellChannel;

import static ren.doob.common.sshwebproxy.MySsh.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
    @RequestMapping(value = "/cd" ,method = RequestMethod.GET , produces="application/json;charset=UTF-8")
    public Object changeDirectory() throws IOException {

        FileChannel fileChannel = getFileChannel();
        if (fileChannel == null ) {
            HttpServletResponse response = getRes();
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().append("{\"sshNoLogin\":\"1\"}");
            return null;
        }
        String path = getPara().get(REQUEST_CDDIRECTORY);
        if (path != null && !"".equals(path.trim()))
            fileChannel.changeDirectory(path);

        getSes().setAttribute(SESSION_FILEPATH , fileChannel.getCurrentDirectory());
        putR(SSH_INFORMATION , fileChannel.getCurrentDirectoryListing());
        putR("nowpath" , getUserinfo().getName() + fileChannel.getCurrentDirectory());
        return getR();
    }

    @RequestMapping("/download")
    public void download() throws IOException {
        HttpServletResponse response = getRes();
        FileChannel fileChannel = getFileChannel();
        String filename = getPara().get("downloadfilename");
        String isdirectory = getPara().get("isdirectory");

        if(isdirectory.equals("true")){ //如果下载的是dir，则打包zip下载，目前有问题
            String zipdirectory = "zip " + filename + ".zip " + filename;
            ShellChannel shellChannel = getShellChannel();
            shellChannel.write(zipdirectory , true);
            filename = filename + ".zip";
        }

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        // Start writing the output.
        ServletOutputStream outputStream = response.getOutputStream();
        fileChannel.downloadFile( filename, outputStream );

    }

    @RequestMapping("/upload")
    public void upload() throws FileUploadException {

        FileChannel fileChannel = getFileChannel();
        DiskFileUpload upload = new DiskFileUpload();
        List files = upload.parseRequest( getReq() );
        FileItem file = null;
        String fileName = null;

        try {
            Iterator iter = files.iterator();
            while (iter.hasNext()) {
                FileItem fileItem = (FileItem) iter.next();
                String fieldName;
                if (fileItem.isFormField()) {
                    fieldName = fileItem.getFieldName();
                    if ("filename".equals(fieldName)) {
                        fileName = fileItem.getString();
                    }
                } else {
                    file = fileItem;
                }
            }

            fileChannel.uploadFile(fileName , file.getInputStream());
            putR(SSH_INFORMATION , 1);
        }catch (Exception e){
            e.printStackTrace();
            putR(SSH_INFORMATION , 0);
        }
    }

}
