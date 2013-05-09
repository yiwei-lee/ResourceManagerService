package thu.cs.lyw.rm.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import thu.cs.lyw.rm.util.Provider;

@Path("/resource_manager")
public class RManagerService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Provider> getProviders(){
		List<Provider> providers = new ArrayList<Provider>();
		
		return providers;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addProvider(Provider provider){
		
	}
}
