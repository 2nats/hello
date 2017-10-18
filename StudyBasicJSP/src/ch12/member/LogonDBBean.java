package ch12.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class LogonDBBean {
	private static LogonDBBean instance = new LogonDBBean();
	
	public static LogonDBBean getInstance() {
		return instance;
	}
	private LogonDBBean() {
		
	}
	private Connection getConnection() throws Exception{
		Context initCtx=new InitialContext();
		Context envCtx=(Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/basicjsp");
		return ds.getConnection();
	}
	public void insertMember(LogonDataBean member) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt =null;
		try {
			conn=getConnection();
			pstmt=(PreparedStatement) conn.prepareStatement("insert into MEMBER values(?,?,?,?)");
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setTimestamp(4, member.getReg_date());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	public int userCheck(String id,String passwd) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String dbpasswd="";
		ResultSet rs =null;
		int x =-1;
		
		try {
			conn=getConnection();
			pstmt=conn.prepareStatement("select passwd from MEMBER where id=?");
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd=rs.getString("passwd");
				if(dbpasswd.equals(passwd)) {
					x=1;
				}else {
					x=0;
				}
			}else {
				x=-1;
			}
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	return x;
	}
}
