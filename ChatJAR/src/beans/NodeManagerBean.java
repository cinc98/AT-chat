package beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.Path;

import models.Host;

@Singleton
@Startup
@Path("/")
public class NodeManagerBean {

	private List<Host> hosts = new ArrayList<Host>();
	private String thisNode = "";
	private String masterNode = "";

	@PostConstruct
	public void nodeInit() {
		System.out.println("STARTED!");

	}

}
