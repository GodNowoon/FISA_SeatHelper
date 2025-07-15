package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Database {
	
	private static String studentFile = "src/database/student.txt";
	private static String seatFile = "src/database/seat.txt";

	private static ArrayList<Student> students = new ArrayList<>();
	private static int[][] seats = new int[4][8];
	
	static {
		BufferedReader br;
		
		try {
			// 학생정보 읽기
			FileReader fileReader = new FileReader(new File(studentFile));
			br = new BufferedReader(fileReader);
			
			String line = "";
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "#");
				
				int num = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				int age = Integer.parseInt(st.nextToken());
				String mbti = st.nextToken();
				boolean glass = (st.nextToken() == "TRUE");
								
				students.add(new Student(num, name, age, mbti, glass));
			}
			
			// 책상정보 읽기
			fileReader = new FileReader(new File(seatFile));
			br = new BufferedReader(fileReader);
			
			line = "";
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "#");
				
				for(int i=0;i<4;i++){
				int num = Integer.parseInt(st.nextToken());
					for(int j=0;j<8;j++) {
						seats[i][j]=num;
					}
				}
			}			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
//			br.close();
		}
	}
	
	public ArrayList<Student> getAllStudents() {
		return students;
	}
	
	public ArrayList<Student> getStudentsNamebyNumber() {
		return students;
	}
	
	public int[][] getAllSeat() {
		return seats;
	}
	
}
