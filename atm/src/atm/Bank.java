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
		System.out.println("3. ���� ��û");
		System.out.println("4. ���� öȸ");
		System.out.println("5. �α���");
		System.out.println("6. �α׾ƿ�");
	}

	private int inputMenu() {
		while(true) {
		int menu = inputInt("�޴���");
		if(menu < JOIN_USER || menu > LOG_OUT) {
			System.out.println("�������� �ʴ� �޴��Դϴ�");
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
		String id = inputString("����� ���̵�");
		if(checkExistId) {
			
		}
		String password = inputString("����� ��й�ȣ��");
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
