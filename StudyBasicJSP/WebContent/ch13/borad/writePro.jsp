<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="ch13.board.BoardDBBean" %>
<%@ page import ="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%request.setCharacterEncoding("utf-8"); %>
<jsp:useBean id="article" scope="page" class="ch13.board.BoardDataBean">
<jsp:setProperty name="article" property="*"/>
</jsp:useBean>
<%
article.setReg_date(new Timestamp(System.currentTimeMillis()));
article.setIp(request.getRemoteAddr());

BoardDBBean dbPro=BoardDBBean.getInstance();
dbPro.insertArticle(article);

response.sendRedirect("list.jsp");
%>
</body>
</html>