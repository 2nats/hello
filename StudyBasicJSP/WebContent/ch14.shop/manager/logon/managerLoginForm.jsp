<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
<h2>로그인 폼</h2>
<form method="post" action="managerLoginPro.jsp">
	아이디:<input type="text" name="id" maxlength="50" style="ime-mode:inactive;"><br>
	비밀번호:<input type="password" name="passwd" maxlength="16"
	style="ime-mode:inacitive;"><br>
	<input type="submit" value="로그인">
</form>
</body>
</html>