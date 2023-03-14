package atm;

import java.util.ArrayList;

public class UserManager {
	private AccountManager userAM = new AccountManager();
	private static ArrayList<User> list;

	public ArrayList<User> getList() {
		ArrayList<User> reqObj = new ArrayList<User>();
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i).getId();
			String password = list.get(i).getPassword();
			String name = list.get(i).getName();
			ArrayList<Account> accs = getAccList(list.get(i));
			User user = new User(id, password, name, accs);
			reqObj.add(user);
		}

		return reqObj;
	}

	public ArrayList<Account> getAccList(User user) {
		ArrayList<Account> reqObj = new ArrayList<Account>();
		for (int i = 0; i < user.getAccs().size(); i++) {
			String accountNum = user.getAccs().get(i).getAccountNum();
			Account acc = new Account(accountNum);
			reqObj.add(acc);
		}
		return reqObj;
	}
	
	public void createUser(User user) {
		User newUser = new User(user);
		list.add(newUser);
	}

	public void deleteUser(int index) {
		list.remove(index);
	}

	public void deleteUserById(String id) {
		int index = findUserIndex(id);
		deleteUser(index);
	}

//	private boolean indexCheck(int index) {
//		if (index >= 0 && index < list.size()) {
//			return true;
//		}
//
//		return false;
//	}

	public User getUser(int index) {
		User user = list.get(index);
		User reqObj = new User(user);

		return reqObj;
	}

	public User getUserById(String id) {
		int index = findUserIndex(id);
		return getUser(index);
	}

	public void updateUser(int index, User user) {
		list.set(index, user);
	}

	private int findUserIndex(String id) {
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			User temp = list.get(i);
			if (temp.getId().equals(id)) {
				index = i;
			}
		}

		return index;
	}

	/*
	 * °èÁÂ¿Í À¯Àú´Â Ä¸½¶È­
	 * 
	 * 
	 * User¿¡ ´ëÇÑ Create Read Update Delete
	 */
}
