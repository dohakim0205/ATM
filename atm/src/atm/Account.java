package atm;

import java.util.Random;

public class Account {
	private Random random;
	private String accountNum;

	public Account() {
		this.random = new Random();
		this.accountNum = "";
		randomAccount();
	}

	public Account(String accountNum) {
		this.accountNum = accountNum;
	}

	public Account(Account acc) {
		this.accountNum = acc.accountNum;
	}

	private void randomAccount() {
		for (int i = 0; i < 4; i++) {
			this.accountNum += (this.random.nextInt(9000) + 1000);
			if (i < 3) {
				this.accountNum += "-";
			}
		}
	}

	public String getAccountNum() {
		return this.accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	@Override
	public String toString() {
		return String.format("%s", this.accountNum);
	}
}
