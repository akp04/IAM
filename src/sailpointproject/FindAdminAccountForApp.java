package sailpointproject;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.QueryOptions;
import sailpoint.tools.GeneralException;


/*
 * Author : Aakash Pandita
 * Arguments : applicationName, context
 * Output	 : we need to find all the accounts/Links of identities which have admin accounts for given application
 */
public class FindAdminAccountForApp 
{
	
	List<Link> returnLinks = null;
	public List<Link> execute( String applicationName, SailPointContext context) throws GeneralException
	{
		returnLinks = new ArrayList<Link>();
		
		// first we get all Active users/identities only using query/filter APIs		
		List<Identity> identities = findActiveUsers(context);
		
		// now for all identities we check each account and see if account is admin
		for(Identity identity : identities)
		{
			Application app = context.getObjectByName(Application.class, applicationName);
			
			// user can have more than 1 account for an application
			List<Link> links = identity.getLinks(app);
			for(Link link : links)
			{
				if(link.getAttribute("nativeIdentity").toString().toLowerCase().contains("admin"))
				{
					returnLinks.add(link);
					break;
				}
			}
		}
		return returnLinks;
	}
	
	public List<Identity> findActiveUsers(SailPointContext context) throws GeneralException
	{
		Filter f = Filter.eq("Inactive", false);
		QueryOptions qo = new QueryOptions();
		qo.add(f);
		List<Identity> identities = context.getObjects(Identity.class, qo);
		
		return identities;
	}
}
