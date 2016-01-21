<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016-1-11
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
<body>
<div>
    <table id="fileTable" class="table table-bordered">
        <thead>
        <tr>
            <th style="width: 6%">NO.</th>
            <th style="width: 39%">文件名</th>
            <th style="width: 12%">操作权限</th>
            <th style="width: 10%">大小</th>
            <th style="width: 15%">最后修改时间</th>
            <th style="width: 10%">属性</th>
            <th style="width: 8%">下载</th>
        </tr>
        </thead>
        <tbody id="fileTableTbody">

        </tbody>
    </table>
</div>
</body>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/zhuye.js"></script>
<script src="/doob/views/chajian/common.js"></script>
</html>
