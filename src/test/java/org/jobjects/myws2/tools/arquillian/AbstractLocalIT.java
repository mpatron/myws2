package org.jobjects.myws2.tools.arquillian;

import java.io.File;
import java.util.logging.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public abstract class AbstractLocalIT extends AbstractIT {
  private static Logger LOGGER = Logger.getLogger(AbstractLocalIT.class.getName());

  @Deployment
  public static WebArchive createTestableDeployment() {
    WebArchive returnValue = null;
    //JObjectsLogFormatter.initializeLogging();
    LOGGER.info("Local mode, looking log into JEE server log file.");
    returnValue = AbstractIT.createTestableDeployment();
    // File[] commons_lang3 =
    // pom.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile();
    // war.addAsLibraries(commons_lang3);
//    PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");
//    File[] jars = pom.resolve("org.hibernate:hibernate-validator").withTransitivity().asFile();
//    returnValue.addAsLibraries(jars);
    return returnValue;
  }
}
