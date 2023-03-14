package atm;

public class User {
	private String id;
	private String password;
	private String name;

	public User(User user) {
		this.id = user.id;
		this.password = user.password;
		this.name = user.name;
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
	
	public void set(User user) {
		this.id = user.id;
		this.password = user.password;
		this.name = user.name;
	}
	@Override
	public String toString() {
		return String.format("ID : %s, PW : %s", this.id, this.password);
	}
	
}
