<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016-1-11
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="containersApp">
<head>
    <title>Title</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
<body>
<div ng-controller="containers">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Image</th>
            <th>Command</th>
            <th>Created</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="container in containers">
            <td><a href="#/containers/{{ container.Id }}/">{{ container|containername}}</a></td>
            <td><a href="#/images/{{ container.Image }}/">{{ container.Image }}</a></td>
            <td>{{ container.Command|truncate:40 }}</td>
            <td>{{ container.Created|getdate }}</td>
            <td><span class="label label-{{ container.Status|statusbadge }}">{{ container.Status }}</span></td>
        </tr>
        </tbody>
    </table>
</div>
</body>
<script src="/doob/views/chajian/angular.min.js"></script>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/zhuye.js"></script>
<script src="/doob/views/chajian/common.js"></script>
</html>
