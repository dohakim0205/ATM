package atm;

public class Account {

	public static int LIMIT = 3;

	private String userId;
	private String accNum;
	private int money;

	public Account(String userId) {
		this.userId = userId;
	}

	public Account(String userId, String accNum, int money) {
		this.userId = userId;
		this.accNum = accNum;
		this.money = money;
	}

	public String getUserId() {
		return userId;
	}

	public String getAccNum() {
		return accNum;
	}
	
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return String.format("%s : %s, %d¿ø", this.userId, this.accNum, this.money);
	}

}
