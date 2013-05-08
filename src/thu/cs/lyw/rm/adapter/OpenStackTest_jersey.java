package thu.cs.lyw.rm.adapter;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class OpenStackTest_jersey {
	private static Client client  = Client.create();
	private static WebResource webResource;
	private static String computeHeader = null;
	private static String token = null;
	public static void main(String[] args) throws Exception {
		try{
			//First step : get token;
			webResource = client
					.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tokens");
			String input = "{\"auth\":{\"passwordCredentials\":{\"username\":" +
					"\"lywthu\",\"password\":\"liyiwei!@#900614\"}}}";
			ClientResponse response = webResource.type("application/json").accept("application/json")
					.post(ClientResponse.class, input);
			checkResponseCode(response, 200);
			JSONObject json = response.getEntity(JSONObject.class);
			token = json.getJSONObject("access").getJSONObject("token").getString("id");
			
			//Second step : get tenant ID;
			webResource = client.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tenants");
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.header("Conection", "keep-alive")
					.get(ClientResponse.class);
			checkResponseCode(response, 200);
			json = response.getEntity(JSONObject.class);
			String tenantId = json.getJSONArray("tenants").getJSONObject(0).getString("id");
			
			//Third step : get computer address header;
			webResource = client.resource("https://region-a.geo-1.identity.hpcloudsvc.com:35357/v2.0/tokens");
			input = "{\"auth\":{\"apiAccessKeyCredentials\":{\"accessKey\":" +
					"\"D7WAWP2LBYLGRZ9FCLNE\",\"secretKey\":\"+fmjdLc4UBvn/gsfEYWnD5AcbOuhEsP30SbJRc/c\"},\"tenantId\":" +
					"\""+tenantId+"\"}}";
			response = webResource.type("application/json").accept("application/json")
					.post(ClientResponse.class, input);
			checkResponseCode(response, 200);
			json = response.getEntity(JSONObject.class);
			token = json.getJSONObject("access").getJSONObject("token").getString("id");
			JSONArray jsonArray = json.getJSONObject("access").getJSONArray("serviceCatalog");
			for (int i = 0 ; i < jsonArray.length() ; i++){
				json = jsonArray.getJSONObject(i);
				if (!json.getString("type").equals("compute")) continue;
				else{
					computeHeader = (json.getJSONArray("endpoints").getJSONObject(0).getString("publicURL"));
				}
			}
			if (computeHeader == null) System.err.println("Something's wrong!");
			listFlavors();
			listImages();
			System.out.println("Tenant ID : " + tenantId);
			System.out.println("Token : " + token);
			//Final step : create a server;
			webResource = client.resource(computeHeader + "/servers");
			json = new JSONObject().put("server", new JSONObject().put("flavorRef", "100")
					.put("imageRef", "84536").put("name", "server-no-1").put("min_count", "1")
					.put("max_count", "1").put("key_name", "RM.OpenStack.Test"));
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.post(ClientResponse.class, json);
			json = response.getEntity(JSONObject.class);
			String serverId = json.getJSONObject("server").getString("id");
			System.out.println("Server ID : " + serverId);
			//Get ip;
			System.out.print("Building server");
			while (!getServerStatus(serverId).equals("ACTIVE")){
				System.out.print(".");
				Thread.sleep(5000);
			}
			System.out.println();
			webResource = client.resource(computeHeader + "/servers/"+serverId+"/ips");
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.get(ClientResponse.class);
			json = response.getEntity(JSONObject.class);
			String ip = json.getJSONObject("addresses").getJSONArray("private").getJSONObject(1).getString("addr");		
			System.out.println("Server IP : " + ip);
			//So...time to kill!
			webResource = client.resource(computeHeader + "/servers/"+serverId);
			response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
					.delete(ClientResponse.class);
			checkResponseCode(response, 204);
			System.out.println("Server deleted.");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private static void checkResponseCode(ClientResponse response, int expectCode){
		if (response.getStatus() != expectCode) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}
	}
	private static void listFlavors() throws JSONException{
		webResource = client.resource(computeHeader + "/flavors/detail");
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		String output = response.getEntity(String.class);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("flavors");
		System.out.println(array.length() + " flavors found.");
	}
	private static void listImages() throws JSONException{
		webResource = client.resource(computeHeader + "/images");
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		String output = response.getEntity(String.class);
		JSONObject json = new JSONObject(output);
		JSONArray array = json.getJSONArray("images");
		System.out.println(array.length() + " images found.");
	}
	private static String getServerStatus(String serverId) throws JSONException{
		String status = null;
		webResource = client.resource(computeHeader + "/servers/"+serverId);
		ClientResponse response = webResource.type("application/json").accept("application/json").header("X-Auth-Token", token)
				.get(ClientResponse.class);
		JSONObject json = response.getEntity(JSONObject.class);
		status = json.getJSONObject("server").getString("status");
		return status;
	}
}
