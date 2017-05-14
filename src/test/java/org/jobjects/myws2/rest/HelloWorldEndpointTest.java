package org.jobjects.myws2.rest;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;
import org.wildfly.swarm.config.webservices.ClientConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@RunWith(Arquillian.class)
@DefaultDeployment
public class HelloWorldEndpointTest {
  private static Logger LOGGER = Logger.getLogger(HelloWorldEndpointTest.class.getName());
  // private final static String REDIRECT_PORT = "9143";
  private final static String REDIRECT_PORT = "8880";
  @ArquillianResource
  protected URL deployUrl;

  @Test
  @RunAsClient
  public void testNothing() {
    MyBean returnValue = null;
    String messageValidationError = null;
    LOGGER.info("public void testNothing() {} to " + deployUrl);
    try {
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/hello");
      MyBean myBean = new MyBean();
      myBean.setMessage("toto");
      Response response = webTarget.request().post(Entity.json(myBean));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(MyBean.class);
        LOGGER.info("Return -> " + returnValue);
      } else {
        messageValidationError = statusType.getReasonPhrase() + " => "
            + (response.bufferEntity() ? response.readEntity(String.class) : "");
        LOGGER.log(Level.WARNING, messageValidationError);
      }
      Assert.assertEquals("Hello world toto", returnValue.getMessage());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
  }
}
