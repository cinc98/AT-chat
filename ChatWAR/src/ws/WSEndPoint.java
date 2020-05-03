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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import models.Message;

@Singleton
@ServerEndpoint("/ws/{username}")
@LocalBean
public class WSEndPoint {
	static List<Session> sessions = new ArrayList<Session>();

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) {
		if (!sessions.contains(session)) {
			session.getUserProperties().put(session.getId(), username);
			sessions.add(session);

		}
		echoTextMessage(getUsers().toString());

	}

	@OnMessage
	public void echoTextMessage(String msg) {

		ObjectMapper objectMapper = new ObjectMapper();
		Message message = null;
		try {
			message = objectMapper.readValue(msg, Message.class);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}

		try {
			if (message != null && !message.getTo().equals("all")) {
				for (Session s : sessions) {
					if (s.getUserProperties().containsValue(message.getTo())
							|| s.getUserProperties().containsValue(message.getFrom())) {
						s.getBasicRemote().sendText(msg);
					}

				}
			} else {
				for (Session s : sessions) {
					s.getBasicRemote().sendText(msg);
				}
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
			if (s.getUserProperties().containsValue(username)) {
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
