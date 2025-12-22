package sailpointproject.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import sailpoint.api.SailPointContext;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.object.QueryOptions;
import sailpoint.tools.GeneralException;

public class DetectAnamalousAccess 
{
	// Return whether a request which is requested by user is anomalous.
	// This means user might be trying to request for an access which is never been requested before or neither does any user related to this user have.
	// Related users are - manager, team mates, people in same department
	
	// get all the users from this users department
	// get users manager
	// get all team mates, which is, get all users who also report to same manager
	
	HashSet<String> ALL_ACCESSES;
	public boolean execute(SailPointContext context, ProvisioningPlan plan) throws GeneralException
	{
		ALL_ACCESSES = new HashSet<String>();
		
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
		Filter f = Filter.eq(Identity.ATT_MANAGER , manager.getName());
		QueryOptions qo = new QueryOptions();
		qo.add(f);
		Iterator<Identity> itr = context.search(Identity.class, qo);
		while(itr.hasNext())
		{
			Identity member = (Identity) itr.next();
			addUserAccessToSet(member);
			itr.remove();
		}
		
		// step 3 : get team mates from same department
		// use query and filter to get all users who report to the same manager
		Filter f1 = Filter.eq("department" , identity.getAttribute("department"));
		qo = new QueryOptions();
		qo.add(f1);
		Iterator<Identity> itr1 = context.search(Identity.class, qo);
		while(itr1.hasNext())
		{
			Identity member = (Identity) itr1.next();
			addUserAccessToSet(member);
			itr1.remove();
		}
		
		// compare the accesses for this user's request with all accesses
		//************* here ********
		// compare ents with allAccesses
		
		return true;
	}
	
	public void addUserAccessToSet(Identity member)
	{
		
		//add entitlements to set
		//ALL_ACCESSES.add("");
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
