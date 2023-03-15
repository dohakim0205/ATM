package atm;

import java.util.ArrayList;
public class UserManager {

	private static ArrayList<User> list = new ArrayList<User>();

	// Create 
	public User addUser(User user) {
		// ∞À¡ı »ƒ add 
		User check = getUserById(user.getId());
		if(check == null) {
			list.add(user);
			return user;
		}
		return null;
	}
	
	public void addUserAccount(User user, Account account) {
		int index = indexOfById(user.getId());
		list.get(index).addAccount(account);
	}
	
	// Read 
	public User getUser(int index) {
		User user = list.get(index);

		User reqObj = new User(user.getId(), user.getPassword(), user.getName(), user.getAccountList());
		return reqObj;
	}

	public User getUserById(String id) {
		User user = null;

		int index = indexOfById(id);
		if(index != -1) {
			user = getUser(index);
		}
		return user;
	}

	public int indexOfById(String id) {
		int index = -1;
		for(User user : list) {
			if(user.getId().equals(id))
				index = list.indexOf(user);
		}
		return index;
	}
	
	public int getListSize() {
		return list.size();
	}

	// Update
	public void setUser(int index, User user) {
		list.set(index, user);
	}

	// Delete 

	public void deleteUser(int index) {
		list.remove(index);
	}

	public void deleteUserById(String id) {
		// 
	}
	
	public void deleteUserAccount(User user, Account account) {
		int index = indexOfById(user.getId());
		list.get(index).removeAccount(account);
	}
}