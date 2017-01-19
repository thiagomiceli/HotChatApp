package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.encoder.HotMessageDecoder;
import com.hotmart.hotchat.encoder.HotMessageEncoder;

/**
 * @author thiagomiceli 
 * Server side endpoint to chat service
 *
 */
@ServerEndpoint(value = "/chat/{user}", encoders = HotMessageEncoder.class, decoders = HotMessageDecoder.class)
public class HotChatEndpoint {

	private final Logger log = Logger.getLogger(getClass().getName());

//	private static Map<String, Session> userSockets = Collections
//			.synchronizedMap(new LinkedHashMap<String, Session>());
	
	@OnOpen
	public void open(final Session session, @PathParam("user") final String user) {
		log.info("session openend and bound to room: " + user);
		session.getUserProperties().put("room", user);
	}

	@OnMessage
	public void onMessage(final Session session, final HotMessage chatMessage) {
		String room = (String) session.getUserProperties().get("user");
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
						&& room.equals(s.getUserProperties().get("user"))) {
					s.getBasicRemote().sendObject(chatMessage);
				}
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
	}
}
