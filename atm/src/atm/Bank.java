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
			System.out.println(message + " �Է��ϼ���");
			int num = -1;
			try {
				num = scan.nextInt();
				return num;
			} catch (InputMismatchException e) {
				System.err.println("���ڸ� �Է��ϼ���");
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " �Է��ϼ���");
		String str = scan.next();
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
	}

	private int inputMenu() {
		while (true) {
			int menu = inputInt("�޴���");
			if (menu < JOIN_USER || menu > LOG_OUT) {
				System.out.println("�������� �ʴ� �޴��Դϴ�");
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
			System.out.println("�̹� �α��� �Ǿ� �ֽ��ϴ�");
			return;
		}

		String id = inputString("���̵�");
		if (checkExistId(id)) {
			int index = findIndex(id);
			String password = inputString("��й�ȣ��");
			if (userCheck(index, password)) {
				log = index;
				System.out.printf("%s�� �α��� �Ǿ����ϴ�", this.um.getList().get(log).getName());
			}
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
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		String account = inputString("������ ���¹�ȣ��");
		for (int i = 0; i < this.am.getList().size(); i++) {
			if (account.equals(this.am.getAccount(i))) {
				this.am.deleteAccount(i);
			}
		}

		System.out.println("���°� �����Ǿ����ϴ�");
	}

	private void createAccount() {
		if (!logInCheck()) {
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		if (threeAccsCheck()) {
			System.out.println("3�� �̻� ���¸� ������ �� �����ϴ�");
			return;
		}

		Account acc = new Account();
		this.am.createAccount(acc);
		System.out.printf("%s��, ���°� �����Ǿ����ϴ�\n���¹�ȣ : %s\n", this.um.getUser(this.log).getName(), acc.getAccountNum());
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
		String id = inputString("����� ���̵�");
		if (checkExistId(id)) {
			String password = inputString("����� ��й�ȣ��");
			String name = inputString("�̸���");
			User user = new User(id, password, name);
			this.um.createUser(user);
			System.out.printf("%s�� ȸ�� ���� �Ǿ����ϴ�!\n", name);
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		String password = inputString("������ �����Ϸ��� ��� ��ȣ��");
		if (userCheck(log, password)) {
			this.um.deleteUser(this.log);
			this.log = -1;
			System.out.println("������ �����Ǿ����ϴ�");
		}
	}

	public void run() {
		selectMenu();
		// ATM ������Ʈ
		// ȸ������/Ż��
		// ���½�û/öȸ(1�� 3���±���)
		// �α���
	}
	// * ��ũ���� ��ŷ ���� �޼ҵ�

}
