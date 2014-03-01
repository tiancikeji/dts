<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/paginator" prefix="paginator" %>

<%@ include file="../includes/head.jsp"%>


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
				<div>仪器</div>
				<div>传感器</div>
			</div>
			<!-- tabs1 -->

		</div>
		<div class="form-mini ac">
			<p>
				监测名称：<input class="ipt-txt ipt-s" type="text" />
			</p>
			<p>
				最高温度：<input class="ipt-txt ipt-s" type="text" />
			</p>
			<p>
				最低温度：<input class="ipt-txt ipt-s" type="text" />
			</p>
			<p>
				平均温度：<input class="ipt-txt ipt-s" type="text" />
			</p>
		</div>
	</div>
	<!-- //sidebar -->

	<div id="content" class="wrapper">

		<div class="mod-1">
			<div class="hd-1 cf">
				<ul id="Tabs-2" class="tabs-2 fl">
					<li><a href="#">平面视图</a></li>
					<li><a href="#">数据报表</a></li>
					<li><a href="#">报警信息</a></li>
				</ul>
				<div class="fr toolbar">
					<ul>
						<li><a href="#"><i class="icon-toolbar icon-toolbar2"></i>打印</a></li>
						<li><a href="#"><i class="icon-toolbar icon-toolbar3"></i>导出</a></li>
					</ul>
				</div>
			</div>
			<div class="bd-1">

				<div class="panes-2">
					<div>
						<div class="wrap-img">
							<c:if test="${!empty area }">
								<img id="picture" alt="图" src="<c:url value="${area}"/>" />
							</c:if>
							<c:if test="${empty area }">
								<h3>
									<a href="<c:url value="/settings/area"/>">您没插入图片，请点击重新插入</a>
								</h3>
							</c:if>
						</div>
					</div>
					<div>
						<h3 class="title-2">曲线图</h3>
						<div class="wrap-img">
							<div id="highcharts"
								style="max-width: 100%; min-width: 690px; height: 400px; margin: 0 auto"></div>
						</div>
						<hr />
						<h3 class="title-2">实时数据</h3>
						<table class="table table-auto">
							<thead>
								<tr>
									<th>序号</th>
									<th>时间</th>
									<th>S1</th>
									<th>S2</th>
									<th>长度</th>
									<th>温度</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="cable" items="${cablelist }">
									<tr>
										<td>${cable.id }</td>
										<td>${cable.created_at }</td>
										<td>${cable.signal1 }</td>
										<td>${cable.signal2 }</td>
										<td>${cable.length }</td>
										<td>${cable.temperature }</td>
										<td>查看</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					<paginator:display maxLinks="10" currPage="${page}" totalPages="${totalPages}" uri="${requestPath}?page=##" />
						
					</div>
					<div>
						<table class="table table-auto">
							<thead>
								<tr>
									<th>序号</th>
									<th>报警类型</th>
									<th>日期</th>
									<th>时间</th>
									<th>传感器</th>
									<th>传感器位号</th>
									<th>火警温度</th>
									<th>报警阔值</th>
									<th>报警类别</th>
									<th>确认</th>
									<th>操作人</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td><span class="color-red">火警</span></td>
									<td>2013-9-24</td>
									<td>14:21:34</td>
									<td>S1</td>
									<td>揽沟1回路</td>
									<td>81</td>
									<td>81</td>
									<td>紧急</td>
									<td>确认</td>
									<td>工程师</td>
								</tr>
								<tr>
									<td>1</td>
									<td><span class="color-red">火警</span></td>
									<td>2013-9-24</td>
									<td>14:21:34</td>
									<td>S1</td>
									<td>揽沟1回路</td>
									<td>81</td>
									<td>81</td>
									<td>紧急</td>
									<td>确认</td>
									<td>工程师</td>
								</tr>
								<tr>
									<td>1</td>
									<td><span class="color-red">火警</span></td>
									<td>2013-9-24</td>
									<td>14:21:34</td>
									<td>S1</td>
									<td>揽沟1回路</td>
									<td>81</td>
									<td>81</td>
									<td>紧急</td>
									<td>确认</td>
									<td>工程师</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!-- tabs2 -->

			</div>
		</div>
		<!-- mod-1 -->

	</div>
	<!-- //content -->

</div>
<!-- //container -->
</body>
<script>
	$.ajax({
		url : _requestPath+".json",
		type : "get",
		dataType : "json",
		success : init_data
	});
	
	function init_data(data) {
		if(data != null){
			var tmp=[{
					name : "title",
					data : cables
				} ,{
					name : "title",
					data : cables
				} ];
			var result = new Array();
			var mintem = -1;
			var maxtem = -1;
			var minlength=-1;
			var maxlength=-1;
			for(var key in data){
				var cables = new Array();
				
				var value = data[key];
				for (var i = 0; i < value.length; i++){
					var tmptemp = parseFloat(value[i].temperature);
					if(mintem == -1)
						mintem = tmptemp;
					if(tmptemp < mintem)
						mintem = tmptemp;
					if(tmptemp > maxtem)
						maxtem = tmptemp;
					
					var length = parseInt(value[i].length);
					if(minlength==-1)
						minlength=length;
					if(minlength > length)
						minlength=length;
					if(maxlength < length)
						maxlength=length;
						
					
					cables[i] = [ length , tmptemp ];
				}
				result[result.length]={
						name : "S"+key,
						data : cables
				};
			}
			/* for (var i = 0; i < data.length; i++) {
				var tmptemp = parseFloat(data[i].temperature);
				if(mintem == -1)
					mintem = tmptemp;
				if(tmptemp < mintem)
					mintem = tmptemp;
				if(tmptemp > maxtem)
					maxtem = tmptemp;
				
				var length = parseInt(data[i].length);
				if(minlength==-1)
					minlength=length;
				if(minlength > length)
					minlength=length;
				if(maxlength < length)
					maxlength=length;
					
				
				cables[i] = [ length , tmptemp ];
			} */
			mintem=mintem - 1;
			maxtem = maxtem + 1;
			if(mintem <= 0)
				mintem = 0;
			
			minlength=minlength - 1;
			maxlength = maxlength + 1;
			if(minlength <= 0)
				minlength = 0;
			$('#highcharts').highcharts({
				chart : {
					type : 'spline'
				},
				title : {
					text : '温度图表'
				},
				xAxis : {
					type : '长度',
					min : minlength,
					max : maxlength
				},
				yAxis : {
					title : {
						text : '温度'
					},
					min : mintem,
					max : maxtem
				},
				// tooltip: {
				// formatter: function() {
				// return '<b>'+ this.series.name +'</b><br/>'+
				// Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m';
				// }
				// },
				series : result
			});
		}
	}
</script>
</html>