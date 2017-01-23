package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.Date;
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

	private final Logger log = Logger.getLogger(getClass().getName());

	private Session session;

	private String userName;

	@OnOpen
	public void open(final Session session, @PathParam("user") final String userName)
			throws IOException, EncodeException {
		log.info("session openend and bound to user: " + userName);
		this.session = session;
		this.userName = userName;
		HotChatController.addUserEndpoint(userName, this);

//		HotMessage hotMessage = new HotMessage();
//		hotMessage.setSender(this.userName);
//		hotMessage.setMessage("connected!");
//		hotMessage.setReceiver("all");
//		hotMessage.setTimeStamp(new Date());
//		broadcast(hotMessage);
	}

	@OnMessage
	public void onMessage(final Session session, final HotMessage hotMessage) throws IOException, EncodeException {
		sendMessageToOneUser(hotMessage);
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		log.info(session.getId() + " disconnected!");
		HotChatController.removeUserEndpoint(userName);
//		HotMessage hotMessage = new HotMessage();
//		hotMessage.setSender(userName);
//		hotMessage.setMessage("disconnected!");
//		hotMessage.setTimeStamp(new Date());
//		broadcast(hotMessage);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		log.warning(throwable.toString());
	}

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

	private static void sendMessageToOneUser(HotMessage hotMessage) throws IOException, EncodeException {
		String sender = hotMessage.getSender();
		String receiver = hotMessage.getReceiver();

		if (HotChatController.isUserOnline(receiver)) {
			HotChatEndpoint senderEndpoint = HotChatController.getUserEndpoint(sender);
			HotChatEndpoint receiverEndpoint = HotChatController.getUserEndpoint(receiver);
			synchronized (senderEndpoint) {
				senderEndpoint.session.getBasicRemote().sendObject(hotMessage);
				HotChatController.addToChatHistory(sender, receiver, hotMessage);
			}
			synchronized (receiverEndpoint) {
				receiverEndpoint.session.getBasicRemote().sendObject(hotMessage);
				HotChatController.addToChatHistory(receiver, sender, hotMessage);
			}
		} else {
			HotChatEndpoint senderEndpoint = HotChatController.getUserEndpoint(sender);
			synchronized (senderEndpoint) {
				senderEndpoint.session.getBasicRemote().sendObject(hotMessage);
				HotChatController.addToChatHistory(sender, receiver, hotMessage);
			}
			HotChatController.addToOfflineMessages(receiver, hotMessage);
			HotChatController.addToChatHistory(receiver, sender, hotMessage);
		}

	}
}
