package atm;

import java.util.ArrayList;

public class UserManager {

	private static ArrayList<User> list = new ArrayList<User>();
	
	public ArrayList<User> getList(){
		ArrayList<User> reqObj = new ArrayList<User>();
		for(int i = 0; i < list.size(); i ++) {
			String id = list.get(i).getId();
			String password = list.get(i).getPassword();
		}
		
	}

	public void createUser(User user) {
		User temp = new User(user);
		list.add(temp);
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
