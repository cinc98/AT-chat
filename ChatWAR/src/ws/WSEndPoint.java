package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Singleton
@ServerEndpoint("/ws/{username}")
@LocalBean
public class WSEndPoint {
	static List<Session> sessions = new ArrayList<Session>();

	@OnOpen
	public void onOpen(Session session,@PathParam("username") String username) {
		if (!sessions.contains(session)) {
			session.getUserProperties().put(session.getId(), username);
			sessions.add(session);
			
		}
		echoTextMessage(getUsers().toString());
	
		
		
	}
	
	

	@OnMessage
	public void echoTextMessage(String msg) {

		try {
			for (Session s : sessions) {
				System.out.println("WSEndPoint: " + msg);
				s.getBasicRemote().sendText(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		users.add(0, "users");
		for (Session s : sessions) {
			
			users.add((String) s.getUserProperties().get(s.getId()));
		}
		
		return users;

	}
	
	public void deleteUser(String username) {
		for (Session s : sessions) {
			if(s.getUserProperties().containsValue(username)) {
				close(s);
				break;
			}
		}
		

	}

	@OnClose
	public void close(Session session) {
				System.out.println("closed!!");
				sessions.remove(session);
				echoTextMessage(getUsers().toString());

	}
			
		
	

	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		t.printStackTrace();
	}

}
