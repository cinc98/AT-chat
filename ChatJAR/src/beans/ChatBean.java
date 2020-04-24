package beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ws.WSEndPoint;

@Stateless
@Path("messages")
@LocalBean
public class ChatBean {

	@EJB
	WSEndPoint ws;

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "ok";
	}

	@POST
	@Path("/all")
	@Produces(MediaType.TEXT_PLAIN)
	public String post(String text) {

		ws.echoTextMessage(text);
		return "ok";
	}

}
