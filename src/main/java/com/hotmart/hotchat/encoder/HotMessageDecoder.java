package com.hotmart.hotchat.encoder;

import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.hotmart.hotchat.dto.HotMessage;

/**
 * @author thiagomiceli
 * Class to decode the chat messages from Json to Text
 *
 */
public class HotMessageDecoder implements Decoder.Text<HotMessage> {
	
	@Override
	public void init(final EndpointConfig config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public HotMessage decode(final String textMessage) throws DecodeException {
		HotMessage hotMessage = new HotMessage();
		JsonObject obj = Json.createReader(new StringReader(textMessage)).readObject();
		hotMessage.setSender(obj.getString("sender"));
		hotMessage.setMessage(obj.getString("message"));
		hotMessage.setReceiver(obj.getString("receiver"));
		hotMessage.setTimeStamp(new Date());
		return hotMessage;
	}

	@Override
	public boolean willDecode(final String s) {
		return true;
	}
}
