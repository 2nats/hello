package ch14.bookshop.shopping;

import java.sql.Timestamp;

public class BankDataBean {
	private String bank;
	private String id;
	private String account;
	private Timestamp reg_date;
	
	
	public String getBank() {
		return bank;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
}
