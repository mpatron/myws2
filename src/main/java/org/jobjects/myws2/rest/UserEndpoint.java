/**
 * 
 */
package org.jobjects.myws2.rest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.orm.user.UserFacade;
import org.jobjects.myws2.tools.Tracked;

/**
 * sources
 * http://www.codingpedia.org/ama/tutorial-rest-api-design-and-implementation-in-java-with-jersey-and-spring/
 * http://www.codingpedia.org/ama/how-to-test-a-rest-api-from-command-line-with-curl/
 * @author Mickael
 *
 */
@Path("/users")
@Tracked
public class UserEndpoint {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());
  @EJB
  UserFacade facade;
  @Context
  UriInfo uriInfo;

  private boolean isValid(User user) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    if(null != user) {
      for (Address address : user.getAddress()) {
        address.setUser(user);
      }
    }
    LOGGER.info("Valiation in processing :" + ReflectionToStringBuilder.toString(user, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<User>> errors = factory.getValidator().validate(user);
    for (ConstraintViolation<User> error : errors) {
      LOGGER.severe("  - " + ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE) + " " + error.getMessage()
          + " due to " + error.getInvalidValue());
    }
    return (errors.size() == 0);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(User entity) {
    if (!isValid(entity)) {
      return Response.status(Response.Status.BAD_REQUEST)
          // 400
          .entity(entity).header("Location", uriInfo.getAbsolutePath()).build();
    }
    entity.setId(UUID.randomUUID());
    User returnValue = facade.save(entity);
    return Response.status(Response.Status.CREATED).entity(returnValue)
        .header("Location", uriInfo.getAbsolutePath().toString() + returnValue.getId()).build();
  }

  @GET
  @Path("{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("id") UUID id) {
    Response returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    try {
      LOGGER.info("public Response read(@PathParam(\"id\") UUID " + id + ")");
      User newEntity = facade.find(id);
      if (null == newEntity) {
        returnValue = Response.status(Response.Status.NO_CONTENT).header("Location", uriInfo.getAbsolutePath().toString() + id).build();
      } else {
        returnValue = Response.status(Response.Status.OK).entity(newEntity).header("Access-Control-Allow-Headers", "X-extra-header")
            .header("Location", uriInfo.getAbsolutePath().toString() + id).allow("OPTIONS").build();
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    return returnValue;
  }

  @GET
  @Path("/byemail/{email:[A-Z0-9._%-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response readByEmail(@PathParam("email") String email) {
    LOGGER.info("public Response read(@PathParam(\"email\") String " + email + ")");
    User returnValue = facade.findByEmail(email);
    return Response.status(Response.Status.OK).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header")
        .header("Location", uriInfo.getAbsolutePath().toString() + email).allow("OPTIONS").build();
  }

  /**
   * Returns all resources (T) from the database
   *
   * @return
   * @throws WebApplicationException
   */
  @GET
  // @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> readall(@DefaultValue("0") @QueryParam("rangeMin") Integer rangeMin,
      @DefaultValue("" + Integer.MAX_VALUE) @QueryParam("rangeMax") Integer rangeMax) throws WebApplicationException {
    LOGGER.info("public List<User> read(@QueryParam(\"rangeMin\") Integer \"" + rangeMin + "\", @QueryParam(\"rangeMax\") Integer \""
        + rangeMax + "\")");
    List<User> returnValue = facade.findRange(rangeMin, rangeMax);
    return returnValue;
  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") UUID id, User entity) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (isValid(entity)) {
      if (returnValue == null) {
        /* Creation */
        User entitySaved = facade.create(entity);
        return Response.status(Response.Status.CREATED)
            // 201
            .entity(entitySaved).header("Location", uriInfo.getAbsolutePath().toString() + entitySaved.getId()).build();
      } else {
        /* Mise à jour */
        User entitySaved = facade.save(entity);
        return Response.status(Response.Status.OK)
            // 200
            .entity(entitySaved).header("Location", uriInfo.getAbsolutePath().toString() + entitySaved.getId()).build();
      }
    } else {
      if (returnValue == null) {
        return Response.status(Response.Status.BAD_REQUEST)
            // 400
            .entity("il faut mettre les erreurs 123456789").header("Location", uriInfo.getAbsolutePath().toString() + entity.getId())
            .build();
      } else {
        return Response.status(Response.Status.CONFLICT)
            // 406
            .entity("il faut mettre les erreurs 123456789").header("Location", uriInfo.getAbsolutePath().toString() + entity.getId())
            .build();
      }
    }
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") UUID id) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (returnValue == null) {
      LOGGER.info("User => NOT_FOUND");
      return Response.status(Response.Status.NO_CONTENT)// 204
          .build();
    } else {
      LOGGER.info("User => NO_CONTENT" + ToStringBuilder.reflectionToString(returnValue));
      facade.remove(returnValue);
      return Response.status(Response.Status.OK)// 200
          .entity(returnValue).build();
    }
  }
}
