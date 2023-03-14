package atm;

import java.util.ArrayList;

public class AccountManager {

	private static ArrayList<Account> list = new ArrayList<Account>();

	public void createAccount(String id) {
		Account account = new Account(id);
		list.add(account);
	}

	public void deleteAccount(int index) {
		list.remove(index);
	}

	public void deleteAccountById(String id) {
		int index = findUserIndex(id);
		deleteAccount(index);
	}

	public Account getAccount(int index) {
		Account acc = list.get(index);
		String id = acc.getId();
		String accountNum = acc.getAccountNum();
		Account reqObj = new Account(id);
		reqObj.setAccountNum(accountNum);

		return reqObj;
	}

	public Account getAccountById(String id) {
		int index = findUserIndex(id);
		return getAccount(index);
	}

	public void updateAccount(int index, Account account) {
		list.set(index, account);
	}

	private int findUserIndex(String id) {
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			Account temp = list.get(i);
			if (temp.getId().equals(id)) {
				index = i;
			}
		}

		return index;
	}

	/*
	 * Account¿¡ ´ëÇÑ Create Read Update Delete
	 */
}
