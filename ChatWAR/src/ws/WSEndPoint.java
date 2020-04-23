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
			sessions.add(session);
		}
		
		System.out.println(username);
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

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		t.printStackTrace();
	}

}
