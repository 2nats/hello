<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch13.board.BoardDBBean" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%request.setCharacterEncoding("utf-8");%>
<%
int num=Integer.parseInt(request.getParameter("num"));
String pageNum=request.getParameter("pageNum");
String passwd = request.getParameter("passwd");

BoardDBBean dbPro=BoardDBBean.getInstance();
int check =dbPro.deleteArticle(num,passwd);

if(check==1){
	response.sendRedirect("list.jsp?pageNum"+pageNum);
}
%>
<script>
alert("비밀번호가 맞지 않습니다.");
history.go(-1);
</script>

</body>
</html>