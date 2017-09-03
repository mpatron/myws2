package org.jobjects.myws2.tools.arquillian;

import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public abstract class AbstractLocalIT extends AbstractIT {
  private static Logger LOGGER = Logger.getLogger(AbstractLocalIT.class.getName());

  @Deployment
  public static WebArchive createTestableDeployment() {
    JObjectsLogFormatter.initializeLogging();
    LOGGER.info("Local mode, looking log into JEE server log file.");
    return AbstractIT.createTestableDeployment();
  }

}
