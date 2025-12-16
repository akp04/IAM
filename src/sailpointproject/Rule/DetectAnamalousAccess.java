package sailpointproject.Rule;

import java.util.ArrayList;
import java.util.List;
import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

public class DetectAnamalousAccess 
{
	// Return whether a request which is requested by user is anamalous.
	// This means user might be trying to request for an access which is never been requested before or neither does any user related to this user have.
	// Related users are - manager, team mates, people in same department
	
	// get all the users from this users department
	// get users manager
	// get all team mates, which is, get all users who also report to same manager
	
	public boolean execute(SailPointContext context, ProvisioningPlan plan) throws GeneralException
	{
		// check for anamalous request from plan
		
		// step 1 : get all entitlements which are requested
		Identity identity = null;
		List<String> ents = new ArrayList<String>();
		if(null != plan)
		{
			identity = plan.getIdentity();
			ents = getEntitlementsFromPlan(context, plan);
		}
		
		// step 2 : get Manager
		Identity manager = identity.getManager();
		
		// step 3 : get team mates
		// use query and filter to get all users who report to the same manager
		
		
		return true;
	}
	
	public List<String> getEntitlementsFromPlan(SailPointContext context, ProvisioningPlan plan)
	{
		List<String> ret = new ArrayList<String>();
		List<AccountRequest> accReqs = plan.getAccountRequests();
		if(null != accReqs)
		{
			for(AccountRequest accReq : accReqs)
			{
				List<AttributeRequest> attrReqs = accReq.getAttributeRequests();
				if(null != attrReqs)
				{
					for(AttributeRequest attrReq : attrReqs)
					{
						ret.add(attrReq.getValue().toString());
					}
				}
			}
		}
		return ret;
	}
}
