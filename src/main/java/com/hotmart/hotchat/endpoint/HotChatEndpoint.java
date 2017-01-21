package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
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
	private Session session;
    private String userName;
    private static final Set<HotChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String,String> users = new HashMap<>();

	
	@OnOpen
	public void open(final Session session, @PathParam("user") final String userName) throws IOException, EncodeException {
		log.info("session openend and bound to user: " + userName);
		this.session = session;
        this.userName = userName;
        chatEndpoints.add(this);
        users.put(session.getId(), this.userName);

        HotMessage hotMessage = new HotMessage();
        hotMessage.setSender(this.userName);
        hotMessage.setMessage("connected!");
        hotMessage.setReceiver("all");
        hotMessage.setTimeStamp(new Date());
        broadcast(hotMessage);
	}

	@OnMessage
	public void onMessage(final Session session, final HotMessage hotMessage) throws IOException, EncodeException {
	        sendMessageToOneUser(hotMessage);
	}
	
	@OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        log.info(session.getId() + " disconnected!");

        chatEndpoints.remove(this);
        HotMessage message = new HotMessage();
        message.setSender(users.get(session.getId()));
        message.setMessage("disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warning(throwable.toString());
    }

    private static void broadcast(HotMessage message) throws IOException, EncodeException {
        for (HotChatEndpoint endpoint : chatEndpoints) {
            synchronized(endpoint) {
            	if (!endpoint.session.getId().equals(getSessionId(message.getSender()))) {
            		endpoint.session.getBasicRemote().sendObject(message);
            	}
            }
        }
    }

    private static void sendMessageToOneUser(HotMessage message) throws IOException, EncodeException {
        for (HotChatEndpoint endpoint : chatEndpoints) {
            synchronized(endpoint) {
				if (endpoint.session.getId().equals(getSessionId(message.getSender()))
						|| endpoint.session.getId().equals(getSessionId(message.getReceiver()))) {
					endpoint.session.getBasicRemote().sendObject(message);
                }
            }
        }
    }

    private static String getSessionId(String to) {
        if (users.containsValue(to)) {
            for (String sessionId: users.keySet()) {
                if (users.get(sessionId).equals(to)) {
                    return sessionId;
                }
            }
        }
        return null;
    }
}
