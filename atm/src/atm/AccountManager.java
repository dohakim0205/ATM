package atm;

import java.util.ArrayList;

public class AccountManager {
	private static ArrayList<Account> list = new ArrayList<Account>();

	public void createAccount(Account acc) {
		Account account = new Account(acc);
		list.add(account);
	}

	public void deleteAccount(int index) {
		list.remove(index);
	}

	public Account getAccount(int index) {
		Account acc = list.get(index);
		String accountNum = acc.getAccountNum();
		Account reqObj = new Account(accountNum);
		return reqObj;
	}

	public void setAccount(int index, Account account) {
		list.set(index, account);
	}
	
	public ArrayList<Account> getList(){
		ArrayList<Account> reqObj = new ArrayList<Account>();
		for(int i = 0; i < list.size(); i ++) {
			String accountNum = list.get(i).getAccountNum();
			Account acc = new Account(accountNum);
			reqObj.add(acc);
		}
		
		return reqObj;
	}

	/*
	 * Account¿¡ ´ëÇÑ Create Read Update Delete
	 */
}
