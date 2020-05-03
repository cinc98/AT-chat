package beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import models.Host;
import models.User;
import ws.WSEndPoint;

@Stateless
@Path("/users")
@LocalBean
public class UserBean {

	@EJB
	WSEndPoint ws;

	
	private List<User> users = new ArrayList<User>();
	private List<User> activeUsers = new ArrayList<User>();

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername())) {
				System.out.println("Username already exists!");
				return Response.status(400).build();
			}

		}

		users.add(user);
		System.out.println("User " + user.getUsername() + " registered!");
		return Response.status(200).build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		for (User u : users) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				activeUsers.add(user);
				System.out.println("User " + user.getUsername() + " logged in!");
//				ResteasyClient client = new ResteasyClientBuilder().build();
//
//				for (Host h : NodeManagerBean.hosts) {
//					ResteasyWebTarget rtarget = client.target("http://" + h.getAddress() + "/ChatWAR/connection");
//					NodeManager rest = rtarget.proxy(NodeManager.class);
//					rest.setUsers(activeUsers);
//				}
//				
//		
//				ws.echoTextMessage(activeUsers.toString());
				return Response.status(200).build();
			}

		}

		System.out.println("Invalid username or password");
		return Response.status(400).build();
	}

	@GET
	@Path("/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> loggedInUsers() {
		List<String> usernames = new ArrayList<String>();
		for (User u : activeUsers) {
			usernames.add(u.getUsername());

		}

		System.out.println(usernames.toString());
		return usernames;
	}

	@GET
	@Path("/registered")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> registeredUsers() {
		List<String> usernames = new ArrayList<String>();
		for (User u : users) {
			usernames.add(u.getUsername());

		}

		System.out.println(usernames.toString());
		return usernames;
	}

	@DELETE
	@Path("/loggedIn/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(@PathParam("user") String username) {

		Iterator<User> itr = activeUsers.iterator();
		while (itr.hasNext()) {
			User user = itr.next();
			if (user.getUsername().equals(username)) {
				activeUsers.remove(user);
				System.out.println("User " + user.getUsername() + " logged out!");

				ws.deleteUser(username);
				return Response.status(200).build();
			}
		}

		return Response.status(400).build();

	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<User> activeUsers) {
		this.activeUsers = activeUsers;
	}

}
