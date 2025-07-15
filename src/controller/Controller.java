package controller;

import static view.AnsiColor.GREEN;
import static view.AnsiColor.RESET;
import static view.AnsiColor.YELLOW;

import java.sql.SQLException;
import java.util.ArrayList;

import database.Student;
import model.Model;
import view.ConsoleView;
import view.StudentPrinter;

public class Controller {
	
	private static final Model model = Model.getModel();
	private static final StudentPrinter stuPrinter = new StudentPrinter();
	private static ConsoleView view = new ConsoleView();
	private static String[][] lastRandomSeats;

	
	// 1. 전체 수강생 목록 가져오기
	public static void getAllStudents() {
		
		try {
			ArrayList<Student> students = model.getStudents();
			
			if (!students.isEmpty()) {
				for (Student student : students) {
					stuPrinter.print(student);
				}
			} else {
				view.printMessage(YELLOW + "⚠️ 저장된 학생 데이터가 없습니다." + RESET);
			}

		} catch (Exception e) {
			e.printStackTrace();
			view.printMessage(YELLOW + "⚠️ 문제가 발생했습니다. 재실행 해 주세요!" + RESET);
		}
		
	}

	
	// 2. 랜덤 자리 출력
	public static void printRandomSeats() {
		
		try {
			String[][] randomSeats = model.getRandomSeat();
			lastRandomSeats = randomSeats;
			view.printSeatLayout(randomSeats);
		} catch (SQLException e) {
			view.printMessage(YELLOW + "⚠️ 문제가 발생했습니다.다시 자리 배치를 해 주세요!" + RESET);
			e.printStackTrace();
		}
		
	}

	
	// 2-1. 랜덤 자리 출력하고 저장 여부 받기
	public static void saveSeats() {
		
		if (lastRandomSeats != null) {
			model.saveCurrentSeat();
			view.printMessage(GREEN + "✅ 자리 배치가 저장되었습니다!" + RESET);
		} else {
			view.printMessage(YELLOW + "⚠️ 먼저 랜덤 자리 배치를 실행하세요!" + RESET);
		}
		
	}

	
	// 3. 현재 자리 보기
	public static void printCurrentSeats() {
		
		String[][] currentSeats = model.getCurrentSeat();
		
		if (currentSeats != null) {
			view.printSeatLayout(currentSeats);
		} else {
			view.printMessage(YELLOW + "⚠️ 현재 저장된 자리가 없습니다. 먼저 랜덤 배치를 실행하고 저장하세요!" + RESET);
		}
		
	}
	
}
