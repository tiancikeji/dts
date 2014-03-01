<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<c:url value="/assets/js/jquery/jquery.js"/>"
	type="text/javascript"></script>
<title>Insert title here</title>
</head>
<script type="text/javascript">
	var checkBoxOnClick = function(){
    	var chk_value = [];
		var str = "";
		$('input[name="checkbox"]:checked').each(function() {
			//push赋值
			chk_value.push($(this).val());
		});
		for (var i = 0; i < chk_value.length; i++) {
			str += chk_value[i] + ",";
		}
		alert(str);
		window.location.href = "deleteall?str=" + str;
    }
    $('input[name="checkbox"]:checked').on('click',checkBoxOnClick);
    
	function deleteall() {
		

	}
	function selectAll(name) {
		$("[name='" + name + "']").attr("checked", 'true');//全选 ,name为input标签name属性值
	}
	function selectNone(name) {
		$("[name='" + name + "']").removeAttr("checked");//取消全选  
	}
</script>
<body>
	<div>
		<h3 class="title-2">实时数据</h3>
当前登录用户：${sessionScope.sessionname }
<br /> 
		<table class="table table-auto">
			<thead>
				<tr>
					<th></th>
					<th>编号</th>
					<th>日期</th>
					<th>密码</th>
					<th>名字</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${UserList}">
					<tr>
						<td><input name="checkbox" type="checkbox" value="${user.id}">
						</td>
						<td>${user.id }</td>
						<td>${user.created_at }</td>
						<td>${user.password }</td>
						<td>${user.name }</td>
						<td><a href="del?id=${user.id }">删除</a> <a
							href="upda?id=${user.id }">修改</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="button" id="btn1" value="全选"
			onclick="selectAll('checkbox');"> <input type="button"
			id="btn2" value="取消全选" onclick="selectNone('checkbox');"> <input
			type="button" class="delete_all" value="批量删除" onclick="checkBoxOnClick();" />
	</div>
</body>



</html>