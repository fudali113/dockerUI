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
    <link rel="Shortcut Icon" href="/doob/views/images/ooo.ico">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
    <link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
</head>
<body ng-controller="containers">
<div>
    <div>
        <h2 style="color: aqua"><Strong>你的容器：</Strong></h2>
    </div>
    <div>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>容器名</th>
                    <th>镜像</th>
                    <th>运行终端</th>
                    <th>创建</th>
                    <th>状态</th>
                </tr>
            </thead>
            <tbody>
                <tr ng-repeat="container in containers">
                    <td><a href="#/doob/docker/containers/{{ container.Id }}/json" ng-click = "getIC($event , 'c')">{{ container.Id}}</a></td>
                    <td><a href="#/doob/docker/images/{{ container.Image }}/json" ng-click = "getIC($event , 'i')">{{ container.Image }}</a></td>
                    <td>{{ container.Command}}</td>
                    <td>{{ container.Created }}</td>
                    <td>{{ container.Status }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<%--模态框--%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" align="cemter">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h2 class="modal-title">
                    <strong>{{modelTitle}}</strong>
                </h2>
            </div>
            <div class="modal-body">
                <div>
                    <div id="containerInfo" style="width: 50% ; float:left" ng-show="containerName">
                        <table>
                            <tr ng-model="containerInfo"><td>aa:</td><td>{{containerInfo.Name}}</td></tr>
                        </table>
                    </div>
                    <div id="imageInfo" style="width: 50% ; float:left" ng-show="imageName">
                        <table>
                            <tr ng-model="imageInfo" ><td>oo:</td><td>{{imageInfo.Created}}</td></tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
<script src="//cdn.bootcss.com/angular.js/1.4.8/angular.min.js"></script>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/zhuye.js"></script>
<script src="/doob/views/chajian/common.js"></script>
</html>
