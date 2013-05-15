package thu.cs.lyw.rm.service;

import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import thu.cs.lyw.rm.data.RDataHelper;
import thu.cs.lyw.rm.manager.RManager;

public class RManagerServiceContext extends HttpServlet{
	private static final long serialVersionUID = 6437708570561192994L;
	public static HashMap<String, RManager> managerMap;
	
	public void init(){
		System.out.println("---Initializing system context---");
		System.out.println("---Loading user data---");	
		managerMap = RDataHelper.loadUserData();
		System.out.println("---" + managerMap.size() + " users' data loaded---");	
	}
	public void destroy(){
		System.out.println("---Neutralizing system context---");
	}
}
