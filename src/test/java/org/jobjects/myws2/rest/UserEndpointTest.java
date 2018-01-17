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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jobjects.myws2.orm.user.JSonImpTest;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.tools.arquillian.AbstractRemoteIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserEndpointTest extends AbstractRemoteIT {
  private static Logger LOGGER = Logger.getLogger(UserEndpointTest.class.getName());
  // private final static String REDIRECT_PORT = "9143";
  private final static String REDIRECT_PORT = "8880";
  private static boolean FLAG = false;
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

  private User createUser(User user) {
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

  public void beforeClass() {
    final String filePathname = "/org/jobjects/myws2/rest/random-users.json";
    try (BufferedReader in = new BufferedReader(new InputStreamReader(JSonImpTest.class.getResourceAsStream(filePathname), "UTF-8"));) {
      JsonReader parser = Json.createReader(in);
      JsonObject jsonObject = parser.readObject();
      JsonArray results = jsonObject.getJsonArray("results");
      results.stream().forEach(obj -> {
        JsonObject prof = (JsonObject) obj;
        JsonObject name = prof.getJsonObject("name");
        User user;
        user = findUser(prof.getString("email"));
        if (user == null) {
          user = new User();
          user.setFirstName(name.getString("first"));
          user.setLastName(name.getString("last"));
          user.setEmail(prof.getString("email"));
          createUser(user);
        }
      });
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    FLAG = true;
  }

  @Test
  @RunAsClient
  public void testCreate() {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users";
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      User user = new User();
      user.setFirstName(RandomStringUtils.random(10));
      user.setEmail(RandomStringUtils.random(10, true, false).toLowerCase() + "@fr.fr");
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
        Assert.assertTrue(false);
      }
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
  }

  @Test
  public void testReadUUID() {
    try {
      User user = new User();
      String email = RandomStringUtils.randomAlphabetic(10) + "@fr.Fr";
      email = email.toLowerCase();
      user.setEmail(email);
      user.setFirstName(RandomStringUtils.randomAlphabetic(10));
      User user2 = createUser(user);
      User user3 = getUser(user2.getId());
      Assert.assertEquals(user2.getFirstName(), user3.getFirstName());
    } catch (Throwable e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
      Assert.assertTrue(false);
    }
  }

  @Test
  public void testReadIntegerInteger() {
    if (!FLAG) {
      beforeClass();
    }
    List<User> returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users";
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url).queryParam("rangeMin", 0).queryParam("rangeMax", Integer.MAX_VALUE);
      Response response = webTarget.request().get();
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<List<User>>() {
        });
        LOGGER.info("Return => " + returnValue);
        returnValue.stream().parallel().forEach(u -> {
          LOGGER.info("User => " + ToStringBuilder.reflectionToString(u));
        });
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

  @Test
  public void testUpdate() {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users";
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      User user = new User();
      
      /**
       * Create
       */
      user.setFirstName(RandomStringUtils.random(10));
      user.setEmail(RandomStringUtils.random(10, true, false).toLowerCase() + "@fr.fr");
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
        Assert.assertTrue(false);
      }
      
      /**
       * Update
       */
      UUID id= returnValue.getId();
      user.setId(id);
      String oldValue = user.getEmail();
      String newValue = RandomStringUtils.random(10, true, false).toLowerCase() + "@fr.fr";
      user.setEmail(newValue);
      webTarget = client.target(url+"/"+id);
      response = webTarget.request().put(Entity.json(user));
      statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("Return => " + returnValue);
        Assert.assertEquals(newValue, returnValue.getEmail());
        Assert.assertNotEquals(oldValue, returnValue.getEmail());
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

  @Test
  public void testDelete() {
    User returnValue = null;
    String messageValidationError = null;
    try {
      String url = deployUrl.toString().replace("8080", REDIRECT_PORT) + "api/users";
      LOGGER.info("public void testReadIntegerInteger() {} to " + url);
      User user = new User();
      
      /**
       * Create
       */
      user.setFirstName(RandomStringUtils.random(10));
      user .setLastName(RandomStringUtils.random(10));
      user.setEmail(RandomStringUtils.random(10, true, false).toLowerCase() + "@fr.fr");
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(url);
      Response response = webTarget.request().post(Entity.json(user));
      StatusType statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("On client, Return => " + returnValue);
      } else {
        messageValidationError = "On client, Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
            + " Contenu : " + (response.bufferEntity() ? response.readEntity(String.class) : "<empty>");
        LOGGER.log(Level.WARNING, messageValidationError);
        Assert.assertTrue(false);
      }
      
      /**
       * Delete
       */
      UUID id= returnValue.getId();
      webTarget = client.target(url+"/"+id);
      response = webTarget.request().delete();
      statusType = response.getStatusInfo();
      if (Response.Status.Family.SUCCESSFUL.equals(Response.Status.Family.familyOf(statusType.getStatusCode()))) {
        LOGGER.info("response.getEntity()=" + response.getEntity());
        returnValue = response.readEntity(new GenericType<User>() {
        });
        LOGGER.info("On client, Return => " + returnValue);
        Assert.assertEquals(user.getFirstName(), returnValue.getFirstName());
        Assert.assertEquals(user.getEmail(), returnValue.getEmail());
      } else {
        messageValidationError = "On client, Return => Reason : HTTP[" + statusType.getStatusCode() + "] " + statusType.getReasonPhrase()
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
