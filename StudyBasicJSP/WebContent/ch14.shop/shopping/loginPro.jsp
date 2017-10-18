<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch14.bookshop.shopping.CustomerDBBean" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Book Shopping Mall</title>
</head>
<body>
<%request.setCharacterEncoding("utf-8"); %>
<%
String id=request.getParameter("id");
String passwd=request.getParameter("passwd");

CustomerDBBean member = CustomerDBBean.getInstance();
int check=member.userCheck(id,passwd);

if(check==1){
	session.setAttribute("id", id);
	response.sendRedirect("shopMain.jsp");
}else if(check==0) {%>
	<script>
	alert("비밀번호가 맞지 않습니다");
	history.go(-1);
	</script>
<%}else{%>
	<script>
	alert("아이디가 맞지 않습니다");
	history.go(-1);
	</script>
<%}%>
</body>
</html>