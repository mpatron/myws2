package org.jobjects.myws2.tools.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

public class JObjectsLogFormatterTest {
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  @Test
  public void testLogRecord() {
    JObjectsLogFormatter.initializeLogging();
    LOGGER.entering(getClass().getName(), "testLogRecord");
    LOGGER.finest("le mega fin.");
    LOGGER.finer("le plus fin.");
    LOGGER.fine("le fin.");
    LOGGER.config("mode configuration");
    LOGGER.info("dernière news !");
    LOGGER.warning("attention");
    LOGGER.severe("la boulette");
    IllegalArgumentException iae = new IllegalArgumentException("Zut alors !");
    LOGGER.log(Level.SEVERE, iae.getMessage(), iae);
    LOGGER.exiting(getClass().getName(), "testLogRecord");
  }
}
