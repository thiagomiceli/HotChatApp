package com.hotmart.hotchat.jersey;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;


/**
 * Test class of the UserResource, a jersey RESTful service 
 * @author thiagomiceli
 *
 */
public class UserResourceTest {

	/**
	 * URL of the resource
	 */
	private static final String URL = "http://0.0.0.0:8080/hotchat/rest/users/";

	/**
	 *  Get all users test
	 */
	@Test
	public void testGetAllUsers() {
		given().contentType("application/json").when().get(URL).then().body(containsString("usr1")).statusCode(200);
	}

	/**
	 * Get a user by userName, successful test
	 */
	@Test
	public void testGetByUserNameSuccess() {
		given().contentType("application/json").when().get(URL + "usr1").then().body(containsString("usr1"))
				.statusCode(200);
	}

	/**
	 * Get a user by userName, fail test
	 */
	@Test
	public void testGetByUserNameFail() {
		given().contentType("application/json").when().get(URL + "usr99").then()
				.body(containsString("Incorrect username or password!")).statusCode(404);
	}

	/**
	 * Get the chat history for a user, successful test
	 */
	@Test
	public void testChatHistorySuccess() {
		given().contentType("application/json").when().get(URL + "usr1/usr2").then().statusCode(200);
	}

	/**
	 * Get the chat history for the sender, fail test
	 */
	@Test
	public void testChatHistorySenderFail() {
		given().contentType("application/json").when().get(URL + "usr100/usr2").then().statusCode(500);
	}

	/**
	 * Get the chat history for the receiver, fail test
	 */
	@Test
	public void testChatHistoryReceiverFail() {
		given().contentType("application/json").when().get(URL + "usr1/usr200").then().statusCode(500);
	}

	/**
	 * Get the offline messages for a user, successful test
	 */
	@Test
	public void testGetOfflineMessagesSuccess() {
		given().contentType("application/json").when().get(URL + "offline/usr1").then().statusCode(200);
	}

	/**
	 * Get the offline messages for a user, fail test
	 */
	@Test
	public void testGetOfflineMessageFail() {
		given().contentType("application/json").when().get(URL + "offline/usr100").then().statusCode(500);
	}

	/**
	 * Get a user online status test
	 */
	@Test
	public void testSetUserOnlineStatus() {
		boolean status = false;

		given().contentType("application/json").body("{\"status\":\"" + status + "\"\n" + "\"}").when()
				.post(URL + "usr1/" + status).then().statusCode(200);
	}
}
