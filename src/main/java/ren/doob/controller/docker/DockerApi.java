package ren.doob.controller.docker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.BaseController;
import ren.doob.common.Mc;
import ren.doob.util.dockerapi.DockerApiUtil;

import java.io.IOException;

/**
 * @author fudali
 * @package ren.doob.controller.docker
 * @class DockerApi
 * @date 2016-2-2
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

@Controller
@RequestMapping("/docker")
public class DockerApi extends BaseController {

    private Log log = LogFactory.getLog(DockerApi.class);

    @Autowired
    private DockerApiUtil dockerApi;

    @ResponseBody   // /**表示获取路径下所有请求
    @RequestMapping(value = "/**" , method = RequestMethod.GET , produces="application/json;charset=UTF-8")
    public Object getApi1Json() throws IOException {

        String path = Mc.getReq().getServletPath();
        String api = path.replaceAll("/docker/" , "").trim();
        log.info(getUserinfo().getName() + "请求了docker remote api:" + api);

        return dockerApi.getDockerApiJson(api);
    }

}
