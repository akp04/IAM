package sailpointproject;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;

/*
 * Author 	: Aakash Pandita
 * Arguments 	: identity, context
 * Output	: we need to create a plan for Joiner - Life Cycle Event (LCE)
 */
public class CreateJoinerPlan 
{
	ProvisioningPlan plan = null;
	public ProvisioningPlan execute(Identity identity, SailPointContext context)
	{
		// other accesses are also given via Birthright roles
		// step 1: provision default user account , in most cases its an AD office account.
		plan = new ProvisioningPlan();
		plan.setIdentity(identity);
		
		createPlan();
		
		return plan;
	}
	
	private void createPlan()
	{
		// get the default application whose account is to be provisioned on the first day of user
		
		final String DEFAULT_APP = "AD_OFFICE_APP"; // can be fetched from a Custom Object
		final String ATTRIBUTE_NAME_FOR_APP_ENTITLEMENT = "memberOf"; // can be fetched from a Custom Object
		
		// get list of accesses to be provisioned along with account
		List<String> ZERO_DAY_ACCESS = new ArrayList(); // can be fetched from a Custom Object
		
		AccountRequest accReq = new AccountRequest();
		accReq.setApplication(DEFAULT_APP);
		accReq.setOperation(AccountRequest.Operation.Create);
		
		List<AttributeRequest> attReqs = new ArrayList();
		for(String val : ZERO_DAY_ACCESS)
		{
			attReqs.add( new AttributeRequest(ATTRIBUTE_NAME_FOR_APP_ENTITLEMENT, ProvisioningPlan.Operation.Add, val , null) );
		}		
		accReq.setAttributeRequests(attReqs);
		
		plan.add(accReq);
	}
	
}
