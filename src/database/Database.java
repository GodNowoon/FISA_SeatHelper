package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Database {
	
	public void saveCurrentSeat() {
		// 코드 작성 필요
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

	    if (glass) {
	        sqlBuffer.append("WHERE glass = ? ");
	        if (picked != null && !picked.isEmpty()) {
	            String placeholders = picked.stream().map(x -> "?").collect(Collectors.joining(", "));
	            sqlBuffer.append("AND no NOT IN (").append(placeholders).append(") ");
	        }
	    } else {
	        if (picked != null && !picked.isEmpty()) {
	            String placeholders = picked.stream().map(x -> "?").collect(Collectors.joining(", "));
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


}

