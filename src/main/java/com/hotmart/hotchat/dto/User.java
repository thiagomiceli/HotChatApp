package com.hotmart.hotchat.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author thiagomiceli
 *
 */
public class User {

	private String firstName;

	private String lastName;
	
	private String userName;
	
	private String password;
	
	private boolean online;
	
	//receiver,messages
	private HashMap<String, List<HotMessage>> chatsHistory = new HashMap<String, List<HotMessage>>();;
	
	//sender,messages
	private List<HotMessage> offlineMessages = new ArrayList<HotMessage>();

	public User() {
		super();
	}
	
	public User(String firstName, String lastName, String userName, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.chatsHistory = new HashMap<String, List<HotMessage>>();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public HashMap<String, List<HotMessage>> getChatsHistory() {
		return chatsHistory;
	}

	public void setChatsHistory(HashMap<String, List<HotMessage>> chatsHistory) {
		this.chatsHistory = chatsHistory;
	}
	
	public List<HotMessage> getOfflineMessages() {
		return offlineMessages;
	}

	public void setOfflineMessages(List<HotMessage> offlineMessages) {
		this.offlineMessages = offlineMessages;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", " + "lastName=" + lastName + ", userName=" + userName + ", password="
				+ password + "]";
	}

}
