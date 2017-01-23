package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.hotmart.hotchat.controller.HotChatController;
import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.encoder.HotMessageDecoder;
import com.hotmart.hotchat.encoder.HotMessageEncoder;

/**
 * @author thiagomiceli Server side endpoint to chat service
 *
 */
@ServerEndpoint(value = "/chat/{user}", encoders = HotMessageEncoder.class, decoders = HotMessageDecoder.class)
public class HotChatEndpoint {

	/**
	 * logger 
	 */
	private final Logger loggger = Logger.getLogger(getClass().getName());

	/**
	 *  session of the current user
	 */
	private Session session;

	/**
	 *  current user userName
	 */
	private String userName;

	/**
	 * Called when a connection is opened
	 * @param session the user session
	 * @param userName the userName
	 * @throws IOException
	 * @throws EncodeException
	 */
	@OnOpen
	public void open(final Session session, @PathParam("user") final String userName)
			throws IOException, EncodeException {
		loggger.info("session openend and bound to user: " + userName);
		this.session = session;
		this.userName = userName;
		HotChatController.addUserEndpoint(userName, this);
	}

	/**
	 * Called when a message arrives on the server
	 * @param session the session of the user
	 * @param hotMessage the message
	 * @throws IOException
	 * @throws EncodeException
	 */
	@OnMessage
	public void onMessage(final Session session, final HotMessage hotMessage) throws IOException, EncodeException {
		sendMessageToOneUser(hotMessage);
	}

	/**
	 * Called when a connection is closed
	 * @param session the session of the user
	 * @throws IOException
	 * @throws EncodeException
	 */
	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		loggger.info(session.getId() + " disconnected!");
		HotChatController.removeUserEndpoint(userName);
	}

	/**
	 * Called when an error occurs
	 * @param session the session of the user
	 * @param throwable
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		loggger.warning(throwable.toString());
	}

	/**
	 * Broadcast a message to all users
	 * @param hotMessage
	 * @throws IOException
	 * @throws EncodeException
	 */
	private static void broadcast(HotMessage hotMessage) throws IOException, EncodeException {
		for (String userName : HotChatController.getUsersEndpoints().keySet()) {
			HotChatEndpoint receiverEndpoint = HotChatController.getUserEndpoint(userName);
			synchronized (receiverEndpoint) {
				if (!userName.equals(hotMessage.getSender())) {
					receiverEndpoint.session.getBasicRemote().sendObject(hotMessage);
				}
			}
		}
	}

	/**
	 *  Send message to one specific user
	 * @param hotMessage the message to be sent
	 * @throws IOException
	 * @throws EncodeException
	 */
	private static void sendMessageToOneUser(HotMessage hotMessage) throws IOException, EncodeException {
		String sender = hotMessage.getSender();
		String receiver = hotMessage.getReceiver();

		// if receiver is online
		if (HotChatController.isUserOnline(receiver)) {
			//get sender/receiver endpoints
			HotChatEndpoint senderEndpoint = HotChatController.getUserEndpoint(sender);
			HotChatEndpoint receiverEndpoint = HotChatController.getUserEndpoint(receiver);
			synchronized (senderEndpoint) {
				//send message to the sender
				senderEndpoint.session.getBasicRemote().sendObject(hotMessage);
				// add message to the chat history of the sender
				HotChatController.addToChatHistory(sender, receiver, hotMessage);
			}
			synchronized (receiverEndpoint) {
				//send message to the receiver
				receiverEndpoint.session.getBasicRemote().sendObject(hotMessage);
				// add message to the chat history of the receiver
				HotChatController.addToChatHistory(receiver, sender, hotMessage);
			}
		} else { //if receiver is offline
			HotChatEndpoint senderEndpoint = HotChatController.getUserEndpoint(sender);
			synchronized (senderEndpoint) {
				//send message to the sender
				senderEndpoint.session.getBasicRemote().sendObject(hotMessage);
				// add message to the chat history of the sender
				HotChatController.addToChatHistory(sender, receiver, hotMessage);
			}
			//add to offline messages of the receiver
			HotChatController.addToOfflineMessages(receiver, hotMessage);
			// add message to the chat history of the receiver
			HotChatController.addToChatHistory(receiver, sender, hotMessage);
		}

	}
}
