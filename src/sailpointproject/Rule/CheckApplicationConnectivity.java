package sailpointproject.Rule;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.tools.GeneralException;

import sailpoint.connector.Connector;
import sailpoint.connector.ConnectorFactory;



/*
 * Author 	 : Aakash Pandita
 * output 	 : we need to check connectivity for given Application
 * Arguments 	 : applicationName
 */
public class CheckApplicationConnectivity 
{
	Connector connector = null;
	
	public void execute(SailPointContext context, String appName) throws GeneralException
	{
		Application app = context.getObjectByName(Application.class, appName);
		try
		{
			connector = ConnectorFactory.getConnector(app, null);
			connector.testConfiguration();
		}
		catch(Exception e)
		{
			log.error("Excetion during Test connectivity of app: " + appName);
			log.error(e.getMessage());
		}		
	}
}
