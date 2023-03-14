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
	private static final int JOIN_USER = 1;
	private static final int DELETE_USER = 2;
	private static final int CREATE_ACC = 3;
	private static final int DELETE_ACC = 4;
	private static final int LOG_IN = 5;
	private static final int LOG_OUT = 6;

	public Bank(String name) {
		this.scan = new Scanner(System.in);
		this.um = new UserManager();
		this.am = new AccountManager();
		this.name = name;
		this.log = -1;
	}

	private int inputInt(String message) {
		while (true) {
			System.out.println(message + " 입력하세요");
			int num = -1;
			try {
				num = scan.nextInt();
				return num;
			} catch (InputMismatchException e) {
				System.err.println("숫자를 입력하세요");
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " 입력하세요");
		String str = scan.next();
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
	}

	private int inputMenu() {
		while (true) {
			int menu = inputInt("메뉴를");
			if (menu < JOIN_USER || menu > LOG_OUT) {
				System.out.println("존재하지 않는 메뉴입니다");
				continue;
			}
			return menu;
		}
	}

	private void selectMenu() {
		printMenu();
		int selectedMenu = inputMenu();

		if (selectedMenu == JOIN_USER) {
			joinUser();
		}

		else if (selectedMenu == DELETE_USER) {
			deleteUser();
		}

		else if (selectedMenu == CREATE_ACC) {
			createAccount();
		}

		else if (selectedMenu == DELETE_ACC) {
			deleteAccount();
		}

		else if (selectedMenu == LOG_IN) {
			logIn();
		}

		else if (selectedMenu == LOG_OUT) {
			logOut();
		}
	}

	private void logIn() {
		if (logInCheck()) {
			System.out.println("이미 로그인 되어 있습니다");
			return;
		}

		String id = inputString("아이디를");
		if (checkExistId(id)) {
			int index = findIndex(id);
			String password = inputString("비밀번호를");
			if (userCheck(index, password)) {
				log = index;
				System.out.printf("%s님 로그인 되었습니다", this.um.getList().get(log).getName());
			}
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

	private int findIndex(String id) {
		int index = -1;
		for (int i = 0; i < this.um.getList().size(); i++) {
			if (this.um.getList().get(i).getId().equals(id)) {
				index = i;
			}
		}

		return index;
	}

	private boolean threeAccsCheck() {
		User user = this.um.getUser(this.log);
		int count = this.um.getAccList(user).size();
		if (count == 3) {
			return true;
		}

		return false;
	}

	private void deleteAccount() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		String account = inputString("삭제할 계좌번호를");
		for (int i = 0; i < this.am.getList().size(); i++) {
			if (account.equals(this.am.getAccount(i))) {
				this.am.deleteAccount(i);
			}
		}

		System.out.println("계좌가 삭제되었습니다");
	}

	private void createAccount() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		if (threeAccsCheck()) {
			System.out.println("3개 이상 계좌를 개설할 수 없습니다");
			return;
		}

		Account acc = new Account();
		this.am.createAccount(acc);
		System.out.printf("%s님, 계좌가 개설되었습니다\n계좌번호 : %s\n", this.um.getUser(this.log).getName(), acc.getAccountNum());
	}

	private boolean logInCheck() {
		if (this.log == -1) {
			return false;
		}

		return true;
	}

	private boolean checkExistId(String id) {
		ArrayList<User> temp = this.um.getList();
		for (User user : temp) {
			if (user.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	private boolean userCheck(int index, String password) {
		if (this.um.getList().get(index).getPassword().equals(password)) {
			return true;
		}

		return false;
	}

	private void joinUser() {
		String id = inputString("사용할 아이디를");
		if (checkExistId(id)) {
			String password = inputString("사용할 비밀번호를");
			String name = inputString("이름을");
			User user = new User(id, password, name);
			this.um.createUser(user);
			System.out.printf("%s님 회원 가입 되었습니다!\n", name);
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		String password = inputString("계정을 삭제하려면 비밀 번호를");
		if (userCheck(log, password)) {
			this.um.deleteUser(this.log);
			this.log = -1;
			System.out.println("계정이 삭제되었습니다");
		}
	}

	public void run() {
		selectMenu();
		// ATM 프로젝트
		// 회원가입/탈퇴
		// 계좌신청/철회(1인 3계좌까지)
		// 로그인
	}
	// * 뱅크에는 뱅킹 관련 메소드

}
