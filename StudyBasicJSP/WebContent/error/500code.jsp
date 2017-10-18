<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>500 에러 페이지</title>
</head>
<body>
<% response.setStatus(HttpServletResponse.SC_OK); %>

서비스 사용에 불편을 끼쳐드려서 대단히 죄송합니다.<br>
빠른 시간 내에 문제를 처리하겠습니다.<br>
</body>
</html>