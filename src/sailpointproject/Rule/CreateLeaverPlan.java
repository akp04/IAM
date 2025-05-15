package sailpointproject.Rule;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

/*
 * Author 	: Aakash Pandita
 * Arguments 	: identity, context
 * Output	: we need to create a plan for Leaver UseCase - Life Cycle Event (LCE)
 */
public class CreateLeaverPlan 
{
	
	// step 1 : disable account and remove access
	// step 2 : remove workgroups
	// step 3 : remove roles
	
	final String AUTHORITATIVE_APP = "HR App";
	final String AD_APP_TYPE = "Active Directory - Direct";
	ProvisioningPlan plan = new ProvisioningPlan();
	
	public void execute(SailPointContext context, Identity identity) throws GeneralException
	{
		plan.setIdentity(identity);
		List<Link> links = identity.getLinks();
		String ENTITLEMENT_ATTRIBUTE = "";
		
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
			
			// step 1 - if application type is AD
			if(app.getType().equalsIgnoreCase(AD_APP_TYPE))
			{
				ENTITLEMENT_ATTRIBUTE = "memberOf";
				AccountRequest accReq = new AccountRequest( AccountRequest.Operation.Disable, appName, null, nativeId );
				plan.add(accReq);
				
				accReq = new AccountRequest();
				accReq.setOperation(AccountRequest.Operation.Modify);
				accReq.setNativeIdentity(nativeId);
				accReq.setApplication(appName);
				
				// fetch all the entitlements for the identity of this application
				List<AttributeRequest> attrReqs = getAttributeList(identity, appName, ENTITLEMENT_ATTRIBUTE);
				accReq.setAttributeRequests(attrReqs);
				plan.add(accReq);
				
			}			
			// if application type is something else then need to generate plan according to the application
			
			
			// step 2 - remove workgroups
			List<Identity> wgs = identity.getWorkgroups();
			if( null != wgs)
			{
				for(Identity wg : wgs)
				{
					
				}
			}
			
			
			// step 3 - remove roles
			
		}
	}

	private List<AttributeRequest> getAttributeList(Identity identity, String appName, String ENTITLEMENT_ATTRIBUTE) 
	{
		// TODO Auto-generated method stub
		Link link = identity.getLink(appName);
		List<AttributeRequest> attrReqs = new ArrayList();
		List<String> groups = (List<String>) link.getAttribute(ENTITLEMENT_ATTRIBUTE);
		
		for(String grp : groups )
		{
			attrReqs.add(new AttributeRequest( ENTITLEMENT_ATTRIBUTE, ProvisioningPlan.Operation.Remove, grp  ));
		}

		return attrReqs;
	}

}
