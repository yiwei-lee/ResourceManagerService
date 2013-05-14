package thu.cs.lyw.rm.service;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import thu.cs.lyw.rm.data.RDataHelper;
import thu.cs.lyw.rm.util.Provider;

@Path("/resource_manager")
public class RManagerService {
	@Path("/provider")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Provider> getProviders(){
		ArrayList<Provider> providers = new ArrayList<Provider>();
		ArrayList<Object> objects = RDataHelper.
				getObjects("select data from provider");
		for (Object object : objects){
			providers.add((Provider)object);
		}
		return providers;
	}
	@Path("/provider/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Provider getProvider(@PathParam("id") String id){
		Provider provider;
		ArrayList<Object> objects = RDataHelper.
				getObjects("select data from provider where Id = " + id);
		provider = (Provider) objects.get(0);
		return provider;
	}
	@Path("/provider")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addProvider(Provider provider){
		RDataHelper.insertObject("provider", provider);
	}
	@Path("/provider/{id}")
	@DELETE
	public void delProvider(@PathParam("id") String id){
		RDataHelper.executeUpdate("delete from provider where Id = " + id);
	}
}
