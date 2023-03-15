package atm;
import java.util.ArrayList;
import java.util.Random;

public class AccountManager {

	private static ArrayList<Account> list = new ArrayList<Account>();

	// Account ø° ¥Î«— 

	// Create 
	public Account createAccount(Account account) {
		String accountNum = accNumGenerator();
		account.setAccNum(accountNum);
		list.add(account);
		return account;
	}
	
	public void addAccount(Account account) {
		list.add(account);
	}

	// Read 
	public Account getAccount(int index) {
		Account account = list.get(index);

		Account reqObj = new Account(account.getUserId(), account.getAccNum(), account.getMoney());;
		return reqObj;
	}

	public Account getAccountByNum(String accountNum) {
		Account account = null;

		for(Account object : list) {
			if(object.getAccNum().equals(accountNum))
				account = object;
		}

		return account;
	}
	
	public int indexOfByAccNum(String accNum) {
		int index = -1;
		for(Account account : list) {
			if(account.getAccNum().equals(accNum))
				index = list.indexOf(account);
		}
		return index;
	}
	
	public int getListSize() {
		return list.size();
	}

	// Update
	public void setAccount(int index, Account account) {
		list.set(index, account);
	}
	
	// Delete 
	public void deleteAccount(int index) {
		list.remove(index);
	}
	
	public void deleteAccount(Account account) {
		list.remove(account);
	}
	
	private String accNumGenerator() {
		String num = "";

		Random ran = new Random();
		while(true) {
			int first = ran.nextInt(8999) + 1000;
			int second = ran.nextInt(8999) + 1000;

			num = first + "-" + second;

			Account account = getAccountByNum(num);

			if(account == null)
				break;
		}

		return num;
	}
}