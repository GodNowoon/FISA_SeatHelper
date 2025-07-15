package view;

import database.Database;

public class ViewTest {
	public static void main(String[] args) {
		ConsoleView view = new ConsoleView();
		
		view.showMainMenu(); 
		int choice = view.getUserChoice();
		
		if (choice == 1) {  
			controller.Controller.getAllStudents();
		} else if (choice == 2) {
			controller.Controller.printRandomSeats();
			view.printExitMessage(); 
		}else if (choice == 0) {
			view.printExitMessage(); 
		}
		else { 
			System.out.println("잘못된 입력입니다."); 
		}
//		Database.closeSSHTunnel();
	}
}
