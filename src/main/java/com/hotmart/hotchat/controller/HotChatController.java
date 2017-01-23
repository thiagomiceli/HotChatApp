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

/**
 * HotChat Controller, used to manage the the chat application
 * 
 * @author thiagomiceli
 *
 */
public class HotChatController {

	/**
	 * Map used to store all the registered users, uses the userName as the key
	 * to the User object
	 * 
	 */
	private static Map<String, User> users = Collections.synchronizedMap(new LinkedHashMap<String, User>() {
		
		/**
		 * serial version number
		 */
		private static final long serialVersionUID = 6018924635238776194L;

		{
			//creating users for testing purpose...
			put("usr1", new User("Walter", "White", "usr1", "1"));
			put("usr2", new User("Jesse", "Pinkman", "usr2", "2"));
			put("usr3", new User("Heisenberg", "Mr.", "usr3", "3"));
		}
	});

	
	/**
	 * Map used to store all the users websocket's endpoint, 
	 * uses the userName as the key to the HotChatEndpoint object
	 */
	private static Map<String, HotChatEndpoint> usersEndpoints = Collections
			.synchronizedMap(new LinkedHashMap<String, HotChatEndpoint>());

	
	/**
	 * Get all registered users
	 * @return all the registerd users
	 */
	public static Map<String, User> getUsers() {
		return users;
	}


	/**
	 * Get a user by his userName
	 * @param userName, user to be found
	 * @return the user
	 */
	public static User getUser(String userName) {
		return users.get(userName);
	}

	/**
	 * Get the user online status
	 * @param userName, the user's status to be found
	 * @return the user online status
	 */
	public static boolean isUserOnline(String userName) {
		return getUser(userName).isOnline();
	}

	/**
	 * Get the user's chat history with another user
	 * @param sender the sender of the messages
	 * @param receiver the receiver of the messages
	 * @return the chat history for a specific sender/receiver
	 */
	public static List<HotMessage> getChatHistory(String sender, String receiver) {
		User retrievedSender = getUser(sender);
		User retrievedReceiver = getUser(receiver);
		//if both users exists
		if (retrievedSender != null && retrievedReceiver != null) {
			//add to chat history
			if (retrievedSender.getChatsHistory().get(receiver) != null) {
				return retrievedSender.getChatsHistory().get(receiver);
			} else {
				retrievedSender.getChatsHistory().put(receiver, new ArrayList<HotMessage>());
				return retrievedSender.getChatsHistory().get(receiver);
			}
		} else {
			//if one of the users don't exist, throw an exception
			throw new InvalidParameterException();
		}
	}

	/**
	 * Get all offline messages of an user
	 * @param userName of the user's offline messages
	 * @return the offline messages
	 */
	public static List<HotMessage> getOfflineMessages(String userName) {
		User retrievedUser = getUser(userName);
		if (retrievedUser != null) {
			return retrievedUser.getOfflineMessages();
		} else {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Used to add a new message to a chat history
	 * @param sender, the sender of the message
	 * @param receiver, the receiver of the message
	 * @param hotMessage, the new message
	 */
	public static void addToChatHistory(String sender, String receiver, HotMessage hotMessage) {
		getChatHistory(sender, receiver).add(hotMessage);

	}

	/**
	 * Used to add a new offline message
	 * @param receiver, the receiver of the message
	 * @param hotMessage, the new offline message
	 */
	public static void addToOfflineMessages(String receiver, HotMessage hotMessage) {
		getOfflineMessages(receiver).add(hotMessage);

	}

	/**
	 * Get an user's endpoint
	 * @param userName, the name of the user
	 * @return the endpoint of the user
	 */
	public static HotChatEndpoint getUserEndpoint(String userName) {
		return usersEndpoints.get(userName);
	}

	/**
	 * Used to add a new user's endpoint to the map
	 * @param userName, the key of the map 
	 * @param userEndpoint, the user's endpoint
	 */
	public static void addUserEndpoint(String userName, HotChatEndpoint userEndpoint) {
		usersEndpoints.put(userName, userEndpoint);

	}

	/**
	 * Used to remove an user's endpoint of the map
	 * @param userName, the key of the map 
	 */
	public static void removeUserEndpoint(String userName) {
		usersEndpoints.remove(userName);

	}

	/**
	 * Get all users endpoints
	 * @return the endpoints
	 */
	public static Map<String, HotChatEndpoint> getUsersEndpoints() {
		return usersEndpoints;
	}

	/**
	 * Used to get the offline messages when the user logs in
	 * the messages must be shown once. So after get the list 
	 * of offline messages, reset the list
	 * @param receiver the receiver
	 * @return the list of offline messages
	 */
	public static Collection<HotMessage> expurgateOfflineMessages(String receiver) {
		User retrievedReceiver = getUser(receiver);
		if (retrievedReceiver != null) {
			//get the offline messages, once...
			List<HotMessage> offlineMessages = retrievedReceiver.getOfflineMessages();
			//and reset the list
			retrievedReceiver.setOfflineMessages(new ArrayList<HotMessage>());
			return offlineMessages;
		} else {
			throw new InvalidParameterException();
		}
	}

}
