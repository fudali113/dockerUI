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
<body ng-controller="containers" style="background: FloralWhite">
<div>
    <h3><Strong>容器构建与查看</Strong></h3>
</div>
<div style="background:#F5F5F5">
    <div>
        <h2><Strong>创建一个新的容器</Strong></h2>
        <Strong>总共{{containers.length}}个容器</Strong>
        <p>构建你的容器并可以登陆shell终端管理你的容器</p>

        <button type="button" class="btn btn-success" ng-click="openSignInModel()">创建一个新的容器</button>
    </div>
    <div><hr/></div>
    <div>
        <div style="float: left ; width: 50%">
            nihao
        </div>
        <div style="float: right ; width: 50%">
            docker
        </div>
    </div>
</div>
<div style="height: 30px"></div>
<div style="background:#F5F5F5">
    <div>
        <h2><Strong>你的容器列表：</Strong></h2>
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
                    <div id="containerInfo" ng-show="modelShowValue == 1">
                        <table style="width: 100%">
                            <tr>
                                <td style=" width: 30%"><Strong>aa:</Strong></td>
                                <td style=" width: 70%"><Strong>{{containerInfo.Name}}</Strong></td>
                            </tr>
                        </table>
                    </div>
                    <div id="imageInfo" ng-show="modelShowValue == 2">
                        <table style="width: 100%">
                            <tr>
                                <td style=" width: 30%"><Strong>oo:</Strong></td>
                                <td style=" width: 70%"><Strong>{{imageInfo.Created}}</Strong></td>
                            </tr>
                        </table>
                    </div>
                    <div ng-show="modelShowValue == 3">

                    </div>
                </div>
            </div>
            <div class="modal-footer" ng-show="modelShowValue == 3">
                <button type="button" class="btn btn-warning"
                        data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-success" ng-click="signInContainer()">
                    注册容器
                </button>
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
