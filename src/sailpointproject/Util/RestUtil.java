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
	
	
	public String getAccessTokenWithOAuth2(String clientID, String clientSecret, String tokenURL, String grant_type)
	{
		Client client  = ClientBuilder.newClient();
		
		MultivaluedMap formData = new MultivaluedHashMap();
		formData.add("grant_type", grant_type);
		
		String credentials = clientID + ":" + clientSecret;
        String encodedCredentials = "Basic " + Base64.encodeBase64String(credentials.getBytes());
        
        Response response = (Response) client.
        					target(tokenURL).
        					request(MediaType.APPLICATION_JSON).
        					header("Authorization", encodedCredentials).
        					post(Entity.form(formData));
        
        
        String tokenString = response.readEntity(String.class);
		return tokenString;
	}
	
}
