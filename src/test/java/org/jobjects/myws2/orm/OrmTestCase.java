package org.jobjects.myws2.orm;

import static org.junit.Assert.assertTrue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jobjects.myws2.orm.user.UserStalessTest;
import org.jobjects.myws2.tools.arquillian.AbstractIT;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore("Bug on niveau de la recherche du driver h2")
@RunWith(Arquillian.class)
public class OrmTestCase {
  private static Logger LOGGER = Logger.getLogger(UserStalessTest.class.getName());
  protected static final String[] DEPENDENCIES = { "org.hibernate:hibernate-entitymanager:5.1.10.Final", };

  protected static JavaArchive thirdPartyLibs() {
    JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "mylibs.jar");
    for (String dependency : DEPENDENCIES) {
      lib.merge(Maven.resolver().resolve(dependency).withoutTransitivity().asSingle(JavaArchive.class));
    }
    return lib;
  }

  @Deployment
  public static WebArchive createTestableDeployment() {
    WebArchive returnValue = null;
    LOGGER.info("Local mode, looking log into JEE server log file.");
    returnValue = AbstractIT.createTestableDeployment();
    // PomEquippedResolveStage pom =
    // Maven.resolver().loadPomFromFile("pom.xml");
    // File[] hibernate_entitymanager =
    // Maven.resolver().resolve("org.hibernate:hibernate-entitymanager:5.0.10.Final").withTransitivity().asFile();
    // returnValue.addAsLibraries(hibernate_entitymanager);
    // File[] hibernate_core =
    // Maven.resolver().resolve("org.hibernate:hibernate-core:5.0.10.Final").withTransitivity().asFile();
    // returnValue.addAsLibraries(hibernate_core);
    // File[] hibernate_ehcache =
    // Maven.resolver().resolve("org.hibernate:hibernate-ehcache:5.0.10.Final").withTransitivity().asFile();
    // returnValue.addAsLibraries(hibernate_ehcache);
    // File[] dom4j =
    // Maven.resolver().resolve("dom4j:dom4j:1.6.1").withTransitivity().asFile();
    // returnValue.addAsLibraries(dom4j);
    // returnValue.addAsLibraries(thirdPartyLibs());
    return returnValue;
  }

  @Test
  public void test() {
    LOGGER.info("public void OrmTestCase.test()");
    try {
      Map<String, String> properties = new HashMap<String, String>();
      properties.put("javax.persistence.database-product-name", "H2");
      properties.put("javax.persistence.database-major-version", "1");
      properties.put("javax.persistence.database-minor-version", "3");
      properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
      properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
      properties.put("javax.persistence.jdbc.user", "test");
      properties.put("javax.persistence.jdbc.password", "test");
      properties.put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
      properties.put("javax.persistence.schema-generation.scripts.drop-target", "./jpa21-generate-schema-no-connection-drop.jdbc");
      properties.put("javax.persistence.schema-generation.scripts.create-target", "./jpa21-generate-schema-no-connection-create.jdbc");
      properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
      Persistence.generateSchema("myPersistence", properties);
      assertTrue(Files.exists(Paths.get(properties.get("javax.persistence.schema-generation.scripts.create-target"))));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
  }
}
