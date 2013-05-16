package thu.cs.lyw.rm.service;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import thu.cs.lyw.rm.manager.RManager;
import thu.cs.lyw.rm.manager.RTask;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.ProviderType;

@SuppressWarnings("unused")
public class RManagerServiceTest {
	public static void main(String[] args) throws JSONException {
		Client client = Client.create();
		WebResource webResource;
		Provider ec2Provider = new Provider(ProviderType.EC2);
		ec2Provider.addProperty("accessKey", "AKIAJOCSWO3APJUNZEGQ");
		ec2Provider.addProperty("secretKey", "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");
		Provider openStackProvider = new Provider(ProviderType.OpenStack);
		openStackProvider.addProperty("username", "lywthu");
		openStackProvider.addProperty("password", "liyiwei!@#900614");
		RManager manager = new RManager("lywthu");
		manager.addProvider(ec2Provider);
		RNode node = new RNode(ec2Provider);
		node.addProperty("instanceId", "i-9999999");
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		System.out.println(new JSONObject(gson.toJson(ec2Provider)).toString(4));
//		System.out.println(new JSONObject(gson.toJson(node)).toString(4));
//		System.out.println(gson.toJson(openStackProvider));
//		RTask task1 = new RTask(ProviderType.OpenStack, "84536", "MICRO");
//		RTask task2 = new RTask(ProviderType.EC2, "ami-56e6a404", "MICRO");
//		System.out.println(new JSONObject(gson.toJson(task1)).toString(4));
//		System.out.println(new JSONObject(gson.toJson(task2)).toString(4));
//		webResource = client.resource("http://localhost/resource_manager/provider");
//		JSONObject json;
//		try {
//			json = new JSONObject().append("type", "EC2").
//					append("username", "lywthu").
//					append("password", "liyiwei!@#900614");
//			ClientResponse response = webResource.type("application/json").accept("application/json")
//					.post(ClientResponse.class, json);
//			json = response.getEntity(JSONObject.class);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
}
