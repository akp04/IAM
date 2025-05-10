package sailpointproject;

import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.tools.GeneralException;

public class RemoveADGroupsForApp 
{
	
	ProvisioningPlan plan = null;
	/*
	 * Author : Aakash Pandita
	 * output - we need to remove all groups for given AD App and Identity
	 * Arguments - applicationName, username
	 */
	public void execute(SailPointContext context, String appName, String user) throws GeneralException
	{
		// fetch the application and identity object
		Application app = context.getObjectByName(Application.class, appName);
		Identity identity = context.getObjectByName(Identity.class, user);
		
		// get the specific account object/link for identity of given app
		List<Link> links = identity.getLinks(app);		
		for(Link link :  links)
		{
			List<String> listOfGroups = (List<String>) link.getAttribute("memberOf");
			plan = createPlan(identity, app, listOfGroups);
		}
	}
	
	public ProvisioningPlan createPlan(Identity identity, Application app, List<String> listOfGroups)
	{
		
		
		return null;
		
	}

}
