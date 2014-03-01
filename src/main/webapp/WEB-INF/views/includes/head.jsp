<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<!--[if IE 6]><html class="ie6 ielt9 no-css3 no-js lte7" lang="en" dir="ltr"><![endif]-->
<!--[if IE 7]><html class="ie7 ielt9 no-css3 no-js lte7" lang="en" dir="ltr"><![endif]-->
<!--[if IE 8]><html class="ie8 ielt9 no-css3 no-js" lang="en" dir="ltr"><![endif]-->
<!--[if IE 9]><html class="ie9 ielt9 no-css3 no-js" lang="en" dir="ltr"><![endif]-->
<!--[if !(IE 6) | !(IE 7) | !(IE 8) | !(IE 9) ]><!--><html lang="en" dir="ltr" class="no-js"><!--<![endif]-->
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>NEINE预警系统</title>
<link rel="shortcut icon" href="favicon.ico" />
<link rel="stylesheet" href="<c:url value="/assets/css/reset.css"/>" />
<link rel="stylesheet" href="<c:url value="/assets/css/screen.css"/>" />
<!--[if IE]>
<script src="js/ie/html5.js"></script>
<![endif]-->
<!--[if lt IE 9]>
<style type="text/css">.css3{behavior: url(js/ie/css3.htc);-pie-lazy-init: true; position: relative;}.ie6png{-pie-png-fix: true;}</style>
<script src="js/ie/ie9.js"></script>
<![endif]-->
<!--[if IE 6]>
<script src="js/ie/png.js"></script>
<script>DD_belatedPNG.fix('.pngfix, .pngfix img, #logo img, .icon-nav, .divider-v');</script>
<![endif]-->
<script src="<c:url value="/assets/js/ie/modernizr.js"/>"></script>
<script src="<c:url value="/assets/js/jquery/jquery.js"/>"></script>
<script src="<c:url value="/assets/js/jquery/jquery.tools.min.js"/>" ></script>
<script>
var _contextPath = "${pageContext.request.contextPath}";
var _requestPath = "${requestPath}";
//alert(_contextPath);
$(function() {

	// tabs1
    // http://jquerytools.org/demos/tabs/index.html
    $("ul#Tabs-1").tabs("div.panes-1 > div"); 

    // tabs2
    $("ul#Tabs-2").tabs("div.panes-2 > div"); 
	
});

</script>

<!--
ztree
http://www.ztree.me/v3/main.php
-->
<link rel="stylesheet" href="<c:url value="/assets/js/ztree/css/zTreeStyle/zTreeStyle.css" />" type="text/css">
<script type="text/javascript" src="<c:url value="/assets/js/ztree/jquery.ztree.core-3.5.js"/>"></script>
<script type="text/javascript" src="<c:url value="/assets/js/tree_init.js"/>" ></script>

<!--
highcharts
http://www.highcharts.com/demo/
-->
<script src="<c:url value="/assets/js/highcharts/highcharts.js"/>" ></script>
<script src="<c:url value="/assets/js/highcharts/modules/exporting.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/assets/js/highchart_init.js"/>" ></script>

</head>
<body>
<header id="header" class="cf">

		<h1 id="logo">
			<a href="index.html">
				<img src="<c:url value="/assets/images/logo.png"/>" alt="NEINE预警系统" title="NEINE预警系统" />
			</a>
		</h1><!-- logo -->

	    <nav id="nav">
	    	<ul>
                <li class="active"><a href="<c:url value="/monitor"/>"><i class="icon-nav icon-nav1"></i>实时监测</a></li>
                <li><a href="<c:url value="/report"/>"><i class="icon-nav icon-nav2"></i>查询报表</a></li>
                <li><a href="<c:url value="/project"/>"><i class="icon-nav icon-nav3"></i>项目信息</a></li>
                <li><a href="<c:url value="/settings"/>"><i class="icon-nav icon-nav4"></i>参数设置</a></li>
                <li><a href="<c:url value="/help"/>"><i class="icon-nav icon-nav5"></i>帮助手册</a></li>
                <li><a href="<c:url value="/users/logout"/>"><i class="icon-nav icon-nav6"></i>安全退出</a></li>
                <li class="divider-v"></li>
                <li>
                    <p><a class="nav-help" href="help.html">帮助</a></p>
                    <p id="localTime"><script src="<c:url value="/assets/js/localtime.js"/>"></script></p>
                </li>
            </ul>
	    </nav><!-- nav -->

</header><!-- //header -->
