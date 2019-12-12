package org.jobjects.myws2.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.orm.address.AddressEnum;
import org.jobjects.myws2.orm.user.JSonImpTest;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AddressEndpointTest extends AbstractRemoteIT {
  private static Logger LOGGER = Logger.getLogger(UserEndpointTest.class.getName());
  // private final static String REDIRECT_PORT = "9143";
  private final static String REDIRECT_PORT = "8880";
  @ArquillianResource
  protected URL deployUrl;

  private User findUser(String email) {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users/byemail/" + URLEncoder.encode(email, "UTF-8");
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url).queryParam("email", email);
      Response response = webTarget.request().get();
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("Return => " + returnValue);
      } else {
        messageValidationError = "Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
    return returnValue;
  }

  private User getUser(UUID id) {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users/" + URLEncoder.encode(id.toString(), "UTF-8");
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url);
      Response response = webTarget.request().get();
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("Return => " + returnValue);
      } else {
        messageValidationError = "Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
    return returnValue;
  }

  public User createUser(User user) {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users";
      LOGGER.info("public void createUser() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url);
      Response response = webTarget.request().post(Entity.json(user));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("Return => " + returnValue);
      } else {
        messageValidationError = "Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
    return returnValue;
  }

  public Address createAddress(Address address) {
    Address returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/address";
      LOGGER.info("public void createUser() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url);
      Response response = webTarget.request().post(Entity.json(address));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<Address>() {
        });
        LOGGER.info("Return => " + returnValue);
      } else {
        messageValidationError = "Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
    return returnValue;
  }

  @Before
  public void beforeClass() {
    final String filePathname = "/org/jobjects/myws2/rest/random-users.json";
    try (BufferedReader in = new BufferedReader(new InputStreamReader(JSonImpTest.class.getResourceAsStream(filePathname), "UTF-8"));) {
      JsonReader parser = Json.createReader(in);
      JsonObject jsonObject = parser.readObject();
      JsonArray results = jsonObject.getJsonArray("results");
      results.stream().forEach(obj -> {
        if (JsonValue.ValueType.OBJECT == obj.getValueType()) {
          JsonObject jsonObjectValue = (JsonObject) obj;
          JsonObject name = jsonObjectValue.getJsonObject("name");
          User user;
          user = findUser(jsonObjectValue.getString("email"));
          if (user == null) {
            user = new User();
            user.setFirstName(name.getString("first"));
            user.setLastName(name.getString("last"));
            user.setEmail(jsonObjectValue.getString("email"));
            user = createUser(user);
            JsonObject location = jsonObjectValue.getJsonObject("location");
            Address address = new Address();
            address.setType(AddressEnum.HOME);
            address.setStreet(location.getString("street"));
            address.setCity(location.getString("city"));
            address.setState(location.getString("state"));
            address.setPostcode(Integer.toString(location.getInt("postcode")));
            address.setUser(user);
            createAddress(address);
          }
        }
      });
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

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
