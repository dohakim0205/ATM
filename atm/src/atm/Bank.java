package atm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {
	private Scanner scan;
	private String name;
	private UserManager um;
	private AccountManager am;
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
		System.out.println("3. 계좌 신청");
		System.out.println("4. 계좌 철회");
		System.out.println("5. 로그인");
		System.out.println("6. 로그아웃");
	}

	private int inputMenu() {
		while(true) {
		int menu = inputInt("메뉴를");
		if(menu < JOIN_USER || menu > LOG_OUT) {
			System.out.println("존재하지 않는 메뉴입니다");
			continue;
		}
		return menu;
		}
	}

	private void selectMenu() {
		printMenu();
		int selectedMenu = inputMenu();
		
		if(selectedMenu == JOIN_USER) {
			joinUser();
		}
		
		else if(selectedMenu == DELETE_USER) {
			
		}
		
		else if(selectedMenu == CREATE_ACC) {
			
		}
		
		else if(selectedMenu == DELETE_ACC) {
			
		}
		
		else if(selectedMenu == LOG_IN) {
			
		}
		
		else if(selectedMenu == LOG_OUT) {
			
		}
	}
	
	private boolean checkExistId(String id) {
		
	}
	
	private void joinUser() {
		String id = inputString("사용할 아이디를");
		if(checkExistId) {
			
		}
		String password = inputString("사용할 비밀번호를");
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
