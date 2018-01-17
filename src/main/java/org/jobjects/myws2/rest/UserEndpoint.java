/**
 * 
 */
package org.jobjects.myws2.rest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.orm.user.UserFacade;
import org.jobjects.myws2.tools.Tracked;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * sources
 * http://www.codingpedia.org/ama/tutorial-rest-api-design-and-implementation-in-java-with-jersey-and-spring/
 * http://www.codingpedia.org/ama/how-to-test-a-rest-api-from-command-line-with-curl/
 * @author Mickael
 *
 */
@Path("/users")
@Api(value = "user", description = "Create, Show, update and delete user", tags = "user")
@Tracked
public class UserEndpoint {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());
  @EJB
  UserFacade facade;

  private boolean isValid(User user) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(user, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<User>> errors = factory.getValidator().validate(user);
    for (ConstraintViolation<User> error : errors) {
      LOGGER.severe(ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE) + " " + error.getMessage()
          + " due to " + error.getInvalidValue());
    }
    return (errors.size() == 0);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Post the user", notes = "Returns the user as a json", response = User.class)
  public Response create(User entity) {
    if (!isValid(entity)) {
      return Response.status(Response.Status.BAD_REQUEST)
          // 400
          .entity(entity).header("Location", "http://localhost:8880/api/users").build();
    }
    entity.setId(UUID.randomUUID());
    User returnValue = facade.save(entity);
    return Response.status(Response.Status.CREATED).entity(returnValue)
        .header("Location", "http://localhost:8880/api/users/" + returnValue.getId()).build();
  }

  @GET
  @Path("{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get the user by id", notes = "Returns the user as a json", response = User.class)
  public Response read(@PathParam("id") UUID id) {
    LOGGER.info("public Response read(@PathParam(\"id\") UUID " + id + ")");
    User returnValue = facade.find(id);
    return Response.status(200).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header").allow("OPTIONS").build();
  }

  @GET
  @Path("/byemail/{email:[A-Z0-9._%-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get the user by email", notes = "Returns the user as a json", response = User.class)
  public Response readbyemil(@PathParam("email") String email) {
    LOGGER.info("public Response read(@PathParam(\"email\") String " + email + ")");
    User returnValue = facade.findByEmail(email);
    return Response.status(200).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header").allow("OPTIONS").build();
  }

  /**
   * Returns all resources (T) from the database
   *
   * @return
   * @throws WebApplicationException
   */
  @GET
  //@Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all users", notes = "Returns the user list as a json", response = List.class)
  public List<User> readall(
      @ApiParam(
          defaultValue = "0",
          name = "rangeMin",
          required = false,
          example = "0") @DefaultValue("0") @QueryParam("rangeMin") Integer rangeMin,
      @ApiParam(
          defaultValue = "\"" + Integer.MAX_VALUE + "\"",
          name = "rangeMax",
          required = false,
          example = "" + Integer.MAX_VALUE) @DefaultValue(""
              + Integer.MAX_VALUE) @QueryParam("rangeMax") Integer rangeMax)
      throws WebApplicationException {
    LOGGER.info("public List<User> read(@QueryParam(\"rangeMin\") Integer \"" + rangeMin + "\", @QueryParam(\"rangeMax\") Integer \""
        + rangeMax + "\")");
    List<User> returnValue = facade.findRange(rangeMin, rangeMax);
    return returnValue;
  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Put the user", notes = "Returns the user as a json", response = User.class)
  public Response update(@PathParam("id") UUID id, User entity) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (isValid(entity)) {
      if (returnValue == null) {
        /* Creation */
        User entitySaved = facade.create(entity);
        return Response.status(Response.Status.CREATED)
            // 201
            .entity(entitySaved).header("Location", "http://localhost:8880/api/users/" + entitySaved.getId()).build();
      } else {
        /* Mise à jour */
        User entitySaved = facade.save(entity);
        return Response.status(Response.Status.OK)
            // 200
            .entity(entitySaved).header("Location", "http://localhost:8880/api/users/" + entitySaved.getId()).build();
      }
    } else {
      if (returnValue == null) {
        return Response.status(Response.Status.BAD_REQUEST)
            // 400
            .entity("il faut mettre les erreurs 123456789").header("Location", "http://localhost:8880/api/users/" + entity.getId()).build();
      } else {
        return Response.status(Response.Status.CONFLICT)
            // 406
            .entity("il faut mettre les erreurs 123456789").header("Location", "http://localhost:8880/api/users/" + entity.getId()).build();
      }
    }
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Delete the user", notes = "Returns the user as a json", response = User.class)
  public Response delete(@PathParam("id") UUID id) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (returnValue == null) {
      LOGGER.info("User => NOT_FOUND");
      return Response.status(Response.Status.NOT_FOUND)// 404
          .build();
    } else {
      LOGGER.info("User => NO_CONTENT" + ToStringBuilder.reflectionToString(returnValue));
      facade.remove(returnValue);
      return Response.status(Response.Status.NO_CONTENT)// 204
          .entity(returnValue).build();
    }
  }
}
