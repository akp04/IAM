
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import sailpoint.api.SailPointContext;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.rest.plugin.RequiredRight;


@Path("basicplugin/rest/services")
@RequiredRight("basicPluginRight")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BasicPluginRestManager extends BasePluginResource
{
	
	private SailPointContext context;
	private Response response;

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "Basic Plugin";
	}
	
	
	@GET
	@Path("/ping")
	public Response getPingResponse()
	{
		String responseString = "Service is up!";
		response = Response.
					status(Response.Status.OK).
					header("Access-Control-Allow-Origin", "*").
					header("Access-Control-Allow-Credentials", "true").
					header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization").
					header("Access-Control-Allow-Mehods", "GET").
					entity(responseString).
					build();
		
		return response;
		
	}
	
}