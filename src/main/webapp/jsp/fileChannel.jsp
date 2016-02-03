<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fudali
  Date: 2016-1-20
  Time: 19:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "ren.doob.common.CommonField" %>
<%@ page import="ren.doob.sshwebproxy.SshConstants" %>
<html>
<head>
    <title>fileChannel</title>
    <link rel="Shortcut Icon" href="/doob/views/images/ooo.ico">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
    <link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
</head>
<body>
<div>
    <div>
        <div class="col-md-10">
            <h2><strong id="nowpath"></strong></h2>
        </div>
        <div class="col-md-2">
            <button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal">上传文件</button>
            <button type="button" class="btn btn-success" onclick="window.location.reload()">刷新</button>
        </div>
    </div>
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
</div>
<div class=".navbar-fixed-bottom" style="position:fixed; bottom:0">
    <div class="col-lg-3">
        <div class="input-group">
            <span class="input-group-addon" id="totalpages">@</span>
            <input type="text" class="form-control" value="1" id="yes">
               <span class="input-group-btn">
               		<button class="btn btn-default" type="button" id="zhuand" onclick="zhuand();">
                        转到
                    </button>
                    <button class="btn btn-default" type="button" id="shangyy" onclick="shangyy();">
                        上一页
                    </button>
                  <button class="btn btn-default" type="button" id="xiayy" onclick="xiayy();">
                      下一页
                  </button>
               </span>
        </div><!-- /input-group -->
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" align="cemter">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title" id="myModalLabel">
                    <strong>上传文件</strong>
                </h2>
            </div>
            <div class="modal-body">
                <div>
                    <form ENCTYPE="multipart/form-data" name="uploadFile" method="post" action="/doob/ssh/file/upload">
                        重命名： <input type="text" name="filename" class="form-control" />
                        <input type="file" name="file" />
                        <input type="submit" value="上传" class="sucesss"/>
                    </form>
                </div>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/fileChannel.js"></script>
<script src="/doob/views/chajian/common.js"></script>
</html>
