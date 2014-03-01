<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../includes/head.jsp" %>

<div id="container" class="grid-elastic cf">

    <div id="sidebar" class="grid-220">
        <div class="menu">
            <h3>项目信息</h3>
            <div class="menu-content">
                <ul>
                    <li class="mactive"><a href="<c:url value="/project" />">监测对象</a></li>
                    <li><a href="<c:url value="/project/graph" />" >系统拓补图</a></li>
                    <li><a href="<c:url value="/project/info" />" >系统概况</a></li>
               </ul>
            </div>
        </div>
    </div><!-- //sidebar -->

    <div id="content" class="wrapper">

        <div class="mod-1">
            <div class="hd-1 cf">
                <h2>系统拓补图</h2>
            </div>
            <div class="bd-1">
                <div class="wrap-img">
                    <img src="<c:url value="/assets/images/pic/pic2.jpg" />" alt="图" />
                </div>
            </div>
        </div><!-- mod-1 -->

    </div><!-- //content -->   

</div><!-- //container -->
</body>
</html>