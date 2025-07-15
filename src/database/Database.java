package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import info.SeatInfo;

public class Database {
	
	public void saveCurrentSeatToDB() throws SQLException {
		//text파일 가져오기    
		String[][] seat = getCurrentSeat();
	    int total = SeatInfo.ROW * SeatInfo.COL;

	    // 1차원 이름 리스트 만들기
		ArrayList<String> names = new ArrayList<>();
		for (int i = 0; i < SeatInfo.ROW; i++) {
			for (int j = 0; j < SeatInfo.COL; j++) {

				if (seat[i][j].equals("빈자리"))
					continue;

				names.add(seat[i][j]);

			}
		}
		
		String placeholders = String.join(", ", Collections.nCopies(30, "?"));

		String sql = "SELECT * FROM student " +
		             "WHERE name IN (" + placeholders + ") " +
		             "ORDER BY FIELD(name, " + placeholders + ")";

		ArrayList<Integer> seatList = new ArrayList<>();

	    try (Connection conn = DBUtil.getConnection();
	            PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        int index = 1;
	        for (String name : names) pstmt.setString(index++, name); // IN (...)
	        for (String name : names) pstmt.setString(index++, name); // FIELD (...)

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            seatList.add(rs.getInt("no"));
	        }
	    }

	    
	}

	public ArrayList<Student> getAllStudents() throws SQLException {

		String sql = "select * from student";

		ArrayList<Student> students = null;

		try (Connection conn = DBUtil.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {

			students = new ArrayList<>();
			
			Student student = null;
			
			while (rs.next()) {
				
				student = new Student(
						rs.getInt("no"), 
						rs.getString("name"), 
						rs.getInt("age"), 
						rs.getString("mbti"),
						rs.getBoolean("glass"));

				students.add(student);
			}
		}

		return students;
	}

	public Student getPartnerStudentByNo(int no, ArrayList<Integer> picked) throws SQLException {

		ArrayList<Integer> excluded = new ArrayList<>(picked);
		excluded.add(no);

		// IN (?, ?, ?, ...) 생성
		String placeholders = excluded.stream().map(x -> "?").collect(Collectors.joining(", "));
 
		StringBuffer sqlBuffer = new StringBuffer();
	    sqlBuffer.append("SELECT s.no, s.name, s.age, s.mbti, s.glass ")
	             .append("FROM partner_history ph ")
	             .append("JOIN student s ON ph.partner_no = s.no ")
	             .append("WHERE ph.student_no = ? ");

	    if (!excluded.isEmpty()) {
	        sqlBuffer.append("AND ph.partner_no NOT IN (").append(placeholders).append(") ");
	    }

	    sqlBuffer.append("GROUP BY ph.partner_no ")
	             .append("ORDER BY COUNT(*) ASC, ph.partner_no ASC ")
	             .append("LIMIT 1");

		
		Student student = null;

		try (Connection conn = DBUtil.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sqlBuffer.toString())) {

			int index = 1;
			pstmt.setInt(index++, no);

			for (Integer ex : excluded) {
				pstmt.setInt(index++, ex);
			}

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				student = new Student(
						rs.getInt("no"),
						rs.getString("name"), 
						rs.getInt("age"), 
						rs.getString("mbti"),
						rs.getBoolean("glass"));
			}
		}

		return student;
	}

	
	public Student getRandomStudent(boolean glass, ArrayList<Integer> picked) throws SQLException {
	    StringBuffer sqlBuffer = new StringBuffer();
	    sqlBuffer.append("SELECT * FROM student ");

        String placeholders = picked.stream().map(x -> "?").collect(Collectors.joining(", "));
        
	    if (glass) {
	        sqlBuffer.append("WHERE glass = ? ");
	        if (picked != null && !picked.isEmpty()) {
	            sqlBuffer.append("AND no NOT IN (").append(placeholders).append(") ");
	        }
	    } else {
	        if (picked != null && !picked.isEmpty()) {
	            sqlBuffer.append("WHERE no NOT IN (").append(placeholders).append(") ");
	        }
	    }

	    sqlBuffer.append("ORDER BY RAND() LIMIT 1");

	    Student student = null;

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sqlBuffer.toString())) {

	        int index = 1;

	        if (glass) {
	            pstmt.setBoolean(index++, true);
	        }

	        if (picked != null && !picked.isEmpty()) {
	            for (Integer p : picked) {
	                pstmt.setInt(index++, p);
	            }
	        }

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                student = new Student(
	                        rs.getInt("no"),
	                        rs.getString("name"),
	                        rs.getInt("age"),
	                        rs.getString("mbti"),
	                        rs.getBoolean("glass")
	                );
	            }
	        }
	    }

	    return student;
	}

	public String[][] getCurrentSeat() {
		String[][] seats = new String[SeatInfo.ROW][SeatInfo.COL];

		try (BufferedReader br = new BufferedReader(new FileReader(SeatInfo.SEATFILE))) {
			String line;
			int row = 0;
			while ((line = br.readLine()) != null && row < SeatInfo.ROW) {
				String[] tokens = line.split("#");
				for (int col = 0; col < tokens.length && col < SeatInfo.COL; col++) {
					seats[row][col] = tokens[col];
				}
				row++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return seats;
	}


}

