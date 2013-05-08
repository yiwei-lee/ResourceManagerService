package thu.cs.lyw.rm.adapter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@SuppressWarnings("unused")
public class VMwareTest {
	private String host;
	private String username;
	private String password;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		JSch jsch = new JSch();
		jsch.addIdentity("RM.OpenStack.Test.pem");
		Session session = jsch.getSession("ubuntu", "15.185.101.194", 22);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		Channel channel = session.openChannel("shell");
		channel.setOutputStream(System.out);
		channel.setInputStream(System.in);
		channel.connect();
	}
}
