package atm;

//test
import java.util.ArrayList;

public class User {
	private String id;
	private String password;
	private String name;
	private ArrayList<Account> accs = new ArrayList<Account>();
	
	public User(User user) {
		this.id = user.id;
		this.password = user.password;
		this.name = user.name;
		this.accs = user.accs;
	}
	
	public User(String id, String password, String name) {
		this.id = id;
		this.password = password;
		this.name = name;
	}


	public User(String id, String password, String name, ArrayList<Account> accs) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.accs = accs;
	}

	public String getId() {
		return this.id;
	}

	public String getPassword() {
		return this.password;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Account> getAccs() {
		ArrayList<Account> reqObjs = new ArrayList<Account>();
		for (Account acc : this.accs) {
			String accountNum = acc.getAccountNum();
			Account reqObj = new Account(accountNum);
			reqObjs.add(reqObj);
		}
		return reqObjs;
	}

	public void set(User user) {
		this.id = user.id;
		this.password = user.password;
		this.name = user.name;
		this.accs = user.accs;
	}

	@Override
	public String toString() {
		return String.format("ID : %s, PW : %s", this.id, this.password);
	}

}
