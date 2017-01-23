package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
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

/**
 * Client used to access the websocket server
 * @author thiagomiceli
 *
 */
@ClientEndpoint(encoders = HotMessageEncoder.class, decoders = HotMessageDecoder.class)
public class HotChatEndpointClient {
	
	/**
	 * User session
	 */
	private Session userSession = null;
	
	/**
	 * listener of incoming messages
	 */
	private MessageHandler messageHandler;
 
	/**
	 * Default constructor, opens a websocket connection with the server
	 */
	public HotChatEndpointClient() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI("ws://0.0.0.0:8080/hotchat/chat/usr1"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
 
	/**
	 * Called when a connection is opened with the server
	 * @param userSession
	 */
	@OnOpen
	public void onOpen(final Session userSession) {
		this.userSession = userSession;
	}
 
	/**
	 * Close the websocket connection
	 * @param userSession the user session
	 */
	@OnClose
	public void onClose(final Session userSession) {
		this.userSession = null;
	}
 
	/**
	 * Callled when a message is sent from the server
	 * @param message the incoming message 
	 */
	@OnMessage
	public void onMessage(final HotMessage message) {
		if (messageHandler != null) {
			messageHandler.handleMessage(message);
		}
	}
 
	/**
	 * Add the listener of incoming messages
	 * @param msgHandler
	 */
	public void addMessageHandler(final MessageHandler msgHandler) {
		messageHandler = msgHandler;
	}
 
	/**
	 * Send a message to the server
	 * @param message the message
	 * @throws IOException
	 * @throws EncodeException
	 */
	public void sendMessage(final HotMessage message) throws IOException, EncodeException {
			userSession.getBasicRemote().sendObject(message);
	}
 
	/**
	 * Interface of the message listener
	 * @author thiagomiceli
	 *
	 */
	public static interface MessageHandler {
		public void handleMessage(HotMessage message);
	}
}
