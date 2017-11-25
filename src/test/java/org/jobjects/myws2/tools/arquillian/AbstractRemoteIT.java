package org.jobjects.myws2.tools.arquillian;

import java.util.logging.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;

/**
 * @author Mickael Patron 2015
 * @version 1.0
 **/
public abstract class AbstractRemoteIT extends AbstractIT {
  private static Logger LOGGER = Logger.getLogger(AbstractRemoteIT.class.getName());

  @Deployment(testable = false)
  public static WebArchive deployement() {
    //JObjectsLogFormatter.initializeLogging();
    LOGGER.info("Remote mode, looking log into current console.");
    return AbstractIT.createTestableDeployment();
  }
}
