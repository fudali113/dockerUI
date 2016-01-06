<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>doob</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">

<div class="container" style="width: 100%">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-inverse" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">doob</a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li class="active">
                            <a href="#">主页</a>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">操作<strong class="caret"></strong></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="#">Action</a>
                                </li>
                                <li>
                                    <a href="#">Another action</a>
                                </li>
                                <li>
                                    <a href="#">Something else here</a>
                                </li>
                                <li class="divider">
                                </li>
                                <li>
                                    <a href="#">Separated link</a>
                                </li>
                                <li class="divider">
                                </li>
                                <li>
                                    <a href="#">One more separated link</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <form class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="text" class="form-control" />
                        </div> <button type="submit" class="btn btn-default">搜索</button>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="#">登陆/注册</a>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">帮助<strong class="caret"></strong></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="#">Action</a>
                                </li>
                                <li>
                                    <a href="#">Another action</a>
                                </li>
                                <li>
                                    <a href="#">Something else here</a>
                                </li>
                                <li class="divider">
                                </li>
                                <li>
                                    <a href="#">Separated link</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>

            </nav>
        </div>
    </div>
</div>

<%--侧边导航--%>
<div class="col-md-1">
<nav class="navbar navbar-vertical-left">
    <ul class="nav navbar-nav">
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-home"></i>
                <span>主页</span>
            </a>
        </li>
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-download "></i>
                <span>下载</span>
            </a>
        </li>
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-comments-o"></i>
                <span>信息</span>
            </a>
        </li>
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-desktop"></i>
                <span>电脑</span>
            </a>
        </li>
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-tablet"></i>
                <span>手机</span>
            </a>
        </li>
        <li>
            <a href>
                <i class="fa fa-fw fa-lg fa-laptop"></i>
                <span>平板</span>
            </a>
        </li>
    </ul>
</nav>
</div>

<div class="col-md-11" >
    <div class="bs-docs-section">
        <!-- <h1 id="overview" class="page-header">任务提报</h1> -->
        <iframe src="/doob/views/jsp/error.jsp"  scroling="yes"  width="100%" height="90%" name="float" frameborder="0" allowTransparency="true">
        </iframe>

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
        <script src="/doob/views/js/index.js"></script>

</body>
</html>
