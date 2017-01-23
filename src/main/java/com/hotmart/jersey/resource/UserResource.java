package com.hotmart.jersey.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hotmart.hotchat.controller.HotChatController;
import com.hotmart.hotchat.dto.HotMessage;
import com.hotmart.hotchat.dto.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/users")
@Api(value = "/users", description = "HotMart Code Challenge that creates a webchat using websockets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	/**
	 * Logger
	 */
	private final Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * Get all registered users
	 * 
	 * @return the users
	 */
	@GET
	@ApiOperation(value = "List all users", response = User.class, responseContainer = "List")
	public Collection<User> getAllUsers() {
		return HotChatController.getUsers().values();
	}

	/**
	 * Get registered user by userName
	 * 
	 * @return the users
	 */
	@GET
	@Path("/{userName}")
	@ApiOperation(value = "Get an user", notes = "Get an specific user by userName", response = Response.class)
	public Response getUserByUserName(
			@ApiParam(value = "UserName to fetch", required = true) @PathParam("userName") String userName) {
		User retrievedUser = HotChatController.getUsers().get(userName);
		if (retrievedUser != null) {
			return Response.status(200).entity(HotChatController.getUsers().get(userName)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Incorrect username or password!")
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * Get chat history with user
	 * 
	 * @return the Response with chatHistory
	 */
	@GET
	@Path("/{sender}/{receiver}")
	@ApiOperation(value = "Get a chat history", notes = "Get a chat history for specfic sender/receiver", response = HotMessage.class, responseContainer = "List")
	public Collection<HotMessage> getChatHistoryByUserName(
			@ApiParam(value = "UserName of the sender", required = true) @PathParam("sender") String sender,
			@ApiParam(value = "UserName of the receiver", required = true) @PathParam("receiver") String receiver) {
		return HotChatController.getChatHistory(sender, receiver);
	}

	@GET
	@Path("/offline/{userName}")
	@ApiOperation(value = "Get offline messages", notes = "Get all offline messages for an user", response = HotMessage.class, responseContainer = "List")
	public Collection<HotMessage> getOfflineMessages(
			@ApiParam(value = "UserName of the sender", required = true) @PathParam("userName") String userName) {
		return HotChatController.expurgateOfflineMessages(userName);
	}

	@POST
	@Path("/{userName}/{status}")
	@ApiOperation(value = "Set user online status", response = Response.class)
	public Response setUserOnlineStatus(
			@ApiParam(value = "UserName of the sender", required = true) @PathParam("userName") String userName,
			@ApiParam(value = "Status of the user", required = true) @PathParam("status") boolean status) {
		HotChatController.getUser(userName).setOnline(status);
		return Response.status(200).entity("User status set").build();
	}

	@POST
	@Path("/authenticate")
	@ApiOperation(value = "Authenticate user", response = Response.class)
	public Response authenticateUser(@ApiParam(value = "The user to be authenticated", required = true) User user) {
		logger.info("authenticate [userName:" + user.getUserName() + ", " + "password:" + user.getPassword() + "]");
		User retrievedUser = HotChatController.getUsers().get(user.getUserName());
		if (retrievedUser != null && retrievedUser.getPassword().equals(user.getPassword())) {
			return Response.status(200).entity(retrievedUser).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Incorrect username or password!")
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@ApiOperation(value = "Create user", response = Response.class)
	public Response create(@ApiParam(value = "The user to be created", required = true) User user) {
		logger.info("creating new user...");
		if (user != null) {
			if (HotChatController.getUsers().get(user.getUserName()) == null) {
				user.setChatsHistory(new HashMap<String, List<HotMessage>>());
				HotChatController.getUsers().put(user.getUserName(), user);
				logger.info("model saved... " + user.toString());
				return Response.status(200).entity(user).build();
			} else {
				throw new WebApplicationException("Username already taken!");
			}
		} else {
			throw new WebApplicationException("User can't be null!");
		}
	}

	@DELETE
	@Path("/{userName}")
	@ApiOperation(value = "Delete user", response = Response.class)
	public void delete(
			@ApiParam(value = "The userName of the user to be deleted", required = true) @PathParam("userName") String userName) {
		logger.info("deleting user...");
		if (userName != null) {
			HotChatController.getUsers().remove(userName);
			logger.info("user " + userName + " deleted.");
		} else {
			throw new WebApplicationException();
		}
	}

}
