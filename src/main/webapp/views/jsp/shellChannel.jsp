<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016-1-11
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
<head>
    <title>Title</title>
</head>
<body>
<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal">登陆终端</button>
<div class="form-group">
    <textarea class="form-control" rows="31" id="shuc" readonly="true"></textarea>
</div>
<div class="col-lg-6">
    <div class="input-group">
        <input type="text" class="form-control" id="mingl" placeholder="在这里输入你要执行的命令">
               <span class="input-group-btn">
                  <button class="btn btn-success" type="button" id="scml">Go!</button>
                   <button class="btn btn-success" type="button" id="scml1">History</button>
               </span>
    </div><!-- /input-group -->
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" align="cemter">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h2 class="modal-title" id="myModalLabel">
                    <strong>登陆SSH Shell</strong>
                </h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="SSHlogin">
                    <div class="form-group">
                        <label for="ssh1" class="col-sm-2 control-label">IP：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssh1" value="139.129.4.187" name="ssh_ip">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="ssh2" class="col-sm-2 control-label">Host：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssh2" value="22" name="ssh_host" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="ssh3" class="col-sm-2 control-label">Name：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssh3" value="root" name="ssh_name" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="ssh4" class="col-sm-2 control-label">Pass：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="ssh4" value="******" name="ssh_pass">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning"
                        data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-success" id="sshdl">
                    登陆
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/shellChannel.js"></script>
</body>
</html>
