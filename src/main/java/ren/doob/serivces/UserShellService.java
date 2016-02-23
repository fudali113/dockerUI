package ren.doob.serivces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;
import ren.doob.serivces.mapper.UserShellMapper;
import ren.doob.serivces.model.Shell;
import java.util.ArrayList;

/**
 * @author fudali
 * @package ren.doob.serivces
 * @class UserShellService
 * @date 2016-2-23
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

@Service("UserShellService")
public class UserShellService {

    @Autowired
    private UserShellMapper userShellMapper;

    public int addShellTerminal(Parameter p){
        return userShellMapper.addShellTerminal(p);
    }

    public ArrayList<Shell> getMyTerminal(Parameter p){
        return userShellMapper.getMyTerminal(p);
    }
}
