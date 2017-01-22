package com.hotmart.hotchat.controller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.dto.User;

public class HotChatController {
	
//	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private static Map<String, User> users = Collections.synchronizedMap(new LinkedHashMap<String, User>() {
		private static final long serialVersionUID = 6018924635238776194L;

	{
		put("usr1", new User("User1", "Number1", "usr1", "1"));
		put("usr2", new User("User1", "Number2", "usr2", "2"));
	}});
	
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
		User retrievedSender = HotChatController.getUser(sender);
		User retrievedReceiver = HotChatController.getUsers().get(receiver);
		if (retrievedSender != null && retrievedReceiver != null) {
			if(retrievedSender.getChatsHistory().get(receiver) != null) {
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
		User retrievedReceiver = HotChatController.getUsers().get(receiver);
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
	
	
}
