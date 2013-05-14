package thu.cs.lyw.rm.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RDataHelper {
	private static final String DRIVER = "com.mysql.jdbc.Driver"; 
	private static final String URL = "jdbc:mysql://localhost/rmdb";
	private static RDataHelper helper;
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
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
	public static Connection getConnection(){
		if (helper == null) helper = new RDataHelper();
		try {
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			return conn;
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		}
		return null;
	}
	public static ResultSet executeQuery(String query){
		if (helper == null) helper = new RDataHelper();
		try {
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void executeUpdate(String query){
		if (helper == null) helper = new RDataHelper();
		try {
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
	}
	public static void insertObject(String table, Object object){
		if (helper == null) helper = new RDataHelper();
		try {
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			pstmt = conn.prepareStatement("INSERT INTO " + table + " VALUES(?)");
			pstmt.setObject(1, object);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
	}
	public static ArrayList<Object> getObjects(String query){
		if (helper == null) helper = new RDataHelper();
		ArrayList<Object> objectList = new ArrayList<Object>();
		try {
			conn = DriverManager.getConnection(URL, "rmdb", "il0veyou");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()){
				ObjectInputStream oips = new ObjectInputStream(rs.getBinaryStream(1));
				Object object = oips.readObject();
				objectList.add(object);
				oips.close();
			}
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException e) {
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
		return objectList;
	}
	public static void main(String[] args){
		helper = new RDataHelper();
	}
}
