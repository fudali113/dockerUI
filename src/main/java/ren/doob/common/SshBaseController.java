package ren.doob.common;

/**
 * @author fudali
 * @package ren.doob.common
 * @class SshBaseController
 * @date 2016-1-14
 */

public class SshBaseController extends BaseController{

    protected String ssh_ip ;
    protected String ssh_host ;
    protected String ssh_name ;
    protected String ssh_pass ;
    protected String ssh_mingl ;
    protected String channelid ;
    protected String connectionInfo;


    /**
     * 加载相关的字段
     */
    protected void localField(){
        ssh_ip = Mc.getReq().getParameter("ssh_ip");
        ssh_host = Mc.getReq().getParameter("ssh_host");
        ssh_name = Mc.getReq().getParameter("ssh_name");
        ssh_pass = Mc.getReq().getParameter("ssh_pass");
        ssh_mingl = Mc.getReq().getParameter("ssh_mingl");
        channelid =(String) Mc.getSes().getAttribute(CommonField.SESSION_CHANNELID);
        connectionInfo = (String) Mc.getSes().getAttribute(CommonField.SESSION_CONNECTIONINFO);
    }

}
