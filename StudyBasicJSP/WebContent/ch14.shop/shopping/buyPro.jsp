<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch14.bookshop.shopping.CartDataBean" %>
<%@ page import="ch14.bookshop.shopping.CartDBBean" %>
<%@ page import="ch14.bookshop.shopping.BuyDBBean" %>
<%@ page import="ch14.bookshop.master.ShopBookDBBean" %>

<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<% request.setCharacterEncoding("utf-8");%>

<%
String account = request.getParameter("account");
String deliveryName=request.getParameter("deliveryName");
String deliveryTel=request.getParameter("deliveryTel");
String deliveryAddress =request.getParameter("deliveryAddress");
String buyer =(String)session.getAttribute("id");

CartDBBean cartProcess =CartDBBean.getInstance();
List<CartDataBean> cartLists =cartProcess.getCart(buyer);

BuyDBBean  buyProcess = BuyDBBean.getInstance();
buyProcess.insertBuy(cartLists,buyer,account,deliveryName,deliveryTel,deliveryAddress);

response.sendRedirect("buyList.jsp");
 %>

</body>
</html>