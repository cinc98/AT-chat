package beans;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Host;

public interface NodeManager {

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerNode(Host host);

	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNode(Host host);

	@POST
	@Path("/nodes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNodes(List<Host> hosts);

	@DELETE
	@Path("/node/{alias}")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean deleteNode(@PathParam("alias") String alias);

	@GET
	@Path("/node")
	public Host getNode();
}
