package sailpointproject.Rule;

import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.ManagedAttribute;
import sailpoint.tools.GeneralException;


/*
 * Author 	: Aakash Pandita
 * Arguments 	: identity, context
 * Output	: we need to make entitlements requestable and set owners for them. we can also set level of approval as per environment
 */
public class MakeEntitlementRequestable 
{
	public ManagedAttribute execute(SailPointContext context, String groupApplication, ManagedAttribute accountGroup) throws GeneralException
	{
		String group_owner = "spadmin"; // this can be workgroup as well
		Identity owner  = context.getObjectByName(Identity.class, group_owner);
		String appName = "SaaS App";
		
		if(groupApplication.equalsIgnoreCase(appName))
		{
			if(null != accountGroup && !accountGroup.isRequestable())
			{
				accountGroup.setRequestable(true);
				accountGroup.setOwner(owner);
				accountGroup.setDisplayName(accountGroup.getDisplayName());
				// accountGroup.setAttribute("Level_Of_Approval", "Two Level");
				// accountGroup.setAttribute("Level 1", "");
				// accountGroup.setAttribute("Level 2", "");
			}
		}
		return accountGroup;
	}
}
