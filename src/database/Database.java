package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.junit.Test;

import info.SeatInfo;


public class Database {
//	private static String studentFile = "src/database/student.txt";
	private static String seatFile = "src/database/seat.txt";

//	private static ArrayList<Student> students = new ArrayList<>();
	private static String[][] currentSeat = new String[SeatInfo.ROW][SeatInfo.COL];
	
	static {
		try {
			// 학생정보 읽기
//			FileReader fileReader = new FileReader(new File(studentFile));
//			BufferedReader br = new BufferedReader(fileReader);
//			
//			String line = "";
//			while((line = br.readLine()) != null) {
//				StringTokenizer st = new StringTokenizer(line, "#");
//				
//				int no = Integer.parseInt(st.nextToken());
//				String name = st.nextToken();
//				int age = Integer.parseInt(st.nextToken());
//				String mbti = st.nextToken();
//				boolean glass = (st.nextToken() == "TRUE");
//								
//				students.add(new Student(no, name, age, mbti, glass));
//			}
			
			// 책상정보 읽기
			FileReader fileReader = new FileReader(new File(seatFile));
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = "";
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "#");
				
				for(int i=0;i<4;i++){
					String name = st.nextToken();
					for(int j=0;j<8;j++) {
						currentSeat[i][j] = name;
					}
				}
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[][] getCurrentSeat() {
		return currentSeat;
	}
	
	public void saveCurrentSeat() {
		
	}
	
	@Test
	public ArrayList<Student> getAllStudents() throws SQLException {
		
		String sql = "select * from student";
		
		ArrayList<Student> students = null;
		
		try(Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			ResultSet rs = pstmt.executeQuery();
			
			students = new ArrayList<>();
			while(rs.next()) {
				Student student = new Student();
				
	            student.no = rs.getInt("no");
	            student.name = rs.getString("name");
	            student.age = rs.getInt("age");
	            student.mbti = rs.getString("mbti");
	            student.glass = rs.getBoolean("glass");

	            students.add(student);
			}
		}
		
		return students;
	}
	
	public Student getPartnerStudentByNo(int no, ArrayList<Integer> picked) throws SQLException {

	    ArrayList<Integer> excluded = new ArrayList<>(picked);
	    excluded.add(no);

	    // IN (?, ?, ?, ...) 생성
	    String placeholders = excluded.stream()
	                                  .map(x -> "?")
	                                  .collect(Collectors.joining(", "));

		
	    String sql = "SELECT s.no, s.name, s.age, s.mbti, s.glass " +
	             "FROM partner_history ph " +
	             "JOIN student s ON ph.partner_no = s.no " +
	             "WHERE ph.student_no = ? " +
	             "AND ph.partner_no NOT IN (" + placeholders + ") " +
	             "GROUP BY ph.partner_no " +
	             "ORDER BY COUNT(*) ASC, ph.partner_no ASC " +
	             "LIMIT 1";
	    
	    Student student = null;

		
		try(Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			
			int index = 1;
	        pstmt.setInt(index++, no);
	        // 다음 파라미터들: picked + student_no
	        for (Integer ex : excluded) {
	            pstmt.setInt(index++, ex);
	        }
	
	        ResultSet rs = pstmt.executeQuery();
	        
            if (rs.next()) {
                student = new Student();
                student.no = rs.getInt("no");
                student.name = rs.getString("name");
                student.age = rs.getInt("age");
                student.mbti = rs.getString("mbti");
                student.glass = rs.getBoolean("glass");
            }
		}
		
		return student;
	}
	
	public Student getRandomStudent(boolean glass, ArrayList<Integer> picked) {
		return null;
	}
}
