package thu.cs.lyw.rm.service;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.ProviderType;

public class RManagerServiceTest {
	public static void main(String[] args) {
		Client client = Client.create();
		WebResource webResource;
		
		Provider ec2Provider = new Provider(ProviderType.EC2);
		ec2Provider.addProperty("accessKey", "AKIAJOCSWO3APJUNZEGQ");
		ec2Provider.addProperty("secretKey", "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");
		Provider openStackProvider = new Provider(ProviderType.OpenStack);
		openStackProvider.addProperty("username", "lywthu");
		openStackProvider.addProperty("password", "liyiwei!@#900614");
		
		webResource = client.resource("http://localhost/resource_manager/provider");
		JSONObject json;
		try {
			json = new JSONObject().append("type", "EC2").
					append("username", "lywthu").
					append("password", "liyiwei!@#900614");
			ClientResponse response = webResource.type("application/json").accept("application/json")
					.post(ClientResponse.class, json);
			json = response.getEntity(JSONObject.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
