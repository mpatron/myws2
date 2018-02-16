package org.jobjects.myws2.rest;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AddressEndpointTest extends AbstractRemoteIT {
  private static Logger LOGGER = Logger.getLogger(UserEndpointTest.class.getName());
  // private final static String REDIRECT_PORT = "9143";
  private final static String REDIRECT_PORT = "8880";
  @ArquillianResource
  protected URL deployUrl;

  @Test
  @RunAsClient
  public void testReadIntegerInteger() {
    List<Address> returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/address";
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url).queryParam("rangeMin", 0).queryParam("rangeMax", Integer.MAX_VALUE);
      Response response = webTarget.request().get();
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<List<Address>>() {
        });
        LOGGER.info("Return => " + returnValue);
        returnValue.stream().parallel().forEach(u -> {
          LOGGER.info("User => " + ToStringBuilder.reflectionToString(u));
        });
        Assert.assertTrue(returnValue.size() >= 0);
      } else {
        messageValidationError = "Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
        Assert.assertTrue(false);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
  }
}
