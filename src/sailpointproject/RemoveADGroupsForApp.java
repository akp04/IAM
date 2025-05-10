package sailpointproject;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;


/*
 * Author 	 : Aakash Pandita
 * output 	 : we need to remove all groups for given AD App and Identity
 * Arguments : applicationName, username
 */
public class RemoveADGroupsForApp 
{
	
	ProvisioningPlan plan = null;

	public void execute(SailPointContext context, String appName, String user) throws GeneralException
	{
		// fetch the application and identity object
		Application app = context.getObjectByName(Application.class, appName);
		Identity identity = context.getObjectByName(Identity.class, user);
		
		// get the specific account object/link for identity of given app
		List<Link> links = identity.getLinks(app);		
		for(Link link :  links)
		{
			plan = new ProvisioningPlan();
			List<String> listOfGroups = (List<String>) link.getAttribute("memberOf");
			plan = createPlan(identity, app, link, listOfGroups);
			
			Provisioner provisioner = new Provisioner(context);
			provisioner.execute(plan);
		}
	}
	
	public ProvisioningPlan createPlan(Identity identity, Application app, Link link, List<String> listOfGroups)
	{
		// set identity on which plan has to act upon
		plan.setIdentity(identity);
		
		// set account request on which identity account/link the action has to be taken
		AccountRequest accReq = new AccountRequest();
		accReq.setApplication(app.getName());
		accReq.setNativeIdentity(link.getNativeIdentity());
		accReq.setOperation(AccountRequest.Operation.Modify);
		
		// set attribute request for each group to be removed
		List<AttributeRequest> attReqs = new <AttributeRequest>ArrayList();
		for(String val : listOfGroups)
		{
			attReqs.add( new AttributeRequest("memberOf", ProvisioningPlan.Operation.Remove, val , null) );
		}
		
		accReq.setAttributeRequests(attReqs);
		plan.add(accReq);
		
		return plan;
	}
}
