package com.hotmart.hotchat.encoder;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.hotmart.hotchat.dto.HotMessage;


/**
 * @author thiagomiceli
 * Class to encode the chat messages from Text to Json
 */
public class HotMessageEncoder implements Encoder.Text<HotMessage>{

	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(final HotMessage hotMessage) throws EncodeException {
		return Json.createObjectBuilder()
				.add("sender", hotMessage.getSender())
				.add("message", hotMessage.getMessage())
				.add("receiver", hotMessage.getReceiver())
				.add("senderFirstLastName", hotMessage.getSenderFirstLastName())
				.add("received", hotMessage.getTimeStamp().toString()).build()
				.toString();
	}

}
