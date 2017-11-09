package org.jobjects.myws2.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.jobjects.myws2.rest.tools.Tracked;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "hello", description = "Get the time", tags = "hello")
@Path("/hello")
@Tracked
public class HelloWorldEndpoint {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @GET
  @Produces("text/plain")
  @ApiOperation(value = "Get the hello", notes = "Returns the hello as a string", response = String.class)
  public Response doGet() {
    return Response.ok("Hello GET from WildFly Swarm!").build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Post the hello", notes = "Returns the hello as a string", response = String.class)
  public Response doPost(MyBean myBean) {
    Response returnValue = null;
    try {
      myBean.setMessage("Hello world " + (myBean.getMessage() == null ? StringUtils.EMPTY : myBean.getMessage()) + " !");
      returnValue = Response.ok(myBean, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      returnValue = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    return returnValue;
  }
}
