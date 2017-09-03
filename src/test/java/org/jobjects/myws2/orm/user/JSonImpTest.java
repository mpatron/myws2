package org.jobjects.myws2.orm.user;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jobjects.myws2.tools.log.JObjectsLogFormatter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSonImpTest {
  private static Logger LOGGER = Logger.getLogger(JSonImpTest.class.getName());

  private static List<User> users = Collections.synchronizedList(new ArrayList<>());

  /**
   * @return the users
   */
  public static List<User> getUsers() {
    return users;
  }

  @BeforeClass
  public static void setUpBeforeClass2() throws Exception {
    //JObjectsLogFormatter.initializeLogging();
    final String filePathname = "/org/jobjects/myws2/rest/random-users.json";
    try (InputStream is = JSonImpTest.class.getResourceAsStream(filePathname)) {
      JsonReader parser = Json.createReader(is);
      JsonObject jsonObject = parser.readObject();
      JsonArray results = jsonObject.getJsonArray("results");
      results.stream().forEach(obj -> {
        JsonObject prof = (JsonObject) obj;
        JsonObject name = prof.getJsonObject("name");
        User user = new User();
        user.setFirstName(name.getString("first"));
        user.setLastName(name.getString("last"));
        user.setEmail(prof.getString("email"));
        users.add(user);
      });
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

  // @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    JObjectsLogFormatter.initializeLogging();
    final String filePathname = "org/jobjects/myws2/rest/random-users.json";
    try {
      URL url = ClassLoader.getSystemResource(filePathname);
      if (url != null) {
        // Path path = Paths.get(url.toURI());
        Path path = Paths.get(JSonImpTest.class.getResource("/org/jobjects/myws2/random-users.json").toURI());
        if (Files.isReadable(path)) {
          JsonReader parser = Json.createReader(new FileReader(path.toAbsolutePath().toString()));
          JsonObject jsonObject = parser.readObject();
          JsonArray results = jsonObject.getJsonArray("results");
          results.stream().forEach(obj -> {
            JsonObject prof = (JsonObject) obj;
            JsonObject name = prof.getJsonObject("name");
            User user = new User();
            user.setFirstName(name.getString("first"));
            user.setLastName(name.getString("last"));
            user.setEmail(prof.getString("email"));
            users.add(user);
          });
        } else {
          LOGGER.severe("Le fichier " + filePathname + " est illisible : " + path.toAbsolutePath());
        }
      } else {
        LOGGER.severe("Le chemin d'accès à " + filePathname + " est introuvable.");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
  }

  @Test
  public void test() {
    Assert.assertNotNull(users);
    Assert.assertTrue(users.size() > 0);
    users.stream().parallel().forEach(u -> {
      LOGGER.info("first=" + u.getFirstName() + " last=" + u.getLastName() + " email=" + u.getEmail());
    });
  }

}
