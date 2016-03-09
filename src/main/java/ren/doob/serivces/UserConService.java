package ren.doob.serivces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;
import ren.doob.serivces.mapper.UserConMapper;
import ren.doob.serivces.model.Container;

import java.util.ArrayList;

/**
 * @author fudali
 * @package ren.doob.serivces
 * @class UserConService
 * @date 2016-2-29
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
@Service
public class UserConService {

    @Autowired
    private UserConMapper userConMapper;

    public int addCon(Parameter parameter){
        return userConMapper.addCon(parameter);
    }

    public int deleteCon(Parameter parameter){
        return userConMapper.deleteCon(parameter);
    }

    public ArrayList<Container> getCon(Parameter parameter){
        return userConMapper.getCon(parameter);
    }

}
