<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../includes/head.jsp" %>

<div id="container" class="grid-elastic cf">

    <div id="sidebar" class="grid-220">
        <div class="menu">
            <h3>参数设置</h3>
            <div class="menu-content">
                <ul>
                    <li class="mactive"><a href="<c:url value="/settings/sensor" />">传感器设置</a></li>
                    <li><a href="<c:url value="/settings/area" />">厂区设置</a></li>
                    <li><a href="<c:url value="/settings/alert" />">分区设置</a></li>
                    <li><a href="<c:url value="/settings/" />">通讯设置</a></li>
                    <li><a href="<c:url value="/settings/system" />">系统设置</a></li>
                </ul>
            </div>
        </div>
    </div><!-- //sidebar -->

    <div id="content" class="wrapper">

        <div class="mod-1">
            <div class="hd-1 cf">
                <h2>传感器设置</h2>
            </div>
            <div class="bd-1">
                
                <div class="mod-2">
                    <div class="hd-2 cf">
                        <h2>基本计算参数</h2>
                    </div>
                    <div class="bd-2">
                        <div class="wrap-cont">
                            基本计算参数
                        </div>
                        <div class="btn-wrap">
                            <input class="btn btn-blue" type="button" value="保 存" />
                        </div>
                    </div>
                </div><!-- mod-2 -->

                <div class="mod-2">
                    <div class="hd-2 cf">
                        <h2>台阶修正</h2>
                    </div>
                    <div class="bd-2">
                        <div class="wrap-cont">
                            台阶修正
                        </div>
                        <div class="btn-wrap">
                            <input class="btn btn-blue" type="button" value="保 存" />
                        </div>
                    </div>
                </div><!-- mod-2 -->

                <div class="mod-2">
                    <div class="hd-2 cf">
                        <h2>高级设置</h2>
                    </div>
                    <div class="bd-2">
                        <div class="wrap-cont">
                            高级设置
                        </div>
                        <div class="btn-wrap">
                            <input class="btn btn-blue" type="button" value="保 存" />
                        </div>
                    </div>
                </div><!-- mod-2 -->

            </div>
        </div><!-- mod-1 -->

    </div><!-- //content -->   

</div><!-- //container -->
</body>
</html>