package com.hotmart.hotchat.dto;

import java.util.Date;

/**
 * @author thiagomiceli 
 * DTO class to send messages to the users
 *
 */
public class HotMessage {

	/**
	 * the user, sender of the message
	 */
	private String sender;

	/**
	 * the user who will receive the message
	 */
	private String receiver;
	
	/**
	 * the first+last name of the sender
	 */
	private String senderFirstLastName;

	/**
	 * the message to be sent
	 */
	private String message;

	/**
	 * Date/Time when the message was received
	 */
	private Date timeStamp;
	

	/**
	 * Default constructor
	 */
	public HotMessage() {
		super();
	}
	
	/**
	 * Class Constructor
	 * @param sender the sender
	 * @param receiver the receiver
	 * @param message the message
	 * @param timeStamp the timestamp of the message
	 */
	public HotMessage(String sender, String receiver, String message, String senderFirstLastName, Date timeStamp) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.timeStamp = timeStamp;
		this.senderFirstLastName = senderFirstLastName;
	}
	

	/**
	 * Returns the sender
	 * 
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Set the sender
	 * 
	 * @param sender
	 *            of the message
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Returns the receiver
	 * 
	 * @return the receiver of the message
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Set the receiver
	 * 
	 * @param receiver
	 *            of the message
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * Returns the message
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get the timeStamp of the message
	 * 
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Set the timeStamp of the message
	 * 
	 * @param timeStamp
	 *            of the message
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSenderFirstLastName() {
		return senderFirstLastName;
	}

	public void setSenderFirstLastName(String senderFirstLastName) {
		this.senderFirstLastName = senderFirstLastName;
	}

	@Override
	public String toString() {
		return "ChatMessage [sender=" + sender + ", " + "message=" + message + ", receiver=" + receiver + ", received="
				+ timeStamp + "]";
	}

}
