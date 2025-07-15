package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;
import database.Student;
import info.SeatInfo;

public class Model {
    private Database db = new Database();

    private static Model model = new Model();

    public static Model getModel() {
        return model;
    }

    // 1. 전체 수강생 목록 가져오기
    public ArrayList<Student> getStudents() throws SQLException {
        return db.getAllStudents();
    }

    // 2. 랜덤 자리 반환
    public String[][] getRandomSeat() throws SQLException {
        String[][] seat = new String[SeatInfo.ROW][SeatInfo.COL];
        Student[][] students = new Student[SeatInfo.ROW][SeatInfo.COL];

        ArrayList<Integer> picked = new ArrayList<>();

        // 중앙 두 자리 먼저 배치 (안경 조건 true)
        for (int r = 0; r < SeatInfo.ROW; r++) {
            students[r][SeatInfo.COL / 2 - 1] = getRandStudentNotPicked(picked, true);
            students[r][SeatInfo.COL / 2] = getRandStudentNotPicked(picked, true);
        }

        // 오른쪽 절반 채우기
        for (int r = 0; r < SeatInfo.ROW; r++) {
            for (int c = SeatInfo.COL / 2 + 1; c < SeatInfo.COL; c++) {
                if (students[r][c - 1] != null) {
                    students[r][c] = getParentStudentNotPicked(students[r][c - 1].getNo(), picked);
                } else {
                    students[r][c] = getRandStudentNotPicked(picked, false);
                }
            }
        }

        // 왼쪽 절반 채우기
        for (int r = 0; r < SeatInfo.ROW; r++) {
            for (int c = SeatInfo.COL / 2 - 2; c >= 0; c--) {
                if (students[r][c + 1] != null) {
                    students[r][c] = getParentStudentNotPicked(students[r][c + 1].getNo(), picked);
                } else {
                    students[r][c] = getRandStudentNotPicked(picked, false);
                }
            }
        }

        // 자리배치 이름으로 매핑
        for (int r = 0; r < SeatInfo.ROW; r++) {
            for (int c = 0; c < SeatInfo.COL; c++) {
                if (students[r][c] == null || students[r][c].getName().isBlank())
                    seat[r][c] = "빈자리";
                else
                    seat[r][c] = students[r][c].getName();
            }
        }

        // 좌측 최후방 자리 이동(기둥 때문에)
        swap(seat, 3, 0, 3, 2);
        swap(seat, 3, 1, 3, 3);

        return seat;
    }

    // 현재 자리 파일에서 가져오기
    public String[][] getCurrentSeat() {
        String[][] seats = new String[4][8];
        String filePath = "src/database/seat.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 4) {
                String[] tokens = line.split("#");
                for (int col = 0; col < tokens.length && col < 8; col++) {
                    seats[row][col] = tokens[col];
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return seats;
    }

    // 현재 자리 저장
    public void saveCurrentSeat() {
        db.saveCurrentSeat();
    }

    // =========================== 로컬 함수 ===========================

    private void swap(String[][] arr, int r1, int c1, int r2, int c2) {
        String temp = arr[r1][c1];
        arr[r1][c1] = arr[r2][c2];
        arr[r2][c2] = temp;
    }

    private Student getRandStudentNotPicked(ArrayList<Integer> picked, boolean glassCondition) throws SQLException {
        Student student = db.getRandomStudent(glassCondition, picked);
        if (student == null) {
            System.out.println("⚠️ 더 이상 뽑을 학생이 없습니다! 빈자리로 처리합니다.");
            return new Student(0, "빈자리", 0, "", false);
        }
        picked.add(student.getNo());
        return student;
    }

    private Student getParentStudentNotPicked(int no, ArrayList<Integer> picked) throws SQLException {
        Student student = db.getPartnerStudentByNo(no, picked);
        if (student == null) {
            System.out.println("⚠️ 더 이상 배치할 학생이 없습니다! 빈자리로 처리합니다.");
            return new Student(0, "빈자리", 0, "", false);
        }
        picked.add(student.getNo());
        return student;
    }
}
