/* 모든 DAO 클래스들이 공통으로 사용하는 클래스
 * - DAO 권장 설계 : table당 1:1개발
 * - DBUtil의 생성자도 private로 객체 생성 금지 설정 권장
 * 
 * 24시간 365일 실행되는 서버의 실행 코드라 간주
 * - 요청 수와 무관하게 최초 단 한번만 공유자원 초기화 하는 공통 코드
 * - DB의 Driver 로딩 로직
 * - 시스템 다운 방지를 위한 리소스 최적화
 * 	: Connection 수는 절대 제한(유한자원)
 * 	- web server 내부에서 설정으로 db server 시스템 동시 접속수 제어 예정
 * 	- Conneciton Pool기법 (CP)
 * 	- Connection 제공, 회수하는 주체 필요(javax.sql.DataSource)
 * 
 * 	결론
 * 		- dirver로딩 한번만 실행 보장
 * 		- Connction 객체 반환하는 로직
 * 		- DB의 설정 정보는 별도의 Key로 db접속 정보 매핑해서 properties 파일로 분리
 * 		- 참고
 * 			: 설정 정보를 코드에서 분리하는 원조가 JDBC
 * 			 - Spring에선 default설정파일로 활용
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// file로부터 1byte 단위로 read하는 기능
import java.io.FileInputStream;

//properties 파일에서 key로 value만 추출하는 기능
import java.util.Properties;

/* DBUtil은 편집 불가능한 구조 권장
 */
public class DBUtil {
	//key로 value값 활용시에만 사용하는 API
	private static Properties dbInfo = new Properties();
	
	private DBUtil() {}
	
	//존재하는 파일로부터 자비 코드로 read하는 API
	static {
		try {
			dbInfo.load(new FileInputStream("dbinfo.properties"));
			Class.forName(dbInfo.getProperty("jdbc.driver"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 자원 반환 필수(query)
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 자원 반환 필수(insert/update/delete)
	public static void close(Connection conn, Statement stmt) {
		try {
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbInfo.getProperty("jdbc.url"), 
				dbInfo.getProperty("jdbc.id"),
				dbInfo.getProperty("jdbc.pw"));
	}
}
