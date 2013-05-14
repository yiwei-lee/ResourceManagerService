package thu.cs.lyw.rm.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import thu.cs.lyw.rm.data.RDataHelper;
import thu.cs.lyw.rm.util.Provider;
import thu.cs.lyw.rm.util.ProviderType;

@Path("")
public class RManagerService {
	@Path("/provider/{uid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addProvider(JSONObject provider, @PathParam("uid") int uid){
		RDataHelper.addProvider(uid, provider);
	}
	@Path("/provider/{uid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getProviders(@PathParam("uid") String uid){
		return RDataHelper.getProviders(uid);
	}
	@Path("/provider/{uid}/{pid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getProvider(@PathParam("uid") String uid, @PathParam("pid") String pid){
		return RDataHelper.getProvider(uid, pid);
	}
	@Path("/provider/{uid}/{pid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProvider(JSONObject provider, @PathParam("uid") String uid, @PathParam("pid") String pid){
		RDataHelper.executeUpdate("update provider set Data = " + provider.toString() + " where UserId = " + uid + " and Id = " + pid);
	}
	@Path("/provider/{uid}/{pid}")
	@DELETE
	public void delProvider(@PathParam("uid") String uid, @PathParam("pid") String pid){
		RDataHelper.executeUpdate("delete from provider where UserId = " + uid + " and Id = " + pid);
	}
}
