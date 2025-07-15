package view;

import static view.AnsiColor.RED;
import static view.AnsiColor.RESET;

import controller.Controller;

public class StartView {

	public static void main(String[] args) {
		ConsoleView view = new ConsoleView();
		
		while (true) { // 사용자가 종료(0)하기전까지 메뉴 출력
			view.showMainMenu(); // 인트로와 메뉴 출력
			int choice = view.getUserChoice(); // 사용자 입력 받기
			
			// 사용자가 입력한 메뉴에 따라 동작하는 로직
			if (choice == 1) {  // 1. 모든 수강생 보기
				view.printMessage("|번호|===이름====|출생연도| MBTI |");
				view.printMessage("-----------------------------"); 
				Controller.getAllStudents();
			} else if (choice == 2) { // 2. 랜덤 자리 배치 보기
				Controller.printRandomSeats();
				
				if (view.askSave()) {
					Controller.saveSeats();
					view.printMessage("자리 배치가 저장되었습니다!");
				} else {
					view.printMessage("저장하지 않고 메뉴로 돌아갑니다.");
				}
			} else if(choice == 3) { // 3. 현재 자리 보기
				Controller.printCurrentSeats();
			} else if (choice == 0) {
				view.printMessage(RED + "\n❌ 프로그램을 종료합니다. 다음에 또 만나요! 💫" + RESET); 
				break; // 0. 프로그램 종료
			} else { 
				view.printMessage(RED + "\n❌ 잘못된 입력입니다. 메뉴 번호를 입력해주세요." + RESET);
			}
			
			System.out.println(); // 메뉴 보기 전 한 줄 띄우기
		}
	}
	
}
