package atm;

import java.util.ArrayList;

public class UserManager {
	private static ArrayList<User> list = new ArrayList<User>();

	public static ArrayList<User> getList() {
		ArrayList<User> reqObj = new ArrayList<User>();
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			String id = user.getId();
			String password = user.getPassword();
			String name = user.getName();
			ArrayList<Account> accs = user.getAccs();

			User newUser = new User(id, password, name, accs);
			reqObj.add(newUser);
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

	public User getUser(int index) {
		User user = list.get(index);
		User reqObj = new User(user);

		return reqObj;
	}

	public User getUserById(String id) {
		int index = findUserIndex(id);
		return getUser(index);
	}

	public void setUser(int index, User user) {
		list.set(index, user);
	}

	private int findUserIndex(String id) {
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			User user = list.get(i);
			if (user.getId().equals(id)) {
				index = i;
			}
		}

		return index;
	}
}
