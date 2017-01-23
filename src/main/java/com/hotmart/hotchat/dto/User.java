package com.hotmart.hotchat.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that represents an user
 * @author thiagomiceli
 *
 */
public class User {

	/**
	 * User's first name
	 */
	private String firstName;

	/**
	 * User's last name
	 */
	private String lastName;
	
	/**
	 * User's username
	 */
	private String userName;
	
	/**
	 * User's password
	 */
	private String password;
	
	/**
	 * User's online status
	 */
	private boolean online = false;
	
	/**
	 * Map to store all the chat history between two users, being receiver username the key of the map
	 * and a list of all messages  
	 */
	private HashMap<String, List<HotMessage>> chatsHistory = new HashMap<String, List<HotMessage>>();;
	
	/**
	 * List to store all the offline messages of the user
	 */
	private List<HotMessage> offlineMessages = new ArrayList<HotMessage>();

	/**
	 * Default constructor
	 */
	public User() {
		super();
	}
	
	/**
	 * Constructor
	 * @param firstName, user's first name
	 * @param lastName, user's last name
	 * @param userName, user's username
	 * @param password, user's password
	 */
	public User(String firstName, String lastName, String userName, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.chatsHistory = new HashMap<String, List<HotMessage>>();
	}

	/**
	 * Get the first name of the user
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	
	/**
	 * Set user's firstName
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get the last name of the user
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set user's lastName
	 * @param lastName, the lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get the userName of the user
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set user's userName
	 * @param userName, the userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get the password of the user
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set user's password
	 * @param password, the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Get user online status 
	 * @return the online status
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * Set the user's online status
	 * @param online, the online status
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * Get the chat history of the user
	 * @return the chatHistory
	 */
	public HashMap<String, List<HotMessage>> getChatsHistory() {
		return chatsHistory;
	}

	/**
	 * Set the map with user's chat history
	 * @param chatsHistory the map with chat history
	 */
	public void setChatsHistory(HashMap<String, List<HotMessage>> chatsHistory) {
		this.chatsHistory = chatsHistory;
	}
	
	/**
	 * Get user's offline messages
	 * @return the offline messages
	 */
	public List<HotMessage> getOfflineMessages() {
		return offlineMessages;
	}

	/**
	 * Set the list of offline messages
	 * @param offlineMessages list of the messages
	 */
	public void setOfflineMessages(List<HotMessage> offlineMessages) {
		this.offlineMessages = offlineMessages;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", " + "lastName=" + lastName + ", userName=" + userName + ", password="
				+ password + "]";
	}

}
