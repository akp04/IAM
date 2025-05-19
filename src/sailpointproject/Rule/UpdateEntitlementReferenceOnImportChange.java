package sailpointproject.Rule;

import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Bundle;
import sailpoint.object.Filter;
import sailpoint.object.Filter.LeafFilter;
import sailpoint.object.Profile;
import sailpoint.object.QueryOptions;
import sailpoint.tools.GeneralException;

/*
 * Author 	: Aakash Pandita
 * Arguments 	: entitlement that changed, context
 * Output	: we need to update all entitlement references when it changes at source. once aggregation is run those changes are pulled. 
 * 
 * This can be also considered as an LCE which is triggered once aggregation runs and brings the updated data.
 */
public class UpdateEntitlementReferenceOnImportChange 
{
	
	public void execute(SailPointContext context, String entitlement) throws GeneralException
	{
		Filter f1 = Filter.eq("type", "IT");
		QueryOptions qo = new QueryOptions();
		qo.add(f1);
		List<Bundle> bundles = context.getObjects(Bundle.class, qo);
		
		for(Bundle role : bundles)
		{
			//String roleName = role.getName();
			List<Profile> profiles = role.getProfiles();
			for(Profile pro : profiles)
			{
			  List<Filter> listFilter = pro.getConstraints();
			  for(Filter filter :  listFilter)
			  {
		          List<Object> value = (List) ((LeafFilter) filter).getValue();
		          int index = -1;
		          for(int i = 0; i < value.size(); i++)
		          {
		        	  Object entName = value.get(i);
		        	  if(entName.toString().equals(entitlement))
		        	  {
		        		  index = i;
		        		  break;
		        	  }
		          }
		          if(index != -1)
		          {
		        	  value.remove(index);
		        	  value.add(entitlement);
		        	  
		        	  context.saveObject(role);
		        	  context.commitTransaction();
		        	  context.decache();
		          }
			  }
			}
		}
		
		return;
	}
}
