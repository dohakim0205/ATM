package atm;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {
	private Scanner scan;
	private String name;
	private UserManager um;
	private AccountManager am;
	private int log;
	private String adminId = "admin";
	private String adminPassword = "admin";
	private static final int JOIN_USER = 1;
	private static final int DELETE_USER = 2;
	private static final int CREATE_ACC = 3;
	private static final int DELETE_ACC = 4;
	private static final int LOG_IN = 5;
	private static final int LOG_OUT = 6;
	private static final int EXIT = 7;
	private static final int ADMIN = 0;

	public Bank(String name) {
		this.scan = new Scanner(System.in);
		this.um = new UserManager();
		this.am = new AccountManager();
		this.name = name;
		this.log = -1;
		this.adminId = "admin";
		this.adminPassword = "admin";
	}

	private int inputInt(String message) {
		while (true) {
			System.out.println(message + " 입력하세요");
			int num = -1;
			try {
				num = this.scan.nextInt();
				return num;
			} catch (InputMismatchException e) {
				System.err.println("숫자를 입력하시기 바랍니다");
				scan.nextLine();
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " 입력하세요");
		String str = this.scan.next();
		return str;
	}

	private void printMenu() {
		System.out.println("=====" + this.name + "=====");
		System.out.println("1. 회원 가입");
		System.out.println("2. 회원 탈퇴");
		System.out.println("3. 계좌 개설");
		System.out.println("4. 계좌 삭제");
		System.out.println("5. 로그인");
		System.out.println("6. 로그아웃");
		System.out.println("7. 종료");
		System.out.println("0. 관리자 모드");
	}

	private int inputMenu() {
		while (true) {
			int menu = inputInt("메뉴를");
			if (menu < ADMIN || menu > EXIT) {
				System.out.println("존재하지 않는 메뉴입니다");
				continue;
			}
			return menu;
		}
	}

	private boolean selectMenu() {
		isLoggedInUser();
		printMenu();
		int selectedMenu = inputMenu();

		if (selectedMenu == JOIN_USER) {
			joinUser();
		}

		else if (selectedMenu == DELETE_USER) {
			deleteUser();
		}

		else if (selectedMenu == CREATE_ACC) {
			createUserAccount();
		}

		else if (selectedMenu == DELETE_ACC) {
			deleteUserAccount();
		}

		else if (selectedMenu == LOG_IN) {
			logIn();
		}

		else if (selectedMenu == LOG_OUT) {
			logOut();
		}

		else if (selectedMenu == EXIT) {
			System.out.println("*ATM을 종료합니다*");
			return false;
		}

		else if (selectedMenu == ADMIN) {
			adminMode();
		}

		return true;
	}

	private void adminMode() {
		if (adminLogIn()) {
			selectAdminMenu();
		}
	}

	private void selectAdminMenu() {
		while (true) {
			printAdminMenu();
			int adminMenu = inputInt("메뉴 번호를");
			if (adminMenu < 1 || adminMenu > 4) {
				System.out.println("존재하지 않는 메뉴입니다");
				continue;
			}

			if (adminMenu == 1) {
				printAllUsers();
			}

			else if (adminMenu == 2) {
				printAllAccounts();
			}

			else if (adminMenu == 3) {
				printselectedUser();
			}

			else if (adminMenu == 4) {
				System.out.println("*관리자 모드 종료*");
				break;
			}
		}
	}

	private void printselectedUser() {
		String id = inputString("회원의 아이디를");
		int index = checkExistId(id);

		if (index != -1) {
			User user = this.um.getUserById(id);
			System.out.println(user.toString());
		}

		else {
			System.out.println("존재하지 않는 아이디입니다");
		}
	}

	private void printAllAccounts() {
		if (AccountManager.getList().size() == 0) {
			System.out.println("존재하는 회원이 없습니다");
			return;
		}

		for (Account acc : AccountManager.getList()) {
			System.out.println(acc.toString());
		}
	}

	private void printAllUsers() {
		if (UserManager.getList().size() == 0) {
			System.out.println("존재하는 회원이 없습니다");
			return;
		}

		for (User user : UserManager.getList()) {
			System.out.println(user.toString());
		}
	}

	private boolean adminLogIn() {
		String id = inputString("(ID : admin)관리자 아이디를");
		String password = inputString("(PW : admin)관리자 비밀번호를");
		if (id.equals(this.adminId) && password.equals(this.adminPassword)) {
			System.out.println("관리자 로그인 되었습니다");
			return true;
		}

		System.out.println("관리자 아이디 혹은 비밀번호가 올바르지 않습니다");
		return false;
	}

	private void printAdminMenu() {
		System.out.println("===== 관리자 모드 =====");
		System.out.println("1. 전체 회원 조회");
		System.out.println("2. 전체 계정 조회");
		System.out.println("3. 선택 회원 조회");
		System.out.println("4. 관리자 모드 종료");
	}

	private void isLoggedInUser() {
		if (logInCheck()) {
			User user = this.um.getUser(this.log);
			System.out.printf("안녕하세요 %s님!\n", user.getName());
		}
	}

	private void logIn() {
		if (logInCheck()) {
			System.out.println("이미 로그인 되어 있습니다");
			return;
		}

		String id = inputString("아이디를");
		String password = inputString("비밀번호를");
		int index = checkExistId(id);
		if (index != -1 && passwordCheck(index, password)) {
			this.log = index;
			User user = this.um.getUser(index);
			System.out.printf("%s님 로그인 되었습니다\n", user.getName());
		}

		else {
			System.out.println("회원 정보가 올바르지 않습니다");
		}

	}

	private void logOut() {
		if (!logInCheck()) {
			System.out.println("이미 로그아웃 되어 있습니다");
			return;
		}

		this.log = -1;
		System.out.println("로그아웃 되었습니다");
	}

	private boolean limitAccsCountCheck() {
		User user = this.um.getUser(this.log);
		int count = user.getAccs().size();
		if (count == 3) {
			return true;
		}

		return false;
	}

	private int findAccountIndex(String accountNum) {
		int index = -1;
		for (int i = 0; i < AccountManager.getList().size(); i++) {
			Account acc = AccountManager.getList().get(i);
			if (accountNum.equals(acc.getAccountNum())) {
				index = i;
			}
		}

		return index;
	}

	private void updateUserAccount(User user, String accountNum) {
		ArrayList<Account> temp = user.getAccs();
		for (int i = 0; i < temp.size(); i++) {
			Account acc = temp.get(i);
			if (acc.getAccountNum().equals(accountNum)) {
				temp.remove(i);
			}
		}

		String id = user.getId();
		String password = user.getPassword();
		String name = user.getName();

		user = new User(id, password, name, temp);
		this.um.setUser(this.log, user);
	}

	private void deleteUserAccount() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		User user = this.um.getUser(this.log);
		System.out.println("=== 보유 계좌 목록 ===");
		System.out.println(user.getAccs().toString());
		String accountNum = inputString("삭제할 계좌번호를");

		int index = findAccountIndex(accountNum);

		if (index != -1) {
			updateUserAccount(user, accountNum);
			this.am.deleteAccount(index);
			System.out.println("계좌가 삭제되었습니다");
		}

		else {
			System.out.println("계좌번호가 올바르지 않습니다");
		}
	}

	private void createUserAccount() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		if (limitAccsCountCheck()) {
			System.out.println("3개 이상 계좌를 개설할 수 없습니다");
			return;
		}

		User user = this.um.getUser(this.log);
		Account acc = addUserAccount(user);
		this.am.createAccount(acc);
		System.out.printf("%s님, 계좌가 개설되었습니다\n계좌번호 : %s\n", user.getName(), acc.getAccountNum());
	}

	private Account addUserAccount(User user) {
		ArrayList<Account> temp = user.getAccs();
		Account acc = new Account();
		temp.add(acc);

		String id = user.getId();
		String password = user.getPassword();
		String name = user.getName();

		user = new User(id, password, name, temp);
		this.um.setUser(this.log, user);

		return acc;
	}

	private boolean logInCheck() {
		if (this.log == -1) {
			return false;
		}

		return true;
	}

	private int checkExistId(String id) {
		int index = -1;
		for (int i = 0; i < UserManager.getList().size(); i++) {
			User user = this.um.getUser(i);
			if (user.getId().equals(id)) {
				index = i;
			}
		}
		return index;
	}

	private boolean passwordCheck(int index, String password) {
		User user = this.um.getUser(index);
		if (user.getPassword().equals(password)) {
			return true;
		}

		return false;
	}

	private void joinUser() {
		String id = inputString("사용할 아이디를");
		int index = checkExistId(id);
		if (index == -1) {
			String password = inputString("사용할 비밀번호를");
			String name = inputString("이름을");
			ArrayList<Account> acc = new ArrayList<Account>();
			User user = new User(id, password, name, acc);
			this.um.createUser(user);
			System.out.printf("%s님 회원 가입 되었습니다!\n", name);
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		String password = inputString("계정을 삭제하려면 비밀번호를");
		if (passwordCheck(this.log, password)) {
			this.um.deleteUser(this.log);
			this.log = -1;
			System.out.println("계정이 삭제되었습니다");
		}
	}

	public void run() {
		boolean isRun = true;
		while (isRun) {
			isRun = selectMenu();
		}
	}
}
