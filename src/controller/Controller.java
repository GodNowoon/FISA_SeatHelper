package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;
import database.Student;
import model.Model;
import view.ConsoleView;
import view.SeatPrinter;
import view.StudentPrinter;

public class Controller {
	private static final Model model = Model.getModel();
	private static final SeatPrinter seatPrinter = new SeatPrinter();
	private static final StudentPrinter stuPrinter = new StudentPrinter();
	private static final Database db = new Database();
	static ConsoleView view = new ConsoleView();
	private static String[][] lastRandomSeats; // 마지막 랜덤 자리 캐시

	// 1. 전체 수강생 목록 가져오기
	public static void getAllStudents() {
		try {
			ArrayList<Student> students = model.getStudents();
			if (!students.isEmpty()) {
				for (Student student : students) {
					stuPrinter.print(student);
				}
			} else {
				view.printMessage("저장된 학생 데이터가 없습니다.");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// 2. 랜덤 자리 출력
	public static void printRandomSeats() throws SQLException {
		/*
		 * int rows = 4; int cols = 8;
		 * 
		 * String[][] seatArr = model.getRandomSeat(rows, cols);
		 * seatPrinter.print(seatArr);
		 */
        
        String[][] randomSeats = model.getRandomSeat();
        lastRandomSeats = randomSeats;
        view.printSeatLayout(randomSeats);
	}

	// 2-1. 랜덤 자리 출력하고 저장 여부 받기
	public static void saveSeats() {
		if (lastRandomSeats != null) {
			model.saveCurrentSeat();
			view.printMessage("✅ 자리 배치가 저장되었습니다!");
		} else {
			view.printMessage("⚠️ 먼저 랜덤 자리 배치를 실행하세요!");
		}
	}

	// 3. 현재 자리 보기
	public static void printCurrentSeats() {
		String[][] currentSeats = model.getCurrentSeat();
	    if (currentSeats != null) {
	        view.printSeatLayout(currentSeats);
	    } else {
	        view.printMessage("⚠️ 현재 저장된 자리가 없습니다. 먼저 랜덤 배치를 실행하고 저장하세요!");
	    }
	}
}
