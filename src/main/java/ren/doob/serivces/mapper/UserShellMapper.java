package ren.doob.serivces.mapper;

import ren.doob.common.Parameter;
import ren.doob.serivces.model.Shell;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-2-23.
 */
public interface UserShellMapper {

    public int addShellTerminal(Parameter p);
    public ArrayList<Shell> getMyTerminal(Parameter p);

}
