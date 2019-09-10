package org.jobjects.myws2.rest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jobjects.myws2.tools.Tracked;

@Path("/mytimeout")
@Tracked
public class TimeOutEndpoint {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response doGet() throws InterruptedException {
    LOGGER.log(Level.INFO, "/mytimeout");
    // Pause de 20 secondes pour entrer dans les timeout de
    /*
     * • max-processing-time
     * • no-request-timeout
     * • processing-time
     */
    TimeUnit.SECONDS.sleep(20);
    return Response.ok("Timeout GET from WildFly Swarm!").build();
  }
}
