<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
 <form name="form1" action="update" method="POST">
    <table width="300" border="1">
        <tr>
            <td colspan="2">修改窗口</td>
        </tr>
        <tr>
                <td>用户名</td>
                <td><input type="text" value="${user1.name} "name="name" size="10"></td>
        </tr>
        <tr>
                <td>密码</td>
                <td><input type="text" name="password" size="10" value="${user1.password} "></td>
                <td><input type="hidden" name="id" value="${user1.id}"/></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" name="submit" value="提交">
        </tr>
    </table>
    </form>
</body>
</html>