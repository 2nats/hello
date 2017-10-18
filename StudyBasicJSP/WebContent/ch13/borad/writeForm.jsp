<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../etc/color.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link href="../etc/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="<%=bodyback_c%>">
<div id="container"><img src="../images/movie.jpg"></div>
<%
int num=0,ref=1,re_step=0,re_level=0;
String strV="";
try{
	if(request.getParameter("num")!=null){
		num=Integer.parseInt(request.getParameter("num"));
		ref=Integer.parseInt(request.getParameter("ref"));
		re_step=Integer.parseInt(request.getParameter("re_step"));
		re_level=Integer.parseInt(request.getParameter("re_level"));
	}
%>
<p>
<form method="post" name="writeform" action="writePro.jsp" onsubmit="return writeSave()">
	<input type="hidden" name="num" value="<%=num %>">
	<input type="hidden" name="ref" value="<%=ref %>">
	<input type="hidden" name="re_step" value="<%=re_step %>">
	<input type="hidden" name="re_level" value="<%=re_level %>">

<table>
<tr>
	<td align="right" colspan="2" bgcolor="<%=value_c %>">
	<a href="list.jsp">글목록</a>
	</td>
</tr>
	
<tr>
	<td width="100" bgcolor="<%=value_c %>" align="center">이름</td>
	<td width="330" align="left">
	<input type="text" size="10" maxlength="10"
	name ="writer" style="ime-mode:active;"></td>
</tr>
	
<tr>
	<td width="70" bgcolor="<%=value_c%>" align="center">제목</td>
	<td width="330"  align="left">
	<%
	if(request.getParameter("num")==null){
		strV="";
	}else{
		strV="[답변]";
	}
	%>
	<input type="text" size="40" maxlength="50" name="subject"
	 value="<%=strV %>" style="ime-mode:active;"></td>
</tr>
	 
<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">Email</td>
	<td width="330" align="left">
	<input type="text" size="40" maxlength="30" name="email"
	style="ime-mode:inactive;"></td>
</tr>

<tr>
	<td width="70" bgcolor="<%=value_c %>" align="center">내용</td>
	<td width="330" align="left">
	<textarea name="content" rows="13" cols="40"
	style="ime-mode:active;"></textarea></td>
</tr>
	 
<tr>
	<td width="70" bgcolor="<%=value_c %>"  align="center">비밀번호</td>
	<td width="330" align="left">
	<input type="password" size="8" maxlength="12"
	name="passwd" style="ime-mode:inactive;">
	</td>
</tr>
	 
<tr>
	<td colspan=2 bgcolor="<%=value_c %>" align="center">
	<input type="submit" value="글쓰기">
	<input type="reset" value="다시작성">
	<input type="button" value="목록보기" OnClick="window.location='list.jsp'">
	</td>
</tr> 
</table>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>
</form>
</body>
</html>