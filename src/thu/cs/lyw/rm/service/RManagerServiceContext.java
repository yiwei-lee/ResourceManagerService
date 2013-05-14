package thu.cs.lyw.rm.service;

import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import thu.cs.lyw.rm.manager.RManager;

public class RManagerServiceContext extends HttpServlet{
	private static final long serialVersionUID = 6437708570561192994L;
	public static HashMap<String, RManager> managerMap;
	
	public void init(){
		System.out.println("---Initializing system context---");
		managerMap = new HashMap<String, RManager>();
	}
	public void destroy(){
		System.out.println("---Neutralizing system context---");
	}
}
