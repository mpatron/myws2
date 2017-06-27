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

@Path("/hello")
public class HelloWorldEndpoint {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @GET
  @Produces("text/plain")
  public Response doGet() {
    return Response.ok("Hello GET from WildFly Swarm!").build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
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
