package org.jobjects.myws2.tools;

import java.util.logging.Logger;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.jobjects.myws2.rest.tools.TrafficLogger;

@Provider
public class LoggerRegistration implements DynamicFeature {

  private static final transient Logger LOGGER = Logger.getLogger(LoggerRegistration.class.getName());

  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    String debug = System.getProperty("jax-rs.traffic");
    debug = "toto";
    if (debug != null) {
      context.register(new TrafficLogger());
      LOGGER.info("new TrafficLogger()");
    }
  }

}
