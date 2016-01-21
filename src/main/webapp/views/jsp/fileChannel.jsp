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
<html>
<head>
    <title>fileChannel</title>
</head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/doob/views/chajian/bootstrap-vertical-menu.css">
<link rel="stylesheet" type="text/css" href="/doob/views/css/index.css">
<body>
<div>
    <div>
        <h2><strong id="nowpath"></strong></h2>
    </div>
    <div>
        <table id="fileTable" class="table table-bordered">
        <thead>
        <tr>
            <th>NO.</th>
            <th>文件名</th>
            <th>操作权限</th>
            <th>大小</th>
            <th>最后修改时间</th>
            <th>下载</th>
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
</body>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<script src="/doob/views/js/fileChannel.js"></script>
<script src="/doob/views/chajian/common.js"></script>
</html>
