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
			System.out.println(message + " �Է��ϼ���");
			int num = -1;
			try {
				num = this.scan.nextInt();
				return num;
			} catch (InputMismatchException e) {
				System.err.println("���ڸ� �Է��Ͻñ� �ٶ��ϴ�");
				scan.nextLine();
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " �Է��ϼ���");
		String str = this.scan.next();
		return str;
	}

	private void printMenu() {
		System.out.println("=====" + this.name + "=====");
		System.out.println("1. ȸ�� ����");
		System.out.println("2. ȸ�� Ż��");
		System.out.println("3. ���� ����");
		System.out.println("4. ���� ����");
		System.out.println("5. �α���");
		System.out.println("6. �α׾ƿ�");
		System.out.println("7. ����");
		System.out.println("0. ������ ���");
	}

	private int inputMenu() {
		while (true) {
			int menu = inputInt("�޴���");
			if (menu < ADMIN || menu > EXIT) {
				System.out.println("�������� �ʴ� �޴��Դϴ�");
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
			System.out.println("*ATM�� �����մϴ�*");
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
			int adminMenu = inputInt("�޴� ��ȣ��");
			if (adminMenu < 1 || adminMenu > 4) {
				System.out.println("�������� �ʴ� �޴��Դϴ�");
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
				System.out.println("*������ ��� ����*");
				break;
			}
		}
	}

	private void printselectedUser() {
		String id = inputString("ȸ���� ���̵�");
		int index = checkExistId(id);

		if (index != -1) {
			User user = this.um.getUserById(id);
			System.out.println(user.toString());
		}

		else {
			System.out.println("�������� �ʴ� ���̵��Դϴ�");
		}
	}

	private void printAllAccounts() {
		if (AccountManager.getList().size() == 0) {
			System.out.println("�����ϴ� ȸ���� �����ϴ�");
			return;
		}

		for (Account acc : AccountManager.getList()) {
			System.out.println(acc.toString());
		}
	}

	private void printAllUsers() {
		if (UserManager.getList().size() == 0) {
			System.out.println("�����ϴ� ȸ���� �����ϴ�");
			return;
		}

		for (User user : UserManager.getList()) {
			System.out.println(user.toString());
		}
	}

	private boolean adminLogIn() {
		String id = inputString("(ID : admin)������ ���̵�");
		String password = inputString("(PW : admin)������ ��й�ȣ��");
		if (id.equals(this.adminId) && password.equals(this.adminPassword)) {
			System.out.println("������ �α��� �Ǿ����ϴ�");
			return true;
		}

		System.out.println("������ ���̵� Ȥ�� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�");
		return false;
	}

	private void printAdminMenu() {
		System.out.println("===== ������ ��� =====");
		System.out.println("1. ��ü ȸ�� ��ȸ");
		System.out.println("2. ��ü ���� ��ȸ");
		System.out.println("3. ���� ȸ�� ��ȸ");
		System.out.println("4. ������ ��� ����");
	}

	private void isLoggedInUser() {
		if (logInCheck()) {
			User user = this.um.getUser(this.log);
			System.out.printf("�ȳ��ϼ��� %s��!\n", user.getName());
		}
	}

	private void logIn() {
		if (logInCheck()) {
			System.out.println("�̹� �α��� �Ǿ� �ֽ��ϴ�");
			return;
		}

		String id = inputString("���̵�");
		String password = inputString("��й�ȣ��");
		int index = checkExistId(id);
		if (index != -1 && passwordCheck(index, password)) {
			this.log = index;
			User user = this.um.getUser(index);
			System.out.printf("%s�� �α��� �Ǿ����ϴ�\n", user.getName());
		}

		else {
			System.out.println("ȸ�� ������ �ùٸ��� �ʽ��ϴ�");
		}

	}

	private void logOut() {
		if (!logInCheck()) {
			System.out.println("�̹� �α׾ƿ� �Ǿ� �ֽ��ϴ�");
			return;
		}

		this.log = -1;
		System.out.println("�α׾ƿ� �Ǿ����ϴ�");
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
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		User user = this.um.getUser(this.log);
		System.out.println("=== ���� ���� ��� ===");
		System.out.println(user.getAccs().toString());
		String accountNum = inputString("������ ���¹�ȣ��");

		int index = findAccountIndex(accountNum);

		if (index != -1) {
			updateUserAccount(user, accountNum);
			this.am.deleteAccount(index);
			System.out.println("���°� �����Ǿ����ϴ�");
		}

		else {
			System.out.println("���¹�ȣ�� �ùٸ��� �ʽ��ϴ�");
		}
	}

	private void createUserAccount() {
		if (!logInCheck()) {
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		if (limitAccsCountCheck()) {
			System.out.println("3�� �̻� ���¸� ������ �� �����ϴ�");
			return;
		}

		User user = this.um.getUser(this.log);
		Account acc = addUserAccount(user);
		this.am.createAccount(acc);
		System.out.printf("%s��, ���°� �����Ǿ����ϴ�\n���¹�ȣ : %s\n", user.getName(), acc.getAccountNum());
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
		String id = inputString("����� ���̵�");
		int index = checkExistId(id);
		if (index == -1) {
			String password = inputString("����� ��й�ȣ��");
			String name = inputString("�̸���");
			ArrayList<Account> acc = new ArrayList<Account>();
			User user = new User(id, password, name, acc);
			this.um.createUser(user);
			System.out.printf("%s�� ȸ�� ���� �Ǿ����ϴ�!\n", name);
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		String password = inputString("������ �����Ϸ��� ��й�ȣ��");
		if (passwordCheck(this.log, password)) {
			this.um.deleteUser(this.log);
			this.log = -1;
			System.out.println("������ �����Ǿ����ϴ�");
		}
	}

	public void run() {
		boolean isRun = true;
		while (isRun) {
			isRun = selectMenu();
		}
	}
}
