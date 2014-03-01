<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../includes/head.jsp" %>

<div id="container" class="grid-elastic cf">

    <div id="sidebar" class="grid-220">
        <div class="menu">
            <h3>实时监测</h3>
            
            <ul id="Tabs-1" class="tabs-1 cf">
                <li><a href="#">监测区域</a></li>
                <li><a href="#">仪器</a></li>
                <li><a href="#">传感器</a></li>
            </ul>
            <div class="panes-1">
                <div>
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
                <div>
                    仪器
                </div>
                <div>
                    传感器
                </div>
            </div><!-- tabs1 -->

        </div>
        <div class="form-mini ac">
            <p>监测名称：<input class="ipt-txt ipt-s" type="text" /></p>
            <p>最高温度：<input class="ipt-txt ipt-s" type="text" /></p>
            <p>最低温度：<input class="ipt-txt ipt-s" type="text" /></p>
            <p>平均温度：<input class="ipt-txt ipt-s" type="text" /></p>
        </div>
    </div><!-- //sidebar -->

    <div id="content" class="wrapper">

        <div class="mod-1">
            <div class="hd-1 cf">
                <ul id="Tabs-2" class="tabs-2 fl">
                    <li><a href="#">历史查询</a></li>
                    <li><a href="#">报警日志</a></li>
                    <li><a href="#">运行日志</a></li>
                </ul>
                <div class="fr toolbar">
                    <ul>
                        <li><a href="#"><i class="icon-toolbar icon-toolbar1"></i>报警设置</a></li>
                        <li><a href="#"><i class="icon-toolbar icon-toolbar2"></i>打印</a></li>
                        <li><a href="#"><i class="icon-toolbar icon-toolbar3"></i>导出</a></li>
                    </ul>
                </div>
            </div>
            <div class="bd-1">

                <div class="panes-2">
                    <div>
                        <h3 class="title-2">历史曲线</h3>
                        <div class="wrap-img">
                            <div id="highcharts" style="max-width: 100%; min-width: 690px; height: 400px; margin: 0 auto"></div>  
                        </div>
                        <hr />
                        <h3 class="title-2">历史数据</h3>
                        <table class="table table-auto">
                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>时间</th>
                                    <th>1#柜上触头A</th>
                                    <th>1#柜上触头B</th>
                                    <th>1#柜上触头C</th>
                                    <th>1#柜上触头A</th>
                                    <th>1#柜上触头A</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>2013年02月27日 15:13:32</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2013年02月27日 15:13:32</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2013年02月27日 15:13:32</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                    <td>=GetHiDat</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>时间</th>
                                    <th>报警类型</th>
                                    <th>报警信息</th>
                                    <th>是否确认</th>
                                    <th>操作人</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>                     
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>时间</th>
                                    <th>内容</th>
                                    <th>性质</th>
                                    <th>用户</th>
                                    <th>系统</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>
                                <tr>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                </tr>                     
                            </tbody>
                        </table>
                    </div>
                </div><!-- tabs2 -->   

            </div>
        </div><!-- mod-1 -->

    </div><!-- //content -->   

</div><!-- //container -->
</body>
</html>