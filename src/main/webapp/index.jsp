<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>doob</title>
</head>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/views/chajian/BootSideMenu.css">

<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="navbar-header" style="width: 200px">
        <a class="navbar-brand" href="#"><strong>doob</strong></a>
    </div>
    <div>
        <ul class="nav navbar-nav" id="nnn">
            <li class="active"><a href="#" title="/emmanage/html/renwtb.html">主页</a></li>
            <li><a href="#" title="/emmanage/html/quexxx.html">信息</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    请假
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#" data-toggle="modal" data-target="#myModal1">请假</a></li>
                    <li><a href="#" data-toggle="modal" data-target="#myModal2">查看请假记录</a></li>
                    <li class="divider"></li>
                    <li><a href="#" data-toggle="modal" data-target="#myModal3">请假审核</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    管理
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu" id="caidan">
                    <li><a href="#" title="html/xitgl.html">系统管理</a></li>
                </ul>
            </li>
            <li><a href="#" data-toggle="modal" data-target="#myModal">登陆</a></li>
        </ul>
    </div>
    <div align="right">
    </div>
</nav>
<div id="demo">
    <div class="list-group">
        <a href="#" class="list-group-item active">Item 1</a>
        <a href="#" class="list-group-item">Item 2</a>
        <a href="#" class="list-group-item">Item 3</a>
        <a href="#" class="list-group-item">Item 4</a>
        <a href="#" class="list-group-item">Item 5</a>
    </div>
</div>



<!-- 模态框（Modal） -->
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
                    <strong>登陆</strong>
                </h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="denglu">
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">登录名：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="firstname"
                                   placeholder="请输入登录名" name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">密码：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="lastname"
                                   placeholder="请输入密码" name="pass">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox"> 请记住我
                                </label>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-warning"
                        data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-success">
                    登陆
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
        <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
        <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
        <script src="/views/chajian/BootSideMenu.js"></script>
        <script src="/views/js/index.js"></script>

</body>
</html>
