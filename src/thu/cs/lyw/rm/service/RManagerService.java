package thu.cs.lyw.rm.service;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import thu.cs.lyw.rm.util.Provider;

@Path("/")
public class RManagerService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Provider> getProviders(){
		List<Provider> providers = new ArrayList<Provider>();
		
		return providers;
	}
}
