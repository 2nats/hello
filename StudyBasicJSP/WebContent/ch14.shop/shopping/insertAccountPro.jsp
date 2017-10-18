<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch14.bookshop.shopping.BankDBBean" %>
<%@ page import="ch14.bookshop.shopping.BankDataBean" %>
<%@ page import="java.sql.Timestamp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제계좌 등록처리</title>
</head>
<body>
<%request.setCharacterEncoding("utf-8"); %>
<%
String buyer = (String)session.getAttribute("id");
String account = request.getParameter("account");
String bank = request.getParameter("bank");

BankDBBean bankProcess  =  BankDBBean.getInstance();
BankDataBean member = new BankDataBean();
member.setId(buyer);
member.setAccount(account);
member.setBank(bank);
member.setReg_date(new Timestamp(System.currentTimeMillis()));
bankProcess.insertAccount(member);

response.sendRedirect("buyForm.jsp");
%>

</body>
</html>