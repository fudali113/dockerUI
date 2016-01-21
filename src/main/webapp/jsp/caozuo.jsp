<%--
  Created by IntelliJ IDEA.
  User: fudali
  Date: 2016-1-6
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>操作容器</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
<body>
<div class="col-md-1">
    <nav class="navbar navbar-vertical-left">
        <ul class="nav navbar-nav">
            <li>
                <a href="#" title="index">
                    <i class="fa fa-fw fa-lg fa-home"></i>
                    <span>主页</span>
                </a>
            </li>
            <li>
                <a href="#" title="info">
                    <i class="fa fa-fw fa-lg fa-comments-o"></i>
                    <span>信息</span>
                </a>
            </li>
            <li>
                <a href="#" title="computer">
                    <i class="fa fa-fw fa-lg fa-desktop"></i>
                    <span>电脑</span>
                </a>
            </li>
            <li>
                <a href="#" title="shell">
                    <i class="fa fa-fw fa-lg fa-laptop"></i>
                    <span>ssh</span>
                </a>
            </li>
            <li>
                <a href="#" title="upload">
                    <i class="fa fa-fw fa-lg fa-download "></i>
                    <span>下载/上传</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
<div>
    <div class="col-md-11"  id = "index">
        <div class="bs-docs-section">
            <iframe src="/doob/pages/get?page=zhuye"  scroling="yes"  width="100%" height="100%" name="float" frameborder="0" allowTransparency="true"></iframe>
        </div>
    </div>
    <div class="col-md-11"  id = "info">
        <div class="bs-docs-section">
            <iframe src="/doob/pages/get?page=zhuye"  scroling="yes"  width="100%" height="100%" name="float" frameborder="0" allowTransparency="true"></iframe>
        </div>
    </div>
    <div class="col-md-11"  id = "computer">
        <div class="bs-docs-section">
            <iframe src="/doob/pages/get?page=zhuye"  scroling="yes"  width="100%" height="100%" name="float" frameborder="0" allowTransparency="true"></iframe>
        </div>
    </div>
    <div class="col-md-11"  id = "shell">
        <div class="bs-docs-section">
            <iframe src="/doob/pages/get?page=shellChannel"  scroling="yes"  width="100%" height="100%" name="float" frameborder="0" allowTransparency="true"></iframe>
        </div>
    </div>
    <div class="col-md-11"  id = "upload">
        <div class="bs-docs-section">
            <iframe src="/doob/pages/get?page=fileChannel"  scroling="yes"  width="100%" height="100%" name="float" frameborder="0" allowTransparency="true"></iframe>
        </div>
    </div>
</div>
</body>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/caozuo.js"></script>
<script>(function(i,s,o,g,r,a,m){i["DaoVoiceObject"]=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,"script","//widget.daovoice.io/widget/29ab36ab.js","daovoice");</script>
</html>