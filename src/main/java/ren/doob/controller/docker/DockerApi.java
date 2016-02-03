package ren.doob.controller.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.BaseController;
import ren.doob.common.Mc;
import ren.doob.dockerapi.DockerApiUtil;

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

    @Autowired
    private DockerApiUtil dockerApi;

    @ResponseBody   // /**表示获取路径下所有请求
    @RequestMapping(value = "/**" , method = RequestMethod.GET , produces="application/json;charset=UTF-8")
    public Object getApi1Json() throws IOException {

        String path = Mc.getReq().getServletPath();
        String api = path.replaceAll("/docker/" , "").trim();

        return dockerApi.getDockerApiJson(api);
    }

}
