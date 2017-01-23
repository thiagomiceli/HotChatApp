package com.hotmart.hotchat.endpoint;

import java.io.IOException;
import java.util.Date;

import javax.websocket.EncodeException;

import org.junit.Test;

import com.hotmart.hotchat.dto.HotMessage;

public class HotChatEndpointTest {
	HotChatEndpointClient clientEndPoint = new HotChatEndpointClient();

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
