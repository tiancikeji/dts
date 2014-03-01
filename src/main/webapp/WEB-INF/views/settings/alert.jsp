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
                    <li><a href="<c:url value="/settings/communication" />">通讯设置</a></li>
                    <li><a href="<c:url value="/settings/system" />">系统设置</a></li>
                </ul>
            </div>
        </div>
    </div><!-- //sidebar -->

    <div id="content" class="wrapper">

        <div class="mod-1">
            <div class="hd-1 cf">
                <h2>分区设置</h2>
            </div>
            <div class="bd-1">
                
                <div class="mod-2">
                    <div class="hd-2 cf">
                        <h2>报警设置</h2>
                    </div>
                    <div class="bd-2">
                        <div class="wrap-cont2 cf">

                            <div class="grid-f4 fl">                                
                                <div class="mod-3">
                                    <div class="hd-3 cf">
                                        <h2>定温报警</h2>
                                    </div>
                                    <div class="bd-3">
                                        <div class="wrap-cont">
                                            <ul class="form-list">
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 低级报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 中级报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 紧急报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="btn-wrap">
                                            <input class="btn btn-blue" type="button" value="保存" />
                                            <input class="btn btn-gray" type="button" value="添加分区" />
                                        </div>
                                    </div>
                                </div><!-- mod-3 -->
                            </div><!-- grid-f4 -->

                            <div class="grid-f4 fr">                                
                                <div class="mod-3">
                                    <div class="hd-3 cf">
                                        <h2>差温报警</h2>
                                    </div>
                                    <div class="bd-3">
                                        <div class="wrap-cont">
                                            <ul class="form-list">
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 低级报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 中级报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                                <li>
                                                    <label for="radio"><input type="radio" id="radio" name="radio" /> 紧急报警</label>
                                                    报警声音：
                                                    <select class="select-2 ipt-f3">
                                                        <option value="">请选择</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="btn-wrap">
                                            <input class="btn btn-blue" type="button" value="保存" />
                                            <input class="btn btn-gray" type="button" value="添加分区" />
                                        </div>
                                    </div>
                                </div><!-- mod-3 -->
                            </div><!-- grid-f4 -->

                        </div>
                    </div>
                </div><!-- mod-2 -->

                <div class="mod-2">
                    <div class="hd-2 cf">
                        <h2>分区设置</h2>
                    </div>
                    <div class="bd-2">
                        <table class="table-2">
                            <thead>
                                <tr>
                                    <th>分区名称</th>
                                    <th>起点刻度</th>
                                    <th>终点刻度</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                </tr>                     
                            </tbody>
                        </table>
                        <div class="btn-wrap">
                            <input class="btn btn-blue" type="button" value="保存" />
                            <input class="btn btn-gray" type="button" value="高级设置" />
                        </div>
                    </div>
                </div><!-- mod-2 -->

            </div>
        </div><!-- mod-1 -->

    </div><!-- //content -->   

</div><!-- //container -->
</body>
</html>