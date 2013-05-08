package thu.cs.lyw.rm.adapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import thu.cs.lyw.rm.evaluation.REvaluation;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.util.NodeType;
import thu.cs.lyw.rm.util.Provider;

public class OpenStackAdapter extends RAdapter{
	private static Client client  = Client.create();
	private static WebResource webResource;
	private static MessageDigest md5 = null;
	public OpenStackAdapter(){
		if (md5 == null){
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void initProvider(Provider provider) {
		if (provider.isInit()) return;
		else provider.setInit();
		try{
			//First step : get token;
			webResource = client
					.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tokens");
			String input = "{\"auth\":{\"passwordCredentials\":{\"username\":" +
					"\"" + provider.getProperty("username") + "\",\"password\":" +
					"\"" + provider.getProperty("password") + "\"}}}";
			ClientResponse response = webResource.type("application/json").accept("application/json")
					.post(ClientResponse.class, input);
			checkResponseCode(response, 200);
			JSONObject json = response.getEntity(JSONObject.class);
			String token = json.getJSONObject("access").getJSONObject("token").getString("id");
			
			//Second step : get tenant ID;
			webResource = client.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tenants");
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.header("Conection", "keep-alive")
					.get(ClientResponse.class);
			checkResponseCode(response, 200);
			json = response.getEntity(JSONObject.class);
			String tenantId = json.getJSONArray("tenants").getJSONObject(0).getString("id");
			
			//Third step : get compute address header;
			webResource = client.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tokens");
			input = "{\"auth\":{\"passwordCredentials\":{\"username\":" +
					"\"" + provider.getProperty("username") + "\",\"password\":" +
					"\"" + provider.getProperty("password") + "\"},\"tenantId\":" +
					"\""+tenantId+"\"}}";
			response = webResource.type("application/json").accept("application/json")
					.post(ClientResponse.class, input);
			checkResponseCode(response, 200);
			json = response.getEntity(JSONObject.class);
			token = json.getJSONObject("access").getJSONObject("token").getString("id");
			String computeHeader = null;
			JSONArray jsonArray = json.getJSONObject("access").getJSONArray("serviceCatalog");
			for (int i = 0 ; i < jsonArray.length() ; i++){
				json = jsonArray.getJSONObject(i);
				if (!json.getString("type").equals("compute")) continue;
				else{
					computeHeader = (json.getJSONArray("endpoints").getJSONObject(0).getString("publicURL"));
				}
			}
			assert(computeHeader != null);
			provider.addProperty("token", token);
			provider.addProperty("header", computeHeader);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RNode getNodeFromProvider(Provider provider, REvaluation evaluation){
		if (!provider.isInit()) initProvider(provider);
		RNode node = new RNode(provider);
		String token = (String) provider.getProperty("token");
		String header = (String) provider.getProperty("header");
		webResource = client.resource(header + "/servers");
		String flavor = "100";
		if (evaluation.type == NodeType.MICRO) flavor = "100";
		try {
			String hexedDate = new HexBinaryAdapter().marshal(md5.digest(new Date().toString().getBytes()));
			JSONObject json = new JSONObject().put("server", new JSONObject()
					.put("flavorRef", flavor).put("imageRef", evaluation.image.getImageName()).put("name", "server-" + hexedDate)
					.put("min_count", "1").put("max_count", "1")
					.put("key_name", evaluation.task.keyName));
//					.put("security_groups",new JSONArray().put(new JSONObject().put("name", evaluation.task.securityGroup))));
			ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.post(ClientResponse.class, json);
			json = response.getEntity(JSONObject.class);
			String serverId = json.getJSONObject("server").getString("id");
			//Get IP;
			System.out.print("OpenStack : Building server...");
			while (!getServerStatus(token, header, serverId).equals("ACTIVE")){
				Thread.sleep(2500);
			}
			System.out.println();
			webResource = client.resource(header + "/servers/"+serverId+"/ips");
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.get(ClientResponse.class);
			json = response.getEntity(JSONObject.class);
			String ip;
			ip = json.getJSONObject("addresses").getJSONArray("private").getJSONObject(1).getString("addr");
			System.out.println("OpenStack : Public IP of the newly created instance is : " + ip + ".");
			node.setIP(ip);
			node.addProperty("serverId", serverId);
			return node;
		} catch (JSONException e) {
			System.err.println(provider.getProperty("token"));
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void releaseNodeFromProvider(RNode node) {
		Provider provider = node.getProvider();
		String token = (String) provider.getProperty("token");
		String header = (String) provider.getProperty("header");
		String serverId = (String) node.getProperty("serverId");
		webResource = client.resource(header + "/servers/"+serverId);
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.delete(ClientResponse.class);
		checkResponseCode(response, 204);
		System.out.println("OpenStack : Public IP of the newly terminated instance is : " + node.getIP() + ".");
	}
	public JSONArray listFlavors(Provider provider) throws JSONException{
		String token = (String) provider.getProperty("token");
		String header = (String) provider.getProperty("header");
		webResource = client.resource(header + "/flavors/detail");
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		String output = response.getEntity(String.class);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("flavors");
		System.out.println(array.length() + " flavors found.");
		return array;
	}
	public JSONArray listImages(Provider provider) throws JSONException{
		String token = (String) provider.getProperty("token");
		String header = (String) provider.getProperty("header");
		webResource = client.resource(header + "/images");
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		String output = response.getEntity(String.class);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("images");
		System.out.println(array.length() + " images found.");
		return array;
	}
	private void checkResponseCode(ClientResponse response, int expectCode){
		if (response.getStatus() != expectCode) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}
	}
	private String getServerStatus(String token, String header, String serverId) throws JSONException{
		String status = null;
		webResource = client.resource(header + "/servers/"+serverId);
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		JSONObject json = response.getEntity(JSONObject.class);
		status = json.getJSONObject("server").getString("status");
		return status;
	}
}
