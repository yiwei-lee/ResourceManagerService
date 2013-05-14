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
import thu.cs.lyw.rm.manager.RManager;
import thu.cs.lyw.rm.manager.RTask;
import thu.cs.lyw.rm.resource.RNode;

@Path("")
public class RManagerService {
	//Provider-specific resources;
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
	
	//RNode-specific resources;
	@Path("/node/{uid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject createNode(JSONObject json, @PathParam("uid") String uid){
		RManager manager = RManagerServiceContext.managerMap.get(uid);
		if (manager == null){
			return RDataHelper.toJson("No provider added for user with uid "+uid);
		}
		RTask task = RDataHelper.fromJson(json.toString(), RTask.class);
		RNode node = manager.getNode(task);
		return RDataHelper.toJson(node);
	}
	@Path("/node/{uid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNodes(JSONObject json, @PathParam("uid") String uid){
		//TODO
		return null;
	}
}
