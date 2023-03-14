package atm;

import java.util.Random;

public class Account {
	private Random random;
	private String accountNum;
	private String id;
	
	public Account(String id) {
		this.random = new Random();
		this.id = id;
		randomAccount();
	}
	
	private void randomAccount() {
		for(int i = 0; i < 4; i ++) {
			this.accountNum += (this.random.nextInt(9000) + 1000) + "-";
		}
	}
	
	public String getAccountNum() {
		return this.accountNum;
	}
	
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.format("%s", this.accountNum);
	}
}
