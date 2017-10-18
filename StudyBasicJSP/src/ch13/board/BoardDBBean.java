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
	//.jsp���������� DB�������� BoardDBBeanŬ������ �޼ҵ忡 ���ٽ� �ʿ�
	public static BoardDBBean getInstance() {
		return instance;
	}
	//private�� ����� ����Ʈ ������
	private BoardDBBean() {}
	
	// 2.�� ��� Connection Pool�κ��� Connection ��ü�� ��
	private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/basicjsp");
		return ds.getConnection();
	}
	// 1�� ��� -JDBC����̹� ����ϴ� ���
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
	//board���̺��� ���� �߰�(insert��)<=writerPro.jsp ���������� ���
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
			//������ �ۼ� 
			//Oracle�� num�� ������Ŭ���̾�Ʈ�� �ȵ��⶧���� �־��ֵ�������
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
	//board���̺��� ����� ��ü ���� ���� ��(select ��)<=list.jsp���� ���
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
	//���� ���(���� ����  ��)�� ������(select��)<=list.jsp���� ���
	public List<BoardDataBean> getArticles(int start,int end) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		List<BoardDataBean> articleList = null;
		String sql = "";
		try {
			conn = getConnection();
			//Mysql limit [���۹�ȣ,��������ȣ]
			//sql="select * from board order by ref desc,re_step asc limit ?, ?";
			//pstmt= conn.prepareStatement(sql);
			
			//Oracle - rownum: ���ȣ  ([���۹�ȣ,����ȣ],alias�� �� �ٿ��� ���)
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
	//���� ������ ���� (1���� ��) (select��)<=content.jsp���������� ���
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
	//�ۼ��� ������ ����� ���� ����(1���� ��)(select ��)<=updateForm.jsp���� ���
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
	//�ۼ��� ó������ ���(update��)<=updatePro.jsp���� ���
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
	// �ۻ���ó���� ���(delete��)<=deletePro.jsp���������� ���
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
					x=1;//�ۻ��� ����
					
				}else {
					x=0;//��й�ȣ Ʋ��
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