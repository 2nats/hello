package ch14.bookshop.shopping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class BankDBBean {
	private static BankDBBean instance = new BankDBBean();
	
	public static BankDBBean getInstance() {
		return instance;
	}
	
	private BankDBBean() {}
	
	public Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/basicjsp");
		
		return ds.getConnection(); // 커넥션 풀에서 커넥션 객체를 얻는 방법
	}
	
	public void insertAccount(BankDataBean member) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		
		try {
			conn = getConnection();
			sql = "insert into bank (id,account,bank,reg_date) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getAccount());
			pstmt.setString(3, member.getBank());
			pstmt.setTimestamp(4, member.getReg_date());
			
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {ex.printStackTrace();}
			if(conn != null) try {conn.close();}catch(SQLException ex) {ex.printStackTrace();}
		}
	}
	
	public void updateAccount(BankDataBean member, String account) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		
		try {
			conn = getConnection();
			
			sql="update bank set account=?, bank=?, reg_date=? "
					+ "where id=? and account=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getAccount());
			pstmt.setString(2, member.getBank());
			pstmt.setTimestamp(3, member.getReg_date());
			pstmt.setString(4, member.getId());
			pstmt.setString(5, account);
			pstmt.executeUpdate();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(pstmt != null) try {pstmt.close();} catch(SQLException ex) {ex.printStackTrace();}
			if(conn != null) try {conn.close();} catch(SQLException ex) {ex.printStackTrace();}
		}
	}
	
	// bank테이블에 있는 전체 레코드를 얻어내는 메소드
    public List<String> getAccount(String id) throws Exception{
    	Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> accountList = null;
        String sql = "";
        try {
            conn = getConnection();
                        
            sql = "select account, bank, name from "
            		+ "member m inner join bank b on m.id = b.id "
            		+ "where b.id = ? "
            		+ "order by b.reg_date desc";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            
            accountList = new ArrayList<String>();
            
            while (rs.next()) {
           	  String account = new String(rs.getString("account")+" "
                     + rs.getString("bank")+" "+rs.getString("name"));
           	  accountList.add(account);
		    }
        }catch(Exception ex) {
        	ex.printStackTrace();
        } finally {
            if (pstmt != null) 
            	try { pstmt.close(); } catch(SQLException ex) {}
            if (conn != null) 
            	try { conn.close(); } catch(SQLException ex) {}
        }
        return accountList;
    }
    
    public void deleteAccount(String id, String account)throws Exception {
    	Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "";
        
        try {
        	conn = getConnection();
        	
        	sql = "delete from bank where id=? and account=?";
        	pstmt = conn.prepareStatement(sql);
        	pstmt.setString(1, id);
        	pstmt.setString(2, account);
        	pstmt.executeUpdate();
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
        	if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {ex.printStackTrace();}
        	if(conn != null) try {conn.close();}catch(SQLException ex) {ex.printStackTrace();}
        }
    }

}
