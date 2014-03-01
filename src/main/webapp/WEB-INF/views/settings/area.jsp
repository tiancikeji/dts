<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ include file="../includes/head.jsp"%>

<div id="container" class="grid-elastic cf">

	<div id="sidebar" class="grid-220">
		<div class="menu">
			<h3>参数设置</h3>
			<div class="menu-content">
				<ul>
					<li><a href="<c:url value="/settings/sensor" />">传感器设置</a></li>
					<li class="mactive"><a href="<c:url value="/settings/area" />">厂区设置</a></li>
					<li><a href="<c:url value="/settings/alert" />">分区设置</a></li>
					<li><a href="<c:url value="/settings/" />">通讯设置</a></li>
					<li><a href="<c:url value="/settings/system" />">系统设置</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!-- //sidebar -->

	<div id="content" class="wrapper">
		<h3>${status}</h3>
		<div class="mod-1">
			<div class="hd-1 cf">
				<h2>厂区设置</h2>
			</div>
			<div class="bd-1">

				<div class="mod-2">
					<div class="hd-2 cf">
						<h2>厂区设置</h2>
					</div>
					<div class="bd-2">
						<div class="wrap-cont">
							<div class="wrap-cont">
								<form action="<c:url value="/settings/area"/>" method="post"  enctype="multipart/form-data">
									<ul class="form-list">
										<li><label for="radio"> 上级：</label> <select
											name="pId" class="select-2 ipt-f3">
												<option value="0">无</option>
												<c:forEach var="area" items="${areaList}">
													<option value="${area.id }">${area.name }</option>
												</c:forEach>
										</select></li>
										<li><label for="radio">名称：</label>
											<input name="name" value="" />
										</li>
										<li><label for="radio">背景图片：</label><input type="file" name="file"  value="上传文件" /></li>
										<li>
										<br />
										<input type="button" class="btn btn-blue" id="b1" value="添加通道" onclick="add()" />
			                           	<table class="table table-auto" id="addtr">
											<tr>
												<td>通道</td>
												<td colspan="2">范围</td>
												<td>操作</td>
											</tr>
											<tr>
												<td>
													<select name="channelids" class="select-2 ipt-f3">
														<c:forEach var="channelI" items="${channels}">
															<option value="${channelI.channel}">${channelI.channel}</option>
														</c:forEach>
													</select>
												</td>
												<td><input name="scopestarts" style=" width:74px" value="" /></td>
												<td><input name="scopeends" style=" width:74px" value="" /></td>
												<td><a href="javascript:void(0);" onclick="deleteRow(this)">删除</a></td>
											</tr>
										</table>
										<li><input class="btn btn-blue" type="submit" value="保存" />
										</li>
									</ul>
								</form>
							</div>

						</div>
						<div class="btn-wrap">
							<table class="table table-auto">
								<tr>
									<td>名次</td>
									<td>父级</td>
									<td>背景</td>
									<td>范围</td>
									<td>创建时间</td>
									<td>操作</td>
								</tr>
								<c:forEach var="area" items="${areaList}">
									<tr>
										<td>${area.name}</td>
										<td>${area.pId }</td>
										<td>${area.background }</td>
										<td>
											<c:forEach var="areaChannel" items="${area.areaChannels}">
												${areaChannel.channelId}: ${areaChannel.scopeStart} - ${areaChannel.scopeEnd}
												<br />
											</c:forEach>
										</td>
										<td>${area.created_at}</td>
										<td><a href="<c:url value="/settings/area/delete/${area.id }"/>">删除</a></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
				<!-- mod-2 -->





			</div>
		</div>
		<!-- mod-1 -->

	</div>
	<!-- //content -->

</div>
<!-- //container -->
</body>
<script>
	function add(){
		 var oTr = document.getElementById("addtr").rows[1];
		 var newTr = oTr.cloneNode(true);
		 document.getElementById("addtr").getElementsByTagName("tbody")[0].appendChild(newTr);
		 //newTr.cells[0].firstChild.value = newTr.rowIndex;
		 document.getElementById("b1").disabled = newTr.rowIndex == 10;
	}
	
	function deleteRow(obj){
		document.getElementById("addtr").getElementsByTagName("tbody")[0].removeChild(document.getElementById("addtr").rows[obj.parentNode.parentNode.rowIndex]);
	}
</script>
</html>