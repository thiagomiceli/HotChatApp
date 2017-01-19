package com.hotmart.jersey.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
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

import com.hotmart.hotchat.dto.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	/**
	 * Logger
	 */
	private final Logger logger = Logger.getLogger(getClass().getName());

	private static Map<String, User> users = Collections.synchronizedMap(new LinkedHashMap<String, User>());

	/**
	 * Get all registered users
	 * 
	 * @return the users
	 */
	@GET
	public Collection<User> getAllUsers() {
		return users.values();
	}

	/**
	 * Get registered user by userName
	 * 
	 * @return the users
	 */
	@GET
	@Path("/{userName}")
	public Response getUserByUserName(@PathParam("userName") String userName) {
		User retrievedUser = users.get(userName);
		if (retrievedUser != null) {
			return Response.status(200).entity(users.get(userName)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Incorrect username or password!")
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@Path("/authenticate")
	public Response authenticateUser(User user) {
		logger.info("authenticate [userName:" + user.getUserName() + ", " + "password:" + user.getPassword() + "]");
		User retrievedUser = users.get(user.getUserName());
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
			if (users.get(user.getUserName()) == null) {
				users.put(user.getUserName(), user);
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
			users.remove(userName);
			logger.info("user " + userName + " deleted.");
		} else {
			throw new WebApplicationException();
		}
	}

}
