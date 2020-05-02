package beans;

import java.lang.management.ManagementFactory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import models.Host;

@Singleton
@Startup
@Path("/")
public class NodeManagerBean implements NodeManager {

	private List<Host> hosts = new ArrayList<Host>();
	private Host host = null;
	private String master = null;

	@PostConstruct
	private void init() {
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			host = new Host();
			this.host.setAddress((String) mBeanServer.getAttribute(http, "boundAddress") + ":8080");
			this.host.setAlias(System.getProperty("jboss.node.name") + ":8080");

			this.master = "192.168.9.107:8080";

			System.out.println("MASTER ADDR: " + master + ", node name: " + this.host.getAlias() + ", node address: "
					+ this.host.getAddress());
			if (!master.equals(this.host.getAddress())) {
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target("http://" + master + "/ChatWAR/rest");
				NodeManager rest = rtarget.proxy(NodeManager.class);
				rest.registerNode(host);
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	private void destroy() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		for (Host h : hosts) {
			ResteasyWebTarget rtarget = client.target("http://" + h.getAddress() + "/ChatWAR/rest/");
			NodeManager rest = rtarget.proxy(NodeManager.class);
			rest.deleteNode(this.host.getAlias());

		}
		
	}

	@Override
	public Response registerNode(Host host) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		for (Host h : hosts) {
			ResteasyWebTarget rtarget = client.target("http://" + h.getAddress() + "/ChatWAR/rest/");
			NodeManager rest = rtarget.proxy(NodeManager.class);
			rest.addNode(host);

		}

		return Response.status(200).build();
	}

	@Override
	public void addNode(Host host) {
		hosts.add(host);
	}

	@Override
	public Response getNodes(List<Host> h) {
		for (Host ho : h) {
			hosts.add(ho);
		}
		return Response.status(200).build();
	}

	@Override
	public boolean deleteNode(String alias) {
		for (Host h : hosts) {
			if (h.getAlias().equals(alias)) {
				hosts.remove(h);
				return true;
			}
		}
		return false;
	}

	@Override
	public Host getNode() {
		return this.host;
	}


}
