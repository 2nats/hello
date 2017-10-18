package ch13.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDBBean {
	
	private static BoardDBBean instance = new BoardDBBean();
	//.jsp페이지에서 DB연동빈인 BoardDBBean클래스의 메소드에 접근시 필요
	public static BoardDBBean getInstance() {
		return instance;
	}
	//private로 선언된 디폴트 생성자
	private BoardDBBean() {}
	
	// 2.번 방법 Connection Pool로부터 Connection 객체를 얻어냄
	private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/basicjsp");
		return ds.getConnection();
	}
	// 1번 방법 -JDBC드라이버 사용하는 방법
//	private Connection getConnection() throws Exception{
//		Connection conn = null;
//		String jdbcUrl="jdbc:mysql://localhost:3306/basicjsp";
//		String dbId= "jspid";
//		String dbPasswd="jsppass";
//		
//		Class.forName("com.mysql.jdbc.Driver");
//		conn = DriverManager.getConnection(jdbcUrl,dbId,dbPasswd);
//		
//		return conn;
//	}
	//board테이블에 글을 추가(insert문)<=writerPro.jsp 페이지에서 사용
	public void insertArticle(BoardDataBean article)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
		int number = 0;
		String sql = "";
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select max(num) from board");	
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				number = rs.getInt(1)+1;
			}else {
				number = 1;
			}
			if(num!=0) {
				sql ="update board set re_step = re_step+1 where ref= ? and re_step> ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, re_step);
				pstmt.executeUpdate();
				re_step=re_step+1;
				re_level=re_level+1;
				
			}else {
				ref=number;
				re_step=0;
				re_level=0;
			}
			//쿼리를 작성 
			//Oracle은 num이 오토인클라이언트로 안들어가기때문에 넣어주도록하자
			sql="insert into board(num,writer,email,subject,passwd, "
					+ "reg_date,ref,re_step,re_level,content,ip) values(?,?,?,?,?,?,?,?,?,?,?)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, number);
			pstmt.setString(2, article.getWriter());
			pstmt.setString(3, article.getEmail());
			pstmt.setString(4, article.getSubject());
			pstmt.setString(5, article.getPasswd());
			pstmt.setTimestamp(6, article.getReg_date());
			pstmt.setInt(7, ref);
			pstmt.setInt(8, re_step);
			pstmt.setInt(9, re_level);
			pstmt.setString(10,article.getContent());
			pstmt.setString(11, article.getIp());
			
			pstmt.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				
			}
		}
	}
	//board테이블에 저장된 전체 글의 수를 얻어냄(select 문)<=list.jsp에서 사용
	public int getArticleCount() throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int x=0;
		
		try {
			conn=getConnection();
			pstmt =conn.prepareStatement("select count(*) from board");
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				x=rs.getInt(1);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		return x;	
	}
	//글의 목록(복수 개의  글)을 가져옴(select문)<=list.jsp에서 사용
	public List<BoardDataBean> getArticles(int start,int end) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		List<BoardDataBean> articleList = null;
		String sql = "";
		try {
			conn = getConnection();
			//Mysql limit [시작번호,페이지번호]
			//sql="select * from board order by ref desc,re_step asc limit ?, ?";
			//pstmt= conn.prepareStatement(sql);
			
			//Oracle - rownum: 행번호  ([시작번호,끝번호],alias를 꼭 붙여서 사용)
			sql="select * from "
					+ "(select rownum as rnum, bo.* "
					+ " from (select * from board order by ref desc, re_step asc) bo) "
					+ "where rnum between ? and ?";
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				articleList=new ArrayList<BoardDataBean>(end);
				do {
					BoardDataBean article = new BoardDataBean();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPasswd(rs.getString("passwd"));
					article.setReg_date(rs.getTimestamp("reg_date"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setRe_step(rs.getInt("re_step"));
					article.setRe_level(rs.getInt("re_level"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					
					articleList.add(article);
				}while(rs.next());
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) {
				try{
					rs.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try{
					pstmt.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			if(conn!=null) {
				try{
					conn.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return articleList;
	}
	//글의 내용을 보기 (1개의 글) (select문)<=content.jsp페이지에서 사용
	public BoardDataBean getArticle(int num)throws Exception{
		Connection conn =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDataBean article = null;
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("update board set readcount = readcount+1 where num =?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("select * from board where num = ?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				article = new BoardDataBean();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPasswd(rs.getString("passwd"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("re_step"));
				article.setRe_level(rs.getInt("re_level"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				
			}
		}
		return article;
	}
	//글수정 폼에서 사용할 글의 내용(1개의 글)(select 문)<=updateForm.jsp에서 사용
	public BoardDataBean updateGetArticle(int num)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDataBean article =null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select * from board where num = ?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				article = new BoardDataBean();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPasswd(rs.getString("passwd"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("re_step"));
				article.setRe_level(rs.getInt("re_level"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		return article;
	}
	//글수정 처리에서 사용(update문)<=updatePro.jsp에서 사용
	public int updateArticle(BoardDataBean article) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		String dbpasswd="";
		String sql = "";
		int x =-1;
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select passwd from board where num = ?");
			pstmt.setInt(1, article.getNum());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				
				if(dbpasswd.equals(article.getPasswd())) {
					sql="update board set writer=?,email=?,subject=?,passwd=?,content=? where num=?";				
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getPasswd());
					pstmt.setString(5, article.getContent());
					pstmt.setInt(6, article.getNum());
					pstmt.executeUpdate();
					x=1;
					
				}else {
					x=0;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		return x;
	}
	// 글삭제처리시 사용(delete문)<=deletePro.jsp페이지에서 사용
	public int deleteArticle(int num,String passwd) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd="";
		int x=-1;
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select passwd from board where num = ?");
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("passwd");
				if(dbpasswd.equals(passwd)) {
					pstmt = conn.prepareStatement("delete from board where num = ?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();
					x=1;//글삭제 성공
					
				}else {
					x=0;//비밀번호 틀림
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) try{
				rs.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(pstmt!=null) try{
				pstmt.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			if(conn!=null) try{
				conn.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
		return x;
	}
}
