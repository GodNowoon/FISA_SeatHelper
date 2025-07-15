package controller;

import java.util.ArrayList;

import database.Student;
import model.Model;
import view.ConsoleView;
import view.SeatPrinter;
import view.StudentPrinter;

public class Controller {
    private static final Model model = Model.getModel();
    private static final SeatPrinter seatPrinter = new SeatPrinter();
    private static final StudentPrinter stuPrinter = new StudentPrinter();
    static ConsoleView view = new ConsoleView();
    
    // 1. 전체 수강생 목록 가져오기
    public static void getAllStudents() {
        ArrayList<Student> students = model.getStudents();

        if (!students.isEmpty()) {
            for (Student stu : students) {
                stuPrinter.print(stu); 
            }
        } else {
        	view.printMessage("저장된 학생 데이터가 없습니다.");
        }
    }

    
    // 2. 랜덤 자리 출력
    public static void printRandomSeats() {
        int rows = 4;
        int cols = 8;

        String[][] seatArr = model.getRandomSeat(rows, cols);
        seatPrinter.print(seatArr);
    }
    
    // 2-1. 랜덤 자리 출력하고 저장 여부 받기
    public static void saveSeats() {
    	
    }

    // 3. 현재 자리 보기
	public static void printNowSeats() {
		
	}



}
