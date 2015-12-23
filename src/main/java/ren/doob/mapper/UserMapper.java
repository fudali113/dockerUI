package ren.doob.mapper;

import ren.doob.common.Parameter;
import ren.doob.model.User;

/**
 * @class UserMapper
 * @pachage ren.doob.mapper
 *
 * @author fudali
 * @date 2015-12-21
 */

public interface UserMapper {

    public User getLoginUser(Parameter parameter);
}
