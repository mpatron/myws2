package org.jobjects.myws2.orm.user;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.orm.address.AddressEnum;
import org.jobjects.myws2.orm.address.AddressFacade;
import org.jobjects.myws2.tools.arquillian.AbstractLocalIT;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserStalessTest extends AbstractLocalIT {
  private static Logger LOGGER = Logger.getLogger(UserStalessTest.class.getName());
  @EJB
  UserFacade userFacade;
  @EJB
  AddressFacade addressFacade;

  @BeforeClass
  public static void init() {
    JObjectsLogFormatter.initializeLogging();
  }

  @Test()
  public void testLoading() {
    LOGGER.info("public void testLoading()");
    List<User> users = null;
    try {
      JSonImpTest.setUpBeforeClass2();
      users = JSonImpTest.getUsers();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
      Assert.fail(e.getLocalizedMessage());
    }
    Assert.assertNotNull(users);
    Assert.assertTrue(users.size() > 0);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    users.stream().parallel().forEach(u -> {
      LOGGER.info("first=" + u.getFirstName() + " last=" + u.getLastName() + " email=" + u.getEmail());
      Set<ConstraintViolation<User>> errors = factory.getValidator().validate(u);
      for (ConstraintViolation<User> error : errors) {
        LOGGER.severe(ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE) + " " + error.getMessage()
            + " due to " + error.getInvalidValue());
      }
      if (errors.size() == 0) {
        userFacade.create(u);
      }
    });
  }

  @Test
  public void testCreate() {
    LOGGER.info("public void testCreate()");
    User user = new User();
    user.setEmail("mpt@gmail.com");
    user.setFirstName("Mickaël");
    user.setLastName("Patron");
    Address address = new Address();
    address.setCity("city");
    address.setStreet("street");
    address.setUser(user);
    address.setType(AddressEnum.HOME);
    user.getAddress().add(address);
    User user33 = userFacade.create(user);
    Assert.assertNotNull(user33.getId());
    Assert.assertTrue(StringUtils.isNotBlank(user33.getId().toString()));
    User user2 = userFacade.find(user.getId());
    Assert.assertNotNull(user2);
    Assert.assertTrue(user2.getAddress().size() > 0);
    List<User> users = userFacade.findByFirstName("Mickaël");
    for (User user3 : users) {
      Assert.assertNotNull(user3);
    }
  }

  @Test
  public void testSave() {
    LOGGER.info("public void testSave()");
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      List<User> users2 = userFacade.findByFirstName("Mickaël");
      for (User user : users2) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    }
  }

  @Test
  public void testRemove() {
    LOGGER.info("public void testRemove()");
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        userFacade.remove(user);
      }
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 0);
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findByFirstName("Mickaël").size() == 0);
    }
  }

  @Test
  public void testFind() {
    LOGGER.info("public void testFind()");
    List<User> users = userFacade.findByFirstName("Mickaël");
    if (users.size() > 0) {
      for (User user : users) {
        User user1 = userFacade.find(user.getId());
        Assert.assertTrue(user.getId().equals(user1.getId()));
        break;
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertNotNull(userFacade.find(user1.getId()));
      userFacade.remove(user1);
      Assert.assertNull(userFacade.find(user1.getId()));
    }
  }

  @Test
  public void testFindAll() {
    LOGGER.info("public void testFindAll()");
    List<User> users = userFacade.findAll();
    if (users.size() > 0) {
      for (User user : users) {
        Assert.assertNotNull(user);
        break;
      }
    } else {
      Assert.assertTrue(userFacade.findAll().size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findAll().size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findAll().size() == 0);
    }
  }

  @Test
  public void testFindRange() {
    LOGGER.info("public void testFindRange()");
    List<User> users = userFacade.findRange(0, Integer.MAX_VALUE);
    if (users.size() > 0) {
      for (User user : users) {
        Assert.assertNotNull(user);
        break;
      }
    } else {
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 1);
      userFacade.remove(user1);
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
    }
  }

  @Test
  public void testCount() {
    LOGGER.info("public void testCount()");
    List<User> users = userFacade.findAll();
    if (users.size() > 0) {
      Assert.assertTrue(true);
    } else {
      Assert.assertTrue(userFacade.findRange(0, Integer.MAX_VALUE).size() == 0);
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findAll().size() > 0);
    }
  }

  @Test
  public void testFindByNamedQuery() {
    LOGGER.info("public void testFindByNamedQuery()");
    List<User> users = userFacade.findByNamedQuery(User.FIND_BY_EMAIL, "mpt@gmail.com");
    if (users.size() > 0) {
      for (User user : users) {
        if (user != null) {
          user.setEmail("mpt@hotmail.com");
          User userNew = userFacade.save(user);
          Assert.assertTrue(StringUtils.equals(user.getEmail(), userNew.getEmail()));
          break;
        } else {
          Assert.assertTrue(false);
        }
      }
    } else {
      User user1 = new User();
      user1.setEmail("mpt@gmail.com");
      user1.setFirstName("Mickaël");
      user1.setLastName("Patron");
      userFacade.create(user1);
      Assert.assertTrue(userFacade.findByNamedQuery(User.FIND_BY_EMAIL, "mpt@gmail.com").size() > 0);
    }
  }
}
