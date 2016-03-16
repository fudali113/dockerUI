package ren.doob.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static ren.doob.common.Mc.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ren.doob.common.BaseController;
import ren.doob.common.CommonField;
import ren.doob.serivces.UserMapperService;

/**
 * @author fudali
 * @package ren.doob.controller.login
 * @class TableHandle
 * @date 2016-3-16
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
@RequestMapping("/tables")
public class TableHandle extends BaseController{

    @Autowired
    private UserMapperService userMapperService;

    @ResponseBody
    @RequestMapping("/update/user")
    public Object updateUser() {
        if (getPara().getAccept().isEmpty()){
            return putR("error" , "请确认输入是否输入修改数据");
        }
        int updateNUmber = userMapperService.updateUserData(getPara());
        if(updateNUmber == 1){
            getSes().setAttribute(CommonField.SESSION_USERINFO , userMapperService.getUser(getPara()));
        }
        putR("result" , updateNUmber);
        return getR();
    }

}
