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

	// Banking 관련 메소드
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
		System.out.println("1. 로그인");
		System.out.println("2. 로그아웃");
		System.out.println("3. 회원 가입/탈퇴");
		System.out.println("4. 계좌 개설/삭제");
		System.out.println("5. 인터넷 뱅킹(입출금, 조회, 이체)");
		System.out.println("6. 파일 저장/로드");
		System.out.println("7. 종료");
		System.out.println("0. 관리자 모드");
	}

	private int inputNumber(String message) {
		int number = -1;

		while (true) {
			System.out.println(message + " 입력하세요");
			String input = this.scan.next();

			try {
				number = Integer.parseInt(input);
				return number;
			} catch (Exception e) {
				System.err.println("숫자만 입력할 수 있습니다");
				scan.nextLine();
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + " 입력하세요");
		String str = this.scan.next();
		return str;
	}

	private int inputMenu() {
		int menu = inputNumber("메뉴를");
		if (menu < ADMIN || menu > EXIT) {
			System.out.println("존재하지 않는 메뉴입니다");
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
				System.out.println("1) 회원 가입");
				System.out.println("2) 회원 탈퇴");

				int subMenu = inputNumber("하위 메뉴를");

				if (subMenu == 1) {
					joinUser();
				}

				else if (subMenu == 2) {
					deleteUser();
				}
			}

			else if (selectedMenu == CREATE_OR_DELETE_ACC) {
				System.out.println("1) 계좌 개설");
				System.out.println("2) 계좌 삭제");

				int subMenu = inputNumber("하위 메뉴를");

				if (!logInCheck()) {
					System.out.println("로그인 후 이용 가능합니다");
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
				System.out.println("1) 입금");
				System.out.println("2) 출금");
				System.out.println("3) 조회");
				System.out.println("4) 이체");

				int subMenu = inputNumber("하위 메뉴를");
				if (!logInCheck()) {
					System.out.println("로그인 후 이용 가능합니다");
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
				System.out.println("1) 파일 저장");
				System.out.println("2) 파일 로드");

				int subMenu = inputNumber("하위 메뉴를");
				if (subMenu == 1) {
					saveFile();
				}

				else if (subMenu == 2) {
					loadFile();
				}
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

			System.out.println("*로드 완료*");
		} catch (Exception e) {
			System.err.println("*로드 실패*");
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

			System.out.println("*저장 성공*");
		} catch (Exception e) {
			System.err.println("*저장 실패*");
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
		Account acc = userAccCheck(user, "본인 계좌를");
		String accNum = acc.getAccNum();
		String toAccNum = inputString("이체할 상대방 계좌를");
		Account toAcc = this.am.getAccountByNum(toAccNum);

		if (toAcc == null || acc == null) {
			System.out.println("계좌 정보가 올바르지 않습니다");
			return;
		}

		int money = inputNumber("이체할 금액을");
		if (money < 0) {
			System.out.println("금액이 올바르지 않습니다");
			return;
		}

		if (acc.getMoney() - money < 0) {
			System.out.println("이체할 금액이 부족합니다");
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
		System.out.printf("%s님 %d원 이체 되었습니다\n잔액 : %d원\n", user.getName(), money, acc.getMoney());
	}

	private void checkMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "조회할 계좌를");
		if (acc != null) {
			System.out.println(acc.toString());
		}
	}

	private Account userAccCheck(User user, String message) {
		if (!existAccountCheck(user)) {
			System.out.println("계좌가 존재하지 않습니다");
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
			System.out.println("계좌 정보가 올바르지 않습니다");
			return null;
		}

		return acc;
	}

	private void drawMoney() {
		User user = this.um.getUser(this.log);
		Account acc = userAccCheck(user, "출금할 계좌를");

		if (acc != null) {
			String accNum = acc.getAccNum();

			int money = inputNumber("출금할 금액을");
			if (money < 0) {
				System.out.println("금액이 올바르지 않습니다");
				return;
			}

			if (acc.getMoney() - money < 0) {
				System.out.println("출금할 금액이 부족합니다");
				return;
			}

			acc.setMoney(acc.getMoney() - money);
			int index = this.am.indexOfByAccNum(accNum);
			this.am.setAccount(index, acc);
			System.out.printf("%s님 %d원 출금 되었습니다\n잔액 : %d원\n", user.getName(), money, acc.getMoney());
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
		Account acc = userAccCheck(user, "입금할 계좌를");

		if (acc != null) {
			String accNum = acc.getAccNum();

			int money = inputNumber("입금할 금액을");
			if (money < 0) {
				System.out.println("금액이 올바르지 않습니다");
				return;
			}
			acc.setMoney(money + acc.getMoney());
			int index = this.am.indexOfByAccNum(accNum);
			this.am.setAccount(index, acc);
			System.out.printf("%s님 %d원 입금 되었습니다\n잔액 : %d원\n", user.getName(), money, acc.getMoney());
		}
	}

	private void createAccount() {
		User user = this.um.getUser(this.log);
		String password = inputString("비밀번호를");
		if (user.getPassword().equals(password)) {
			if (user.getAccountSize() < Account.LIMIT) {
				Account account = this.am.createAccount(new Account(user.getId()));
				this.um.addUserAccountById(user.getId(), account);
				System.out.printf("%s님, 계좌가 생성되었습니다\n계좌번호 : %s\n", user.getName(), account);
			}

			else {
				System.out.println("계좌는 최대 3개까지 개설 가능합니다");
			}
		}

		else {
			System.out.println("회원정보를 확인하세요.");
		}
	}

	private void deleteAccount() {
		User user = this.um.getUser(this.log);
		System.out.println("=== 보유 계좌 목록 ===");
		for (int i = 0; i < user.getAccountSize(); i++) {
			System.out.println(user.getAccount(i).toString());
		}

		Account acc = userAccCheck(user, "삭제할 계좌의 계좌번호를");
		if (acc != null) {
			this.um.deleteUserAccount(user, acc);
			this.am.deleteAccount(acc);
			System.out.println("계좌가 삭제되었습니다");
		}
	}

	private void logIn() {
		if (logInCheck()) {
			System.out.println("이미 로그인 되어 있습니다");
			return;
		}

		String id = inputString("아이디를");
		String password = inputString("비밀번호를");

		User user = checkExistId(id);
		if (user != null && user.getPassword().equals(password)) {
			this.log = this.um.indexOfById(id);
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
			System.out.printf("환영합니다 %s님!\n", user.getName());
		}
	}

	private void printAdminMenu() {
		System.out.println("===== 관리자 모드 =====");
		System.out.println("1. 전체 회원 조회");
		System.out.println("2. 전체 계정 조회");
		System.out.println("3. 선택 회원 조회");
		System.out.println("4. 관리자 모드 종료");
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

	private void printAllUsers() {
		for (int i = 0; i < this.um.getListSize(); i++) {
			User user = this.um.getUser(i);
			System.out.println(user.toString());
		}
	}

	private void printAllAccounts() {
		if (this.am.getListSize() == 0) {
			System.out.println("존재하는 계좌가 없습니다");
			return;
		}

		for (int i = 0; i < this.am.getListSize(); i++) {
			Account acc = this.am.getAccount(i);
			System.out.println(acc.toString());
		}
	}

	private void printselectedUser() {
		String id = inputString("회원의 아이디를");
		User check = checkExistId(id);

		if (check != null) {
			System.out.println(check.toString());
		}

		else {
			System.out.println("존재하지 않는 회원입니다");
		}
	}

	private void selectAdminMenu() {
		while (true) {
			printAdminMenu();
			int adminMenu = inputNumber("메뉴 번호를");
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

	private void adminMode() {
		if (adminLogIn()) {
			selectAdminMenu();
		}
	}

	private void deleteUser() {
		if (!logInCheck()) {
			System.out.println("로그인 후 이용 가능합니다");
			return;
		}

		User user = this.um.getUser(this.log);
		String password = inputString("계정을 삭제하려면 비밀번호를");
		if (user.getPassword().equals(password)) {
			this.um.deleteUser(this.log);
			deleteUserAccInList(user);
			this.log = -1;
			System.out.println("계정이 삭제되었습니다");
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
			System.out.println("로그아웃 후 이용 가능합니다");
			return;
		}

		String id = inputString("사용할 아이디를");
		User check = checkExistId(id);

		if (check == null) {
			String password = inputString("사용할 비밀번호를");
			String name = inputString("이름을");
			ArrayList<Account> accs = new ArrayList<Account>();
			User user = new User(id, password, name, accs);
			this.um.addUser(user);

			System.out.printf("%s님 회원 가입 되었습니다!\n", user.getName());
		}

		else {
			System.out.println("중복된 아이디가 존재합니다.");
		}
	}

	public void run() {
		boolean isRun = true;
		while (isRun) {
			isRun = selectMenu();
		}
	}
}