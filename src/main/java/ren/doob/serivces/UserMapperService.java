package ren.doob.serivces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ren.doob.common.Parameter;
import ren.doob.mapper.UserMapper;
import ren.doob.model.User;

/**
 * @author fudali
 * @package ren.doob.serivces
 * @class UserMapperService
 * @date 2015-12-21
 */

@Service
public class UserMapperService {

    @Autowired
    private UserMapper um;

    public User getUser(Parameter parameter){
        return um.getUser(parameter);
    }
}
