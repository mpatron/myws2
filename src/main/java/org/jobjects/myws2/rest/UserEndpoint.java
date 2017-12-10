/**
 * 
 */
package org.jobjects.myws2.rest;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.orm.user.UserFacade;
import org.jobjects.myws2.tools.Tracked;
import io.swagger.annotations.Api;

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

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(User entity) {
    User returnValue = facade.save(entity);
    return Response.status(Response.Status.CREATED).entity(returnValue)
        .header("Location", "http://localhost:8880/api/users/" + returnValue.getId()).build();
  }

  @GET
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("id") UUID id) {
    User returnValue = facade.find(id);
    return Response.status(200).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header").allow("OPTIONS").build();
  }

  /**
   * Returns all resources (T) from the database
   *
   * @return
   * @throws WebApplicationException
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> read(@QueryParam("rangeMin") Integer rangeMin, @QueryParam("rangeMax") Integer rangeMax)
      throws WebApplicationException {
    List<User> returnValue = facade.findRange(rangeMin, rangeMax);
    return returnValue;
  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") UUID id, User entity) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (returnValue == null) {
      /* Creation */
      User entitySaved = facade.save(entity);
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
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") UUID id) throws WebApplicationException {
    User returnValue = facade.find(id);
    if (returnValue == null) {
      return Response.status(Response.Status.NOT_FOUND)// 404
          .build();
    } else {
      facade.remove(returnValue);
      return Response.status(Response.Status.NO_CONTENT)// 204
          .entity(returnValue).build();
    }
  }
}
