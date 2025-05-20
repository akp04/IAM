package sailpointproject.Util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

/*
 * Author 	: Aakash Pandita
 * Output	: This is a utility class
 */
public class RestUtil 
{
	
	
	public static String getAccessTokenWithOAuth2(String clientID, String clientSecret, String tokenURL, String grant_type)
	{
		Client client = null;
		Response response = null;
		String tokenString = null;
		try
		{
			client  = ClientBuilder.newClient();
			
			MultivaluedMap formData = new MultivaluedHashMap();
			formData.add("grant_type", grant_type);
			// grant_type = "client_credentials" - This is generally default
			
			String credentials = clientID + ":" + clientSecret;
	        	String encodedCredentials = "Basic " + Base64.encodeBase64String(credentials.getBytes());
	        
	        	response = (Response) client.
					target(tokenURL).
					request(MediaType.APPLICATION_JSON).
					header("Authorization", encodedCredentials).
					post(Entity.form(formData));
	        
	        	tokenString = response.readEntity(String.class);
		}
        	catch(Exception e)
		{
        		e.printStackTrace();
		}
		finally
		{
		        response.close();
		        client.close();
		}
        
		return tokenString;
	}
	
	
	public static String GETApiWithTokenAuthorization(String token, String API_URL )
	{
		Client client = null;
		Response response = null;
		String output = null;		
		try 
		{
			client = ClientBuilder.newClient();
			
			response = (Response)client.
					target(API_URL).
					request(MediaType.APPLICATION_JSON).
					accept(MediaType.APPLICATION_JSON).
					header("Authorization", token).
					get();
			
			output = response.readEntity(String.class); // reading response as string format
		}
		catch(Exception e)
		{
        		e.printStackTrace();
		}
		finally
		{
		        response.close();
		        client.close();
		}
		
		return output;		
	}
	
	
	public static String POSTApiWithTokenAuthorization(String token, String API_URL, String body )
	{
		Client client = null;
		Response response = null;
		String output = null;		
		try 
		{
		        client = ClientBuilder.newClient();
	
		        //String jsonPayload = "{\"name\": \"John\", \"age\": 30}";
	
		        response = client
					.target(API_URL)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(body, MediaType.APPLICATION_JSON));
		        
		        output = response.readEntity(String.class);
		}
		catch(Exception e)
		{
        		e.printStackTrace();
		}
		finally
		{
		        response.close();
		        client.close();
		}
		
		return output;		
	}
	
	
}
