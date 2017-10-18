<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="ch14.bookshop.master.ShopBookDBBean" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%request.setCharacterEncoding("utf-8"); %>
<%
String realFolder="";
String filename="";
MultipartRequest imageUp =null;

String saveFolder = "/imageFile";
String encType="utf-8";
int maxSize=2*1024*1024;

ServletContext context =getServletContext();
realFolder =context.getRealPath(saveFolder);

try{
	imageUp=new MultipartRequest(request,realFolder,maxSize,encType,new DefaultFileRenamePolicy());
	Enumeration<?> files = imageUp.getFileNames();
	while(files.hasMoreElements()){
		String name=(String)files.nextElement();
		
		filename = imageUp.getFilesystemName(name);
	}
}catch(IOException ioe){
	System.out.println(ioe);
}catch(Exception ex){
	System.out.println(ex);
}
%>
<jsp:useBean id="book" scope="page" class="ch14.bookshop.master.ShopBookDataBean">
</jsp:useBean>

<%
int book_id= Integer.parseInt(imageUp.getParameter("book_id"));
String book_kind=imageUp.getParameter("book_kind");
String book_title=imageUp.getParameter("book_title");
String book_price=imageUp.getParameter("book_price");
String book_count=imageUp.getParameter("book_count");
String author = imageUp.getParameter("author");
String publishing_com=imageUp.getParameter("publishing_com");
String book_content=imageUp.getParameter("book_content");
String discount_rate = imageUp.getParameter("discount_rate");

String year =imageUp.getParameter("publishing_year");
String month=(imageUp.getParameter("publishing_month").length()==1)?
		"0"+imageUp.getParameter("publishing_month"):
			imageUp.getParameter("publishing_month");
String day=(imageUp.getParameter("publishing_day").length()==1)?
		"0"+imageUp.getParameter("publishing_day"):
			imageUp.getParameter("publishing_day");
book.setBook_kind(book_kind);
book.setBook_title(book_title);
book.setBook_price(Integer.parseInt(book_price));
book.setBook_count(Short.parseShort(book_count));
book.setAuthor(author);
book.setPublishing_com(publishing_com);
book.setPublishing_date(year+"-"+month+"-"+day);
book.setBook_image(filename);
book.setBook_content(book_content);
book.setDiscount_rate(Byte.parseByte(discount_rate));
book.setReg_date(new Timestamp(System.currentTimeMillis()));

ShopBookDBBean bookProcess = ShopBookDBBean.getInstance();
bookProcess.updateBook(book, book_id);

response.sendRedirect("bookList.jsp?book_kind="+book_kind);
%>
</body>
</html>