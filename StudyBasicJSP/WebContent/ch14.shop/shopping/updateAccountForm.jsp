<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../etc/color.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제계좌를 수정하는 폼</title>
<link href="../etc/style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="<%=bodyback_c%>">
<%
String buyer = (String)session.getAttribute("id");
String accountList =request.getParameter("accountList");

if(buyer ==null){
	response.sendRedirect("shopMain.jsp");
}else{
%>
<h3><b><%=buyer %>님의 결제계좌를 수정</b></h3>
<form method="post" action="updateAccountPro.jsp">
	<table>
		<tr align="center">
			<td width="100">계좌</td>
			<td width="300"><%=accountList%></td>
		</tr>
		<tr>
			<td width="70" align="center">수정 계좌번호</td>
			<td width="200">
				<input type="text" name="update_Account">
			</td>
		</tr>
		<tr>
			<td width="70" align="center">수정 계좌은행</td>
			<td width="200">
				<input type="text" name="account_bank">
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="확인">&nbsp;
				<input type="button" value="취소" 
				onclick="javascript:window.location='buyForm.jsp'">
			</td>
		</tr>
	</table>
</form>
<%
}
%>
</body>
</html>