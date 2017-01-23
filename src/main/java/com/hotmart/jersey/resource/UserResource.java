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

@Path("/users")
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
	public Response getUserByUserName(@PathParam("userName") String userName) {
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
	public Collection<HotMessage> getChatHistoryByUserName(@PathParam("sender") String sender, @PathParam("receiver") String receiver) {
		 return HotChatController.getChatHistory(sender, receiver);
	}
	
	@GET
	@Path("/offline/{userName}")
	public Collection<HotMessage> getOfflineMessages(@PathParam("userName") String userName) {
		 return HotChatController.expurgateOfflineMessages(userName);
	}
	
	@POST
	@Path("/{userName}/{status}")
	public Response setUserOnlineStatus(@PathParam("userName") String userName, @PathParam("status") boolean status) {
		 HotChatController.getUser(userName).setOnline(status);
		 return Response.status(200).entity("User status set").build();
	}

	@POST
	@Path("/authenticate")
	public Response authenticateUser(User user) {
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
	public Response create(User user) {
		logger.info("creating new user...");
		if (user != null) {
			if (HotChatController.getUsers().get(user.getUserName()) == null) {
				user.setChatsHistory(new HashMap<String,List<HotMessage>>());
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
	public void delete(@PathParam("userName") String userName) {
		logger.info("deleting user...");
		if (userName != null) {
			HotChatController.getUsers().remove(userName);
			logger.info("user " + userName + " deleted.");
		} else {
			throw new WebApplicationException();
		}
	}

}
