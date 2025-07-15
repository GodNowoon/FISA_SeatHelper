package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    public ArrayList<Student> getStudents() throws SQLException {
        return db.getAllStudents();
    }

    // 2. 랜덤 자리 반환
    public String[][] getRandomSeat() throws SQLException {
        String[][] seat = new String[SeatInfo.ROW][SeatInfo.COL];
        Student[][] students = new Student[SeatInfo.ROW][SeatInfo.COL];
        
        ArrayList<Integer> picked = new ArrayList<>();

    	// 중앙자리 먼저 배치
    	// 가로위치 홀수이면 바뀌긴 하는데 일단 놔둬
    	students[0][SeatInfo.COL/2-1] = getRandStudentNotPicked(picked);
        students[0][SeatInfo.COL/2] = getRandStudentNotPicked(picked);
        for(int r=1; r<SeatInfo.ROW; r++) {
        	students[r][0] = getRandStudentNotPicked(picked);
            students[r][SeatInfo.COL-1] = getRandStudentNotPicked(picked);
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
    
    public void saveCurrentSeat() {
    	db.saveCurrentSeat();
    }
    
    //=================================== 로컬 함수 =================================================
    
    private void swap(String str1, String str2) {
    	String temp = str1;
    	str1 = str2;
    	str2 = temp;
    }
    
    private Student getRandStudentNotPicked(ArrayList<Integer> picked) throws SQLException {
    	Student student = db.getRandomStudent(true, picked);
    	picked.add(student.getNo());
    	return student;
    }
    
    private Student getParentStudentNotPicked(int no, ArrayList<Integer> picked) throws SQLException {
    	Student student = db.getPartnerStudentByNo(no, picked);
		picked.add(student.getNo());
		return student;
    }
}
