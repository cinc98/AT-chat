package beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import models.Message;
import ws.WSEndPoint;

@Stateless
@Path("messages")
@LocalBean
public class ChatBean {

	@EJB
	WSEndPoint ws;


	private List<Message> messages = new ArrayList<Message>();

	@GET
	@Path("/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public List<String> getAllMessages(@PathParam("user") String username) {
		List<String> retval = new ArrayList<String>();
		String msg="";
		ObjectMapper mapper = new ObjectMapper();

		for(Message m : messages) {
			if(m.getFrom().equals(username) || m.getTo().equals(username) || m.getTo().equals("all")) {
				try {
				     msg = mapper.writeValueAsString(m);
				}
				catch (JsonGenerationException | JsonMappingException  e) {
				    // catch various errors
				    e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				retval.add(msg);
			}
		}
		
		
		return retval;
	}
	
	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendMessageToUser(Message m) {
		Date d = new Date();
		m.setDate(d);
		messages.add(m);
		
		String msg="";
		ObjectMapper mapper = new ObjectMapper();
		try {
		    // convert user object to json string and return it 
		     msg = mapper.writeValueAsString(m);
		}
		catch (JsonGenerationException | JsonMappingException  e) {
		    // catch various errors
		    e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		System.out.println(msg);
		ws.echoTextMessage(msg);

		return Response.status(200).build();

	}


	@POST
	@Path("/all")
	@Produces(MediaType.TEXT_PLAIN)
	public Response post(Message m) {
		Date d = new Date();
		m.setDate(d);
		messages.add(m);
		String msg="";
		ObjectMapper mapper = new ObjectMapper();
		try {
		    // convert user object to json string and return it 
		     msg = mapper.writeValueAsString(m);
		}
		catch (JsonGenerationException | JsonMappingException  e) {
		    // catch various errors
		    e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		System.out.println(msg);
		ws.echoTextMessage(msg);
		return Response.status(200).build();
	}

}
