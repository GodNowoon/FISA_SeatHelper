package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;

public class Database {
	
	private static String studentFile = "src/database/student.txt";
	private static String seatFile = "src/database/seat.txt";

	private static ArrayList<Student> students = new ArrayList<>();
	private static int[][] seats = new int[4][8];
	
//    private static Session sshSession;
	
	static {
		try {
			// Oracle Driver 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
//			// SSH 터널링
//            String sshUser = "ubuntu";			// VM 사용자명
//            String sshHost = "localhost";		// VM IP
//            int sshPort = 22;
//
//            JSch jsch = new JSch();
//            sshSession = jsch.getSession(sshUser, sshHost, sshPort);
//            sshSession.setPassword("ubuntu"); // SSH 비밀번호
//            sshSession.setConfig("StrictHostKeyChecking", "no");
//            sshSession.connect(5000);
//
//            // SSH내부에서 Docker 연결
//            String remoteHost = "localhost";
//            int localPort = 15221;
//            final int remotePort = 1521;
//            
//            sshSession.setPortForwardingL(localPort, remoteHost, remotePort);
//
//            System.out.println("SSH 터널링 성공: localhost:" + localPort + " → " + remoteHost + ":" + remotePort);
			
			// 학생정보 읽기
			FileReader fileReader = new FileReader(new File(studentFile));
			BufferedReader br = new BufferedReader(fileReader);
			
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
//			closeSSHTunnel();
		}
	}
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
	}
	
	public ArrayList<Student> getAllStudents() {
		try(Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM emp");) {
			while(rs.next()) {
				int empno = rs.getInt("empno");
                String ename = rs.getString("ename");
                String job = rs.getString("job");
                int mgr = rs.getInt("mgr");
                Date hiredate = rs.getDate("hiredate");
                double sal = rs.getDouble("sal");
                double comm = rs.getDouble("comm");
                int deptno = rs.getInt("deptno");

                System.out.printf("%d | %s | %s | %d | %s | %.2f | %.2f | %d\n",
                    empno, ename, job, mgr, hiredate, sal, comm, deptno);
			}
			
		} catch(SQLException e) {
			System.out.println("조회실패");
			e.printStackTrace();
		}
		finally {
//			closeSSHTunnel();
		}
		
		return students;
	}
	
	public ArrayList<Student> getStudentsNamebyNumber() {
		return students;
	}
	
	public int[][] getAllSeat() {
		return seats;
	}
	
//    public static void closeSSHTunnel() {
//        if (sshSession != null && sshSession.isConnected()) {
//            sshSession.disconnect();
//            System.out.println("🔓 SSH 터널링 종료 완료");
//        }
//    }
}
