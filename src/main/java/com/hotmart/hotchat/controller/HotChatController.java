package com.hotmart.hotchat.controller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.dto.User;
import com.hotmart.hotchat.endpoint.HotChatEndpoint;

public class HotChatController {

	// private final Logger logger = Logger.getLogger(getClass().getName());

	private static Map<String, User> users = Collections.synchronizedMap(new LinkedHashMap<String, User>() {
		private static final long serialVersionUID = 6018924635238776194L;

		{
			put("usr1", new User("Walter", "White", "usr1", "1"));
			put("usr2", new User("Jesse", "Pinkman", "usr2", "2"));
			put("usr3", new User("Heisenberg", "Mr.", "usr3", "3"));
		}
	});

	private static Map<String, HotChatEndpoint> usersEndpoints = Collections
			.synchronizedMap(new LinkedHashMap<String, HotChatEndpoint>());

	public static Map<String, User> getUsers() {
		return users;
	}

	public static void setUsers(Map<String, User> users) {
		HotChatController.users = users;
	}

	public static User getUser(String userName) {
		return users.get(userName);
	}

	public static boolean isUserOnline(String userName) {
		return getUser(userName).isOnline();
	}

	public static List<HotMessage> getChatHistory(String sender, String receiver) {
		User retrievedSender = getUser(sender);
		User retrievedReceiver = getUser(receiver);
		if (retrievedSender != null && retrievedReceiver != null) {
			if (retrievedSender.getChatsHistory().get(receiver) != null) {
				return retrievedSender.getChatsHistory().get(receiver);
			} else {
				retrievedSender.getChatsHistory().put(receiver, new ArrayList<HotMessage>());
				return retrievedSender.getChatsHistory().get(receiver);
			}
		} else {
			throw new InvalidParameterException();
		}
	}

	public static List<HotMessage> getOfflineMessages(String receiver) {
		User retrievedReceiver = getUser(receiver);
		if (retrievedReceiver != null) {
			return retrievedReceiver.getOfflineMessages();
		} else {
			throw new InvalidParameterException();
		}
	}

	public static void addToChatHistory(String sender, String receiver, HotMessage hotMessage) {
		getChatHistory(sender, receiver).add(hotMessage);

	}

	public static void addToOfflineMessages(String receiver, HotMessage hotMessage) {
		getOfflineMessages(receiver).add(hotMessage);

	}
	
	public static HotChatEndpoint getUserEndpoint(String userName) {
		return usersEndpoints.get(userName);
	}
	
	public static void addUserEndpoint(String userName, HotChatEndpoint userEndpoint) {
		usersEndpoints.put(userName, userEndpoint);

	}

	public static void removeUserEndpoint(String userName) {
		usersEndpoints.remove(userName);
		
	}

	public static Map<String, HotChatEndpoint> getUsersEndpoints() {
		return usersEndpoints;
	}

	public static Collection<HotMessage> expurgateOfflineMessages(String receiver) {
		User retrievedReceiver = getUser(receiver);
		if (retrievedReceiver != null) {
			List<HotMessage> offlineMessages = retrievedReceiver.getOfflineMessages(); 
			retrievedReceiver.setOfflineMessages(new ArrayList<HotMessage>());
			return offlineMessages;
		} else {
			throw new InvalidParameterException();
		}
	}

}
