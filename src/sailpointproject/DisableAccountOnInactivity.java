package sailpointproject;

import java.util.Date;
import java.util.List;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.QueryOptions;
import sailpoint.tools.GeneralException;


/*
 * Author 	: Aakash Pandita
 * Arguments 	: applicationName, context
 * Output	: we need to find all the accounts/Links of identities which have not been active or logged-in from long time.
 */
public class DisableAccountOnInactivity 
{
	final int MAX_DAYS_FOR_INACTIVITY = 60;
	
	public void execute(SailPointContext context, String appName) throws GeneralException
	{
		ProvisioningPlan plan = null;
		Application app = context.getObjectByName(Application.class, appName);
		
		// here we are taking example of AD application and we will be using attributes - lastLoginTimeStamp, pwdLastSet
		List<Identity> identities = getActiveUsers(context, appName);		
		for(Identity identity : identities)
		{
			Link link = identity.getLink(app);
			
			// asssuming both attributes here are returning date in format of string
			// in actual they generally return dates in Date() format or Unix date time.
			// those need to be converted first to human readable format and then apply business logic for comparison
			String lastLoginTimeStamp = link.getAttribute("lastLoginTimeStamp").toString();
			String pwdLastSet = link.getAttribute("pwdLastSet").toString();
			
			if(null != lastLoginTimeStamp && null != pwdLastSet )
			{
				
				int diff = pwdLastSet.compareTo(lastLoginTimeStamp);
				if(diff < 0)
				{
					//  means pwdLastSet < lastLoginTimeStamp, so we will use the date which is latest (lastLoginTimeStamp)
					int diff2 = findDiffInDays(lastLoginTimeStamp, new Date().toString());
					// check if the dates exceed the given days for Inactivity
					if(diff2 > MAX_DAYS_FOR_INACTIVITY)
					{
						// create plan to disable account
						plan = createPlanToDisableAccount(context, link, identity);
					}
				}
				else
				{
					//  means pwdLastSet > lastLoginTimeStamp, so we will use the date which is latest (pwdLastSet)
					int diff2 = findDiffInDays(pwdLastSet, new Date().toString());
					if(diff2 > MAX_DAYS_FOR_INACTIVITY)
					{
						// create plan to disable account
						plan = createPlanToDisableAccount(context, link, identity);
					}
				}
			}
			else if(null != lastLoginTimeStamp)
			{
				int diff2 = findDiffInDays(lastLoginTimeStamp, new Date().toString());
				if(diff2 > MAX_DAYS_FOR_INACTIVITY)
				{
					// create plan to disable account
					plan = createPlanToDisableAccount(context, link, identity);
				}
			}
			else
			{
				int diff2 = findDiffInDays(pwdLastSet, new Date().toString());
				if(diff2 > MAX_DAYS_FOR_INACTIVITY)
				{
					// create plan to disable account
					plan = createPlanToDisableAccount(context, link, identity);
				}
			}
			Provisioner provisioner = new Provisioner(context);
			provisioner.execute(plan);
		}		
	}
	
	
	private ProvisioningPlan createPlanToDisableAccount(SailPointContext context, Link link, Identity identity) 
	{
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setIdentity(identity);
		
		// set account request on which identity account/link the action has to be taken
		AccountRequest accReq = new AccountRequest();
		accReq.setApplication(link.getApplicationName());
		accReq.setNativeIdentity(link.getNativeIdentity());
		accReq.setOperation(AccountRequest.Operation.Disable);
		
		plan.add(accReq);
		
		return plan;
	}


	private int findDiffInDays(String date1, String date2) 
	{
		// logic for calculating difference in number of days between 2 dates
		return 0;
	}


	/*
	 * Output : Returns list of active users which have account for given application
	 */
	private List<Identity> getActiveUsers(SailPointContext context, String appName) throws GeneralException
	{
		Filter f1 = Filter.eq("Inactive", false);
		Filter f2 = Filter.eq("application.name", appName);
		Filter f3 = Filter.and(f1, f2);
		QueryOptions qo = new QueryOptions();
		qo.add(f3);
		List<Identity> identities = context.getObjects(Identity.class, qo);
		
		return identities;
	}
}
