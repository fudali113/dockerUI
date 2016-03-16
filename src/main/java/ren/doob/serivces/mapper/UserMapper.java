package ren.doob.serivces.mapper;

import ren.doob.common.Parameter;
import ren.doob.serivces.model.User;

/**
 * @class UserMapper
 * @pachage ren.doob.serivces.mapper
 *
 * @author fudali
 * @date 2015-12-21
 */

public interface UserMapper {

    public User getLoginUser(Parameter parameter);
    public int signin(Parameter parameter);
    public int updateUserData(Parameter parameter);

}
