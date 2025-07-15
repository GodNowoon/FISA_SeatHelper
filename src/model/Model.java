package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

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
    public ArrayList<Student> getStudents() {
        return db.getAllStudents();
    }

    // 2. 랜덤 자리 반환
    public String[][] getRandomSeat() throws SQLException {
        String[][] seat = new String[SeatInfo.ROW][SeatInfo.COL];
        Student[][] students = new Student[SeatInfo.ROW][SeatInfo.COL];
        
        ArrayList<Integer> picked = new ArrayList<>();

    	// 중앙자리 먼저 배치
    	// 가로위치 홀수이면 바뀌긴 하는데 일단 놔둬
    	students[0][SeatInfo.COL/2-1] = getRandNotPicked(picked);
        students[0][SeatInfo.COL/2] = getRandNotPicked(picked);
        for(int r=1; r<SeatInfo.ROW; r++) {
        	students[r][0] = getRandNotPicked(picked);
            students[r][SeatInfo.COL-1] = getRandNotPicked(picked);
        }
        
        // 중앙자리로부터 좌우로 배치
        for(int r=0; r<SeatInfo.ROW; r++) {
        	for(int c=SeatInfo.COL/2+1; c<SeatInfo.COL; c++) {	// 우측자리
        		students[r][c] = getParentStudentNotPicked(students[r][c-1].getNo(), picked);
        	}
        	for(int c=SeatInfo.COL/2-2; c>=0; c--) {	// 좌측자리
        		students[r][c] = getParentStudentNotPicked(students[r][c+1].getNo(), picked);
        	}
        }
        
        // 자리배치 이름으로 매핑
        for(int r=0; r<SeatInfo.ROW; r++) {
        	for(int c=0; c<SeatInfo.COL; c++) {
        		if(students[r][c].getName() == "")
        			seat[r][c] = "빈자리";
        		else
            		seat[r][c] = students[r][c].getName();
        	}
        }
        
        // 좌측 최후방 자리이동(기둥땜에 빈자리 옮겨야함)
        swap(seat[3][0], seat[3][2]);
        swap(seat[3][1], seat[3][3]);
        
        return seat;
    }
    
    public String[][] getCurrentSeat() {
    	db.getCurrentSeat();
    }
    
    public void saveCurrentSeat() {
    	db.saveCurrentSeat();
    }
    
    //=================================== 로컬 함수 =================================================
    
    private void swap(String str1, String str2) {
    	String temp = str1;
    	str1 = str2;
    	str2 = temp;
    }
    
    private Student getRandStudentNotPicked(ArrayList<Integer> picked) {
    	Student student = db.getRandomStudent(true, picked);
    	picked.add(student.getNo());
    	return student;
    }
    
    private Student getParentStudentNotPicked(int no, ArrayList<Integer> picked) {
    	Student student = getPartnerStudentByNo(no, picked);
		picked.add(student.getNo());
		return student;
    }
}
