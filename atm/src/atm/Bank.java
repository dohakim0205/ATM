package atm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {

	private Scanner scan;
	private String brandName;
	private UserManager um;
	private AccountManager am;
	private FileManager fm;
	private String adminId;
	private String adminPassword;
	private int log;
	private static final int LOG_IN = 1;
	private static final int LOG_OUT = 2;
	private static final int JOIN_OR_DELETE_USER = 3;
	private static final int CREATE_OR_DELETE_ACC = 4;
	private static final int ONLINE_BANKING = 5;
	private static final int SAVE_OR_LOAD_FILE = 6;
	private static final int EXIT = 7;
	private static final int ADMIN = 0;

	// Banking ���� �޼ҵ�
	public Bank(String brandName) {
		this.brandName = brandName;
		this.scan = new Scanner(System.in);
		this.um = new UserManager();
		this.am = new AccountManager();
		this.fm = new FileManager();
		this.log = -1;
		this.adminId = "admin";
		this.adminPassword = "admin";
	}

	private void printMainMenu() {
		System.out.println("=====" + this.brandName + "=====");
		System.out.println("1. �α���");
		System.out.println("2. �α׾ƿ�");
		System.out.println("3. ȸ�� ����/Ż��");
		System.out.println("4. ���� ����/����");
		System.out.println("5. ���ͳ� ��ŷ(�����, ��ȸ, ��ü)");
		System.out.println("6. ���� ����/�ε�");
		System.out.println("7. ����");
		System.out.println("0. ������ ���");
	}

	private int inputNumber(String message) {
		int number = -1;

		while (true) {
			System.out.println(message + " �Է��ϼ���");
			String input = this.scan.next();

			try {
				number = Integer.parseInt(input);
				return number;
			} catch (Exception e) {
				System.err.println("���ڸ� �Է��� �� �ֽ��ϴ�");
				scan.nextLine();
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " �Է��ϼ���");
		String str = this.scan.next();
		return str;
	}

	private int inputMenu() {
		int menu = inputNumber("�޴���");
		if (menu < ADMIN || menu > EXIT) {
			System.out.println("�������� �ʴ� �޴��Դϴ�");
		}
		return menu;
	}

	private boolean selectMenu() {
		while (true) {
			isLoggedInUser();
			printAllUsers();
			printMainMenu();
			int selectedMenu = inputMenu();

			if (selectedMenu == LOG_IN) {
				logIn();
			}

			else if (selectedMenu == LOG_OUT) {
				logOut();
			}

			else if (selectedMenu == JOIN_OR_DELETE_USER) {
				System.out.println("1) ȸ�� ����");
				System.out.println("2) ȸ�� Ż��");

				int subMenu = inputNumber("���� �޴���");

				if (subMenu == 1) {
					joinUser();
				}

				else if (subMenu == 2) {
					deleteUser();
				}
			}

			else if (selectedMenu == CREATE_OR_DELETE_ACC) {
				System.out.println("1) ���� ����");
				System.out.println("2) ���� ����");

				int subMenu = inputNumber("���� �޴���");

				if (!logInCheck()) {
					System.out.println("�α��� �� �̿� �����մϴ�");
					continue;
				}

				if (subMenu == 1) {
					createAccount();
				}

				else if (subMenu == 2) {
					deleteAccount();
				}
			}

			else if (selectedMenu == ONLINE_BANKING) {
				System.out.println("1) �Ա�");
				System.out.println("2) ���");
				System.out.println("3) ��ȸ");
				System.out.println("4) ��ü");

				int subMenu = inputNumber("���� �޴���");
				if (!logInCheck()) {
					System.out.println("�α��� �� �̿� �����մϴ�");
					continue;
				}

				if (subMenu == 1) {
					inputMoney();
				}

				else if (subMenu == 2) {
					drawMoney();
				}

				else if (subMenu == 3) {
					checkMoney();
				}

				else if (subMenu == 4) {
					sendMoney();
				}
			}

			else if (selectedMenu == SAVE_OR_LOAD_FILE) {
				System.out.println("1) ���� ����");
				System.out.println("2) ���� �ε�");

				int subMenu = inputNumber("���� �޴���");
				if (subMenu == 1) {
					saveFile();
				}

				else if (subMenu == 2) {
					loadFile();
				}
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
	}

	private void loadAccData(String accData) {

		String[] accs = accData.split("/");
		String userId = accs[0];
		String accNum = accs[1];
		int money = Integer.parseInt(accs[2]);

		Account acc = new Account(userId, accNum, money);
		this.am.addAccount(acc);
	}

	private void loadUserData(String userData) {
		String[] temp = userData.split("///");
		ArrayList<Account> list = new ArrayList<Account>();
		if (temp.length != 1) {
			String[] userAccs = temp[1].split("//");

			for (int i = 0; i < userAccs.length; i++) {
				String[] userAcc = userAccs[i].split("/");
				Account acc = new Account(userAcc[0], userAcc[1], Integer.parseInt(userAcc[2]));
				list.add(acc);
			}
		}

		String[] userInfo = temp[0].split("/");
		String id = userInfo[0];
		String password = userInfo[1];
		String name = userInfo[2];
		User user = new User(id, password, name, list);
		this.um.addUser(user);
	}

	private void loadFile() {
		try {
			FileReader fr = this.fm.fr;
			fr = new FileReader(this.fm.file);
			BufferedReader br = this.fm.br;
			br = new BufferedReader(fr);
			this.am = new AccountManager();
			this.um = new UserManager();

			while (true) {
				String userData = br.readLine();
				if (userData.charAt(userData.length() - 1) == '&') {
					userData = userData.substring(0, userData.length() - 1);
					loadUserData(userData);
					break;
				}
				loadUserData(userData);
			}

			while (true) {
				String accData = br.readLine();
				if (accData.charAt(accData.length() - 1) == '&') {
					accData = accData.substring(0, accData.length() - 1);
					loadAccData(accData);
					break;
				}
				loadAccData(accData);
			}

			String etc = br.readLine();
			String[] etcs = etc.split("/");
			this.brandName = etcs[0];
			this.adminId = etcs[1];
			this.adminPassword = etcs[2];

			br.close();
			fr.close();

			System.out.println("*�ε� �Ϸ�*");
		} catch (Exception e) {
			System.err.println("*�ε� ����*");
			e.printStackTrace();
		}
	}

	private String makeUserData() {
		String userData = "";
		for (int i = 0; i < this.um.getListSize(); i++) {
			User user = this.um.getUser(i);
			userData += user.getId() + "/";
			userData += user.getPassword() + "/";
			userData += user.getName() + "///";

			ArrayList<Account> list = user.getAccountList();
			for (int j = 0; j < list.size(); j++) {
				Account acc = list.get(j);
				userData += acc.getUserId() + "/";
				userData += acc.getAccNum() + "/";
				userData += acc.getMoney();

				if (j < list.size() - 1) {
					userData += "//";
				}
			}

			if (i < this.um.getListSize() - 1) {
				userData += "\n";
			}
		}

		return userData;
	}

	private String makeAccData() {
		String accData = "";
		for (int i = 0; i < this.am.getListSize(); i++) {
			Account acc = this.am.getAccount(i);
			accData += acc.getUserId() + "/";
			accData += acc.getAccNum() + "/";
			accData += acc.getMoney();

			if (i < this.um.getListSize() - 1) {
				accData += "\n";
			}
		}

		return accData;
	}

	private void saveFile() {
		String userData = makeUserData() + '&' + "\n";
		String accData = makeAccData() + '&' + "\n";
		String etc = this.brandName + "/" + this.adminId + "/" + this.adminPassword + "/";

		try {
			FileWriter fw = this.fm.fw;
			fw = new FileWriter(this.fm.file);
			fw.write(userData);
			fw.write(accData);
			fw.write(etc);
			fw.close();

			System.out.println("*���� ����*");
		} catch (Exception e) {
			System.err.println("*���� ����*");
			e.printStackTrace();
		}
	}

	private int findUserIndex(User user, Account acc) {
		int index = -1;
		for (int i = 0; i < user.getAccountSize(); i++) {
			Account temp = user.getAccount(i);
			if (acc.getUserId().equals(temp.getUserId())) {
				index = i;
			}
		}
		return index;
	}

	private void sendMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "���� ���¸�");
		String accNum = acc.getAccNum();
		String toAccNum = inputString("��ü�� ���� ���¸�");
		Account toAcc = this.am.getAccountByNum(toAccNum);

		if (toAcc == null || acc == null) {
			System.out.println("���� ������ �ùٸ��� �ʽ��ϴ�");
			return;
		}

		int money = inputNumber("��ü�� �ݾ���");
		if (money < 0) {
			System.out.println("�ݾ��� �ùٸ��� �ʽ��ϴ�");
			return;
		}

		if (acc.getMoney() - money < 0) {
			System.out.println("��ü�� �ݾ��� �����մϴ�");
			return;
		}

		int indexMyAcc = findUserIndex(user, acc);
		User toUser = this.um.getUserById(toAcc.getUserId());
		int indexOtherAcc = findUserIndex(toUser, toAcc);

		acc.setMoney(acc.getMoney() - money);
		toAcc.setMoney(toAcc.getMoney() + money);

		user.setUserAccountMoney(indexMyAcc, acc.getMoney());
		toUser.setUserAccountMoney(indexOtherAcc, toAcc.getMoney());

		int indexMy = this.am.indexOfByAccNum(accNum);
		int indexOther = this.um.indexOfById(toAcc.getUserId());

		this.am.setAccount(indexMy, acc);
		this.am.setAccount(indexOther, toAcc);
		System.out.printf("%s�� %d�� ��ü �Ǿ����ϴ�\n�ܾ� : %d��\n", user.getName(), money, acc.getMoney());
	}

	private void checkMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "��ȸ�� ���¸�");
		if (acc != null) {
			System.out.println(acc.toString());
		}
	}

	private Account userAccCheck(User user, String message) {
		if (!existAccountCheck(user)) {
			System.out.println("���°� �������� �ʽ��ϴ�");
			return null;
		}

		String accNum = inputString(message);
		Account acc = null;
		for (int i = 0; i < user.getAccountSize(); i++) {
			Account userAcc = user.getAccount(i);
			if (accNum.equals(userAcc.getAccNum())) {
				acc = userAcc;
			}
		}
		if (acc == null) {
			System.out.println("���� ������ �ùٸ��� �ʽ��ϴ�");
			return null;
		}

		return acc;
	}

	private void drawMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "����� ���¸�");

		if (acc != null) {
			String accNum = acc.getAccNum();

			int money = inputNumber("����� �ݾ���");
			if (money < 0) {
				System.out.println("�ݾ��� �ùٸ��� �ʽ��ϴ�");
				return;
			}

			if (acc.getMoney() - money < 0) {
				System.out.println("����� �ݾ��� �����մϴ�");
				return;
			}

			acc.setMoney(acc.getMoney() - money);
			int index = this.am.indexOfByAccNum(accNum);
			this.am.setAccount(index, acc);
			System.out.printf("%s�� %d�� ��� �Ǿ����ϴ�\n�ܾ� : %d��\n", user.getName(), money, acc.getMoney());
		}
	}

	private boolean existAccountCheck(User user) {
		if (user.getAccountSize() == 0) {
			return false;
		}

		return true;
	}

	private void inputMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "�Ա��� ���¸�");

		if (acc != null) {
			String accNum = acc.getAccNum();

			int money = inputNumber("�Ա��� �ݾ���");
			if (money < 0) {
				System.out.println("�ݾ��� �ùٸ��� �ʽ��ϴ�");
				return;
			}
			acc.setMoney(money + acc.getMoney());
			int index = this.am.indexOfByAccNum(accNum);
			this.am.setAccount(index, acc);
			System.out.printf("%s�� %d�� �Ա� �Ǿ����ϴ�\n�ܾ� : %d��\n", user.getName(), money, acc.getMoney());
		}
	}

	private void createAccount() {
		User user = this.um.getUser(this.log);
		String password = inputString("��й�ȣ��");
		if (user.getPassword().equals(password)) {
			if (user.getAccountSize() < Account.LIMIT) {
				Account account = this.am.createAccount(new Account(user.getId()));
				this.um.addUserAccountById(user.getId(), account);
				System.out.printf("%s��, ���°� �����Ǿ����ϴ�\n���¹�ȣ : %s\n", user.getName(), account);
			}

			else {
				System.out.println("���´� �ִ� 3������ ���� �����մϴ�");
			}
		}

		else {
			System.out.println("ȸ�������� Ȯ���ϼ���.");
		}
	}

	private void deleteAccount() {
		User user = this.um.getUser(this.log);
		System.out.println("=== ���� ���� ��� ===");
		for (int i = 0; i < user.getAccountSize(); i++) {
			System.out.println(user.getAccount(i).toString());
		}

		Account acc = userAccCheck(user, "������ ������ ���¹�ȣ��");
		if (acc != null) {
			this.um.deleteUserAccount(user, acc);
			this.am.deleteAccount(acc);
			System.out.println("���°� �����Ǿ����ϴ�");
		}
	}

	private void logIn() {
		if (logInCheck()) {
			System.out.println("�̹� �α��� �Ǿ� �ֽ��ϴ�");
			return;
		}

		String id = inputString("���̵�");
		String password = inputString("��й�ȣ��");

		User user = checkExistId(id);
		if (user != null && user.getPassword().equals(password)) {
			this.log = this.um.indexOfById(id);
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

	private User checkExistId(String id) {
		User check = this.um.getUserById(id);
		if (check != null) {
			return check;
		}

		return null;
	}

	private boolean logInCheck() {
		if (this.log == -1) {
			return false;
		}

		return true;
	}

	private void isLoggedInUser() {
		if (logInCheck()) {
			User user = this.um.getUser(this.log);
			System.out.printf("ȯ���մϴ� %s��!\n", user.getName());
		}
	}

	private void printAdminMenu() {
		System.out.println("===== ������ ��� =====");
		System.out.println("1. ��ü ȸ�� ��ȸ");
		System.out.println("2. ��ü ���� ��ȸ");
		System.out.println("3. ���� ȸ�� ��ȸ");
		System.out.println("4. ������ ��� ����");
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

	private void printAllUsers() {
		for (int i = 0; i < this.um.getListSize(); i++) {
			User user = this.um.getUser(i);
			System.out.println(user.toString());
		}
	}

	private void printAllAccounts() {
		if (this.am.getListSize() == 0) {
			System.out.println("�����ϴ� ���°� �����ϴ�");
			return;
		}

		for (int i = 0; i < this.am.getListSize(); i++) {
			Account acc = this.am.getAccount(i);
			System.out.println(acc.toString());
		}
	}

	private void printselectedUser() {
		String id = inputString("ȸ���� ���̵�");
		User check = checkExistId(id);

		if (check != null) {
			System.out.println(check.toString());
		}

		else {
			System.out.println("�������� �ʴ� ȸ���Դϴ�");
		}
	}

	private void selectAdminMenu() {
		while (true) {
			printAdminMenu();
			int adminMenu = inputNumber("�޴� ��ȣ��");
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

	private void adminMode() {
		if (adminLogIn()) {
			selectAdminMenu();
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("�α��� �� �̿� �����մϴ�");
			return;
		}

		User user = this.um.getUser(this.log);
		String password = inputString("������ �����Ϸ��� ��й�ȣ��");
		if (user.getPassword().equals(password)) {
			this.um.deleteUser(this.log);
			deleteUserAccInList(user);
			this.log = -1;
			System.out.println("������ �����Ǿ����ϴ�");
		}
	}

	private void deleteUserAccInList(User user) {
		for (int i = 0; i < user.getAccountSize(); i++) {
			Account accInUser = user.getAccount(i);
			for (int j = 0; j < this.am.getListSize(); j++) {
				Account accInList = this.am.getAccount(j);
				if (accInUser.getAccNum().equals(accInList.getAccNum())) {
					this.am.deleteAccount(j);
				}
			}
		}
	}

	private void joinUser() {
		if (logInCheck()) {
			System.out.println("�α׾ƿ� �� �̿� �����մϴ�");
			return;
		}

		String id = inputString("����� ���̵�");
		User check = checkExistId(id);

		if (check == null) {
			String password = inputString("����� ��й�ȣ��");
			String name = inputString("�̸���");
			ArrayList<Account> accs = new ArrayList<Account>();
			User user = new User(id, password, name, accs);
			this.um.addUser(user);

			System.out.printf("%s�� ȸ�� ���� �Ǿ����ϴ�!\n", user.getName());
		}

		else {
			System.out.println("�ߺ��� ���̵� �����մϴ�.");
		}
	}

	public void run() {
		boolean isRun = true;
		while (isRun) {
			isRun = selectMenu();
		}
	}
}