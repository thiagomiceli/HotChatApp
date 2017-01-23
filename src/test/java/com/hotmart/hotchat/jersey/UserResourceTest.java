package com.hotmart.hotchat.jersey;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;


public class UserResourceTest {

	private static final String URL = "http://0.0.0.0:8080/hotchat/rest/users/";

	@Test
	public void testGetAllUsers() {
		given().contentType("application/json").when().get(URL).then().body(containsString("usr1")).statusCode(200);
	}

	@Test
	public void testGetByUserNameSuccess() {
		given().contentType("application/json").when().get(URL + "usr1").then().body(containsString("usr1"))
				.statusCode(200);
	}

	@Test
	public void testGetByUserNameFail() {
		given().contentType("application/json").when().get(URL + "usr99").then()
				.body(containsString("Incorrect username or password!")).statusCode(404);
	}

	@Test
	public void testChatHistorySuccess() {
		given().contentType("application/json").when().get(URL + "usr1/usr2").then().statusCode(200);
	}

	@Test
	public void testChatHistorySenderFail() {
		given().contentType("application/json").when().get(URL + "usr100/usr2").then().statusCode(500);
	}

	@Test
	public void testChatHistoryReceiverFail() {
		given().contentType("application/json").when().get(URL + "usr1/usr200").then().statusCode(500);
	}

	@Test
	public void testGetOfflineMessagesSuccess() {
		given().contentType("application/json").when().get(URL + "offline/usr1").then().statusCode(200);
	}

	@Test
	public void testGetOfflineMessageFail() {
		given().contentType("application/json").when().get(URL + "offline/usr100").then().statusCode(500);
	}

	@Test
	public void testSetUserOnlineStatus() {
		boolean status = false;

		given().contentType("application/json").body("{\"status\":\"" + status + "\"\n" + "\"}").when()
				.post(URL + "usr1/" + status).then().statusCode(200);
	}
}
