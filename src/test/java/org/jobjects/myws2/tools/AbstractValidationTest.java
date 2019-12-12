package org.jobjects.myws2.tools;

import static org.junit.Assert.assertFalse;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractValidationTest {
  private static Logger LOGGER = Logger.getLogger(AbstractValidationTest.class.getName());

  @BeforeClass
  public static void init() {
    JObjectsLogFormatter.initializeLogging();
  }

  @Test
  public final void testIsValid() {
    LOGGER.info("public final void testIsValid()");
    AbstractValidation<User> instance = new AbstractValidation<User>();
    User entity = new User();
    entity.setEmail("123456");
    assertFalse(instance.isValid(entity));
  }

  @Test
  public final void testGetValidationMessages() {
    LOGGER.info("public final void testGetValidationMessages()");
    AbstractValidation<User> instance = new AbstractValidation<User>();
    User entity = new User();
    entity.setEmail("123456");
    String message = instance.getValidationMessages(entity);
    LOGGER.info(message);
    assertFalse(StringUtils.isBlank(message));
  }
}
