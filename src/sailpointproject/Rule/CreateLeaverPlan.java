package sailpointproject.Rule;

import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.tools.GeneralException;

/*
 * Author 	: Aakash Pandita
 * Arguments 	: identity, context
 * Output	: we need to create a plan for Leaver UseCase - Life Cycle Event (LCE)
 */
public class CreateLeaverPlan 
{
	
	// step 1 : raise account request to disable/delete accounts - loop through all apps
	// step 2 : remove all business roles, birthright roles will be revoked automatically
	// step 3 : create account request for modify and add attribute requests in it to remove access
	
	final String AUTHORITATIVE_APP = "HR App";
	final String AD_APP_TYPE = "Active Directory - Direct";
	ProvisioningPlan plan = new ProvisioningPlan();
	
	public void execute(SailPointContext context, Identity identity) throws GeneralException
	{
		plan.setIdentity(identity);
		List<Link> links = identity.getLinks();
		
		for(Link link : links)
		{
			String nativeId = link.getNativeIdentity();
			String appName = link.getApplicationName();
			Application app = context.getObjectByName(Application.class, appName);
			
			// check if its Authoritative app
			if(appName.equalsIgnoreCase(AUTHORITATIVE_APP)) 
			{
				continue;
			}
			
			// if application type is AD
			if(app.getType().equalsIgnoreCase(AD_APP_TYPE))
			{
				
			}
			
		}
	}
	
	
	// disable account and remove access
	
	
	
	// remove workgroups
	
	
	
	// remove roles
	
	
}
