package thu.cs.lyw.rm.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RDataHelper {
	private static final String DRIVER = "com.mysql.jdbc.Driver"; 
	private static final String URL = "jdbc:mysql://localhost/rmdb";
	private static RDataHelper helper;
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	
	private RDataHelper(){
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT VERSION()");
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
            
		} catch (ClassNotFoundException e) {
			System.err.print("Java Error : ");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		
	}
	public void getConnection(){
		if (helper == null) helper = new RDataHelper();
	}
	public static void main(String[] args){
		helper = new RDataHelper();
	}
}
