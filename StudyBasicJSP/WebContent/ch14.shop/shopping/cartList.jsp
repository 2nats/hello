<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch14.bookshop.shopping.CartDataBean" %>
<%@ page import="ch14.bookshop.shopping.CartDBBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ include file="../etc/color.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Book shopping Mall</title>
<link href="../etc/style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="<%=bodyback_c%>">
<%
String book_kind = request.getParameter("book_kind");
String buyer =(String)session.getAttribute("id");

List<CartDataBean> cartLists =null;
CartDataBean cartList = null;
int count = 0;
int number=0;
int total=0;

if(session.getAttribute("id")==null){
	response.sendRedirect("shopMain.jsp");
}else{
	CartDBBean bookProcess=CartDBBean.getInstance();
	count = bookProcess.getListCount(buyer);
	
	if(count ==0){
%>
	<h3><b>장바구니</b></h3>
	<table>
		<tr><td><font color="red">장바구니에 담긴 물품이 없습니다.</font></td></tr>
	</table>
	<input type="button" value="쇼핑계속"
	onclick="javascript:window.location='list.jsp?book_kind=<%=book_kind %>'">
<%
	}else{
		cartLists=bookProcess.getCart(buyer);
%>	
	<h3><b>장바구니</b></h3>
	<table>
		<tr>
		<td width="50">번호</td>
		<td width="300">책이름</td>
		<td width="100">판매가격</td>
		<td width="150">수량</td>
		<td width="150">금액</td>
	</tr>
<%
	for(int i=0;i<cartLists.size();i++){
		cartList = (CartDataBean)cartLists.get(i);
%>
	<tr>
		<td width="50"><%=++number %></td>
		<td width="300" align="left">
		<img src="../../imageFile/<%=cartList.getBook_image()%>"
		border="0" width="30" height="50" align="middle">
		<%=cartList.getBook_title() %>
		</td>
		<td width="100"><%=NumberFormat.getInstance().format(cartList.getBuy_price()) %></td>
		<td width="150">
			<form method="post" action="updateCart.jsp">
				<input type="text" name="buy_count" size="5" value="<%=cartList.getBuy_count() %>">
				<input type="hidden" name="cart_id" value="<%=cartList.getCart_id() %>">
				<input type="hidden" name="book_kind" value="<%=book_kind%>">
				<input type="submit" value="수정">
			</form>
		</td>
		<td align="center" width="150">
			<%total+=cartList.getBuy_count()*cartList.getBuy_price(); %>
			<%=NumberFormat.getInstance().format(cartList.getBuy_count()*cartList.getBuy_price()) %>
			
			<input type="button" value="삭제" 
			onclick="javascript:window.location='cartListDel.jsp?list=<%=cartList.getCart_id() %>&book_kind=<%=book_kind %>'">
		</td>
	</tr>
	<%} %>
	<tr>
		<td colspan="5" align="right" style="padding-right:30px">
		<font color="blue"><b>총금액:<%=NumberFormat.getInstance().format(total) %></b></font>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<input type="button" value="구매하기"
			onclick="javascript:window.location='buyForm.jsp'">
			<input type="button" value="쇼핑계속"
			onclick="javascript:window.location='list.jsp?book_kind=<%=book_kind %>'">&nbsp;
			<input type="button" value="장바구니 비우기"
			onclick="javascript:window.location='cartListDel.jsp?list=all&book_kind=<%=book_kind%>'">
		</td>
	</tr>
	</table>
	<%
	}
	
}
	%>
</body>
</html>