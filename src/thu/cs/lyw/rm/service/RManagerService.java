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
	public void addProvider(JSONObject provider, @PathParam("uid") String uid){
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
		RDataHelper.executeUpdate("update provider set Data = " + provider.toString() + " where Id = " + pid + " and UserId = " + uid);
	}
	@Path("/provider/{uid}/{pid}")
	@DELETE
	public void delProvider(@PathParam("uid") String uid, @PathParam("pid") String pid){
		RDataHelper.executeUpdate("delete from provider where Id = " + pid + " and UserId = " + uid);
	}
	
	//RNode-specific resources;
	@Path("/node/{uid}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject createNode(JSONObject json, @PathParam("uid") String uid){
		RManager manager = RManagerServiceContext.managerMap.get(uid);
		if (manager == null){
			return RDataHelper.toJson("No provider added for user with user id "+uid);
		}
		RTask task = RDataHelper.fromJson(json.toString(), RTask.class);
		RNode node = manager.getNode(task);
		return RDataHelper.toJsonWithAnnotation(node);
	}
	@Path("/node/{uid}/{nid}")
	@DELETE
	public void releaseNode(@PathParam("uid") String uid, @PathParam("nid") String nid){
		RManager manager = RManagerServiceContext.managerMap.get(uid);
		RNode node = RDataHelper.fromJson(RDataHelper.getNode(uid, nid).toString(), RNode.class);
		manager.releaseNode(node);
	}
	@Path("/node/{uid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNodes(@PathParam("uid") String uid){
		return RDataHelper.getNodes(uid);
	}
	@Path("/node/{uid}/{nid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNode(@PathParam("uid") String uid, @PathParam("nid") String nid){
		RManager manager = RManagerServiceContext.managerMap.get(uid);
		RNode node = RDataHelper.fromJson(RDataHelper.getNode(uid, nid).toString(), RNode.class);
		manager.checkNodeStatus(node);
		return RDataHelper.toJsonWithAnnotation(node);
	}
}
