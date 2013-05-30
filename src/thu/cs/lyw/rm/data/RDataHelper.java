package thu.cs.lyw.rm.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import thu.cs.lyw.rm.manager.RManager;
import thu.cs.lyw.rm.manager.RTask;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.service.RManagerServiceContext;
import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.RStringGenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RDataHelper {
	private static final String DRIVER = "com.mysql.jdbc.Driver"; 
	private static final String URL = "jdbc:mysql://localhost/rmdb";
	private static RDataHelper helper;
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	private static Gson gson;
	
	private RDataHelper(){
		gson = new Gson();
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
	//General functions;
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
	private static ResultSet executeQuery(String query){
		rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		}
		return rs;
	}
	public static void executeUpdate(String query){
		try {
			conn = getConnection();
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
	public static JSONObject toJson(Object object){
		if (gson == null) gson = new Gson();
		String json = gson.toJson(object);
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static JSONObject toJsonWithAnnotation(Object object) {
		Gson gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson2.toJson(object);
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static <T> T fromJson(String json, Class<T> classOfT){
		if (gson == null) gson = new Gson();
		T object = gson.fromJson(json, classOfT);
		return object;
	}
	//Provider-specific functions;
	public static void addProvider(String uid, JSONObject provider){
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("insert into provider(Id, UserId, Data) values(?, ?, ?)");
			String id = "p-"+RStringGenerator.getString(8);
			while (isExist(id, "provider")){
				id = "p-"+RStringGenerator.getString(8);
			}
			pstmt.setString(1, id);
			pstmt.setString(2, uid);
			pstmt.setString(3, provider.toString());
			pstmt.executeUpdate();
			RManager manager = RManagerServiceContext.managerMap.get(uid);
			if (manager == null) manager = new RManager(uid);
			manager.addProvider(gson.fromJson(provider.toString(), Provider.class));
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
	public static JSONObject getProvider(String uid, String pid){
		JSONObject json = null;
		String provider = null;
		try {
			rs = executeQuery("select data from provider where Id = " + pid + " and UserId = " + uid);
			if (rs.next()){
				provider = rs.getString(1);
			} else {
				provider = "User: " + uid +"has no provider with id : " + pid;
			}
			json = new JSONObject(provider);
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.print("JSON Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return json;
	}
	public static JSONObject getProviders(String uid){
		JSONObject json = null;
		StringBuilder list = new StringBuilder();
		list.append("[");
		try {
			rs = executeQuery("select data from provider where UserId = " + uid);
			while (rs.next()){
				list.append(rs.getString(1)).append(",");
			}
			list.append("]");
			json = new JSONObject().append("providers", new JSONArray(list.toString()));
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.print("JSON Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return json;
	}
	//RNode-specific functions;
	public static JSONObject createNode(String uid, JSONObject json){
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("insert into node(Id, UserId, Data) values(?, ?, ?)");
			String id = "n-" + RStringGenerator.getString(8);
			while (isExist(id, "node")){
				id = "n-" + RStringGenerator.getString(8);
			}
			pstmt.setString(1, id);
			pstmt.setString(2, uid);
			pstmt.setString(3, json.toString());
			pstmt.executeUpdate();
			RManager manager = RManagerServiceContext.managerMap.get(uid);
			if (manager == null){
				return RDataHelper.toJson("No provider added for user with user id "+uid);
			}
			RTask task = RDataHelper.fromJson(json.toString(), RTask.class);
			RNode node = manager.getNode(task);
			return RDataHelper.toJsonWithAnnotation(node);
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
		return toJson("Failed to create node.");
	}
	public static void releaseNode(String uid, String nid){
		return;
	}
	public static JSONObject getNodes(String uid){
		JSONObject json = null;
		StringBuilder list = new StringBuilder();
		list.append("[");
		try {
			rs = executeQuery("select data from node where UserId = " + uid);
			while (rs.next()){
				list.append(rs.getString(1)).append(",");
			}
			list.append("]");
			json = new JSONObject().append("nodes", new JSONArray(list.toString()));
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.print("JSON Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return json;
	}
	public static JSONObject getNode(String uid, String nid){
		JSONObject json = null;
		String node = null;
		try {
			rs = executeQuery("select data from node where Id = " + nid + " and UserId = " + uid);
			if (rs.next()){
				node = rs.getString(1);
			}
			assert(node != null);
			json = new JSONObject(node);
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.print("JSON Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return json;
	}
	public static JSONObject getNodeProvider(String uid, String nid){
		JSONObject json = null;
		String provider = null;
		try {
			rs = executeQuery("select providerId from node where Id = " + nid);
			if (rs.next()){
				provider = rs.getString(1);
			}
			rs = executeQuery("select data from provider where Id = " + provider);
			if (rs.next()){
				provider = rs.getString(1);
			}
			json = new JSONObject(provider);
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.print("JSON Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return json;
	}
	//Functions for initialization;
	public static HashMap<String, RManager> loadUserData(){
		HashMap<String, RManager> userData = new HashMap<String, RManager>();
		String uid;
		String provider;
		RManager manager = null;
		try {
			rs = executeQuery("select * from provider");
			while (rs.next()){
				uid = rs.getString(2);
				provider = rs.getString(3);
				if ((manager = userData.get(uid)) == null){
					manager = new RManager(uid);
					manager.addProvider(gson.fromJson(provider, Provider.class));
					userData.put(uid, manager);
				} else {
					manager.addProvider(gson.fromJson(provider, Provider.class));
				}
			}
		} catch (SQLException e) {
			System.err.print("SQL Error : ");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return userData;
	}
	//Utilities;
	private static boolean isExist(String id, String tableName){
		boolean isExist = false;
		String query = "select * from " + tableName + "where Id = " + id;
		try {
			rs = executeQuery(query);
			if (rs.next() == false) isExist = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (rs != null) rs.close();
			} catch (SQLException e){
				System.err.print("SQL Error : ");
				e.printStackTrace();
			}
		}
		return isExist;
	}
}
