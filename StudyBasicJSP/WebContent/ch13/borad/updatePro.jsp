<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch13.board.BoardDBBean" %>
<%@ page import="java.sql.Timestamp"%>
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
String pageNum=request.getParameter("pageNum");

BoardDBBean dbPro= BoardDBBean.getInstance();
int check=dbPro.updateArticle(article);

if(check==1){
	response.sendRedirect("list.jsp?pageNum="+pageNum);
}%>
<script type="text/javascript">
alert("비밀번호가 맞지 않습니다.");
history.go(-1);
</script>

</body>
</html>