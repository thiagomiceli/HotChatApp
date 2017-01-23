package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.encoder.HotMessageDecoder;
import com.hotmart.hotchat.encoder.HotMessageEncoder;

@ClientEndpoint(encoders = HotMessageEncoder.class, decoders = HotMessageDecoder.class)
public class HotChatEndpointClient {
	private Session userSession = null;
	private MessageHandler messageHandler;
 
	public HotChatEndpointClient() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI("ws://0.0.0.0:8080/hotchat/chat/usr1"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
 
	@OnOpen
	public void onOpen(final Session userSession) {
		this.userSession = userSession;
	}
 
	@OnClose
	public void onClose(final Session userSession, final CloseReason reason) {
		this.userSession = null;
	}
 
	@OnMessage
	public void onMessage(final HotMessage message) {
		if (messageHandler != null) {
			messageHandler.handleMessage(message);
		}
	}
 
	public void addMessageHandler(final MessageHandler msgHandler) {
		messageHandler = msgHandler;
	}
 
	public void sendMessage(final HotMessage message) throws IOException, EncodeException {
			userSession.getBasicRemote().sendObject(message);
	}
 
	public static interface MessageHandler {
		public void handleMessage(HotMessage message);
	}
}
