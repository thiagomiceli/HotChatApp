package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.Date;

import javax.websocket.EncodeException;

import org.junit.Test;

import com.hotmart.hotchat.dto.HotMessage;

/**
 * Test class of the websocket server 
 * @author thiagomiceli
 *
 */
public class HotChatEndpointTest {
	HotChatEndpointClient clientEndPoint = new HotChatEndpointClient();

	/**
	 * Send a message successful test
	 */
	@Test
	public void sendHotMessageSucess() {
		// add listener
		clientEndPoint.addMessageHandler(new HotChatEndpointClient.MessageHandler() {
			@Override
			public void handleMessage(HotMessage message) {
				assert (message.equals("Test Message"));
			}
		});

		try {
			clientEndPoint.sendMessage(new HotMessage("usr1", "usr2", "Test Message", "firstLastName", new Date()));
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Send a message fail test
	 */
	@Test(expected=RuntimeException.class)
	public void sendHotMessageFail() {
		// add listener
		clientEndPoint.addMessageHandler(new HotChatEndpointClient.MessageHandler() {
			@Override
			public void handleMessage(HotMessage message) {
				assert (message.equals("Test Message"));
			}
		});

			try {
				clientEndPoint.sendMessage(new HotMessage());
			} catch (IOException | EncodeException e) {
				e.printStackTrace();
			}
	}

}
