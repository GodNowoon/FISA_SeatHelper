package controller;

import java.util.ArrayList;

import database.Student;
import model.Model;
import view.SeatPrinter;
import view.StudentPrinter;

public class Controller {
    private static final Model model = Model.getModel();
    private static final SeatPrinter seatPrinter = new SeatPrinter();
    private static final StudentPrinter stuPrinter = new StudentPrinter();
    
    // 1. 전체 수강생 목록 가져오기
    public static void getAllStudents() {
        ArrayList<Student> students = model.getStudents();

        if (!students.isEmpty()) {
            for (Student stu : students) {
                stuPrinter.print(stu); 
            }
        } else {
            System.out.println("학생 데이터 없음");
        }
    }

    
    // 2. 랜덤 자리 출력
    public static void printRandomSeats() {
        int rows = 4;
        int cols = 8;

        String[][] seatArr = model.getRandomSeat(rows, cols);
        seatPrinter.print(seatArr);
    }

}
