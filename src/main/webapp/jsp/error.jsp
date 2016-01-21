<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-12-21
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404 error page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/views/css/error.css" rel="stylesheet" type="text/css" media="all" />
</head>
<body>

<div class="wrap">
    <!-----start-content--------->
    <div class="content">
        <!-----start-logo--------->
        <div class="logo">
            <h1><a href="#"><img src="/views/images/logo.png"/></a></h1>
            <span><img src="/views/images/signal.png"/>Oops! The Page you requested was not found!</span>
        </div>
        <!-----end-logo--------->
        <!-----start-search-bar-section--------->
        <div class="buttom">
            <div class="seach_bar">
                <p>you can go to <span><a href="#">home</a></span> page or search here</p>
                <!-----start-sear-box--------->
                <div class="search_box">
                    <form>
                        <input type="text" value="Search" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search';}"><input type="submit" value="">
                    </form>
                </div>
            </div>
        </div>
        <!-----end-sear-bar--------->
    </div>
    <!----copy-right-------------->
    <p class="copy_right">&#169; 2014 Template by<a href="http://w3layouts.com" target="_blank">&nbsp;w3layouts</a> </p>
</div>

<!---------end-wrap---------->
</body>
</html>