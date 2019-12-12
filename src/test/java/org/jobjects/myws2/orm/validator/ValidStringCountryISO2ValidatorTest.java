package org.jobjects.myws2.orm.validator;

import static org.junit.Assert.assertTrue;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.jboss.arquillian.junit.Arquillian;
import org.jobjects.myws2.tools.log.JObjectsLogFormatter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.arquillian.DefaultDeployment;

@RunWith(Arquillian.class)
@DefaultDeployment
public class ValidStringCountryISO2ValidatorTest {
  private static Logger LOGGER = Logger.getLogger(ValidStringCountryISO2ValidatorTest.class.getName());

  @BeforeClass
  public static void init() {
    JObjectsLogFormatter.initializeLogging();
    StringBuffer sb = new StringBuffer();
    for (String iso2 : ValidStringCountryISO2Validator.ISO_COUNTRIES.stream().sorted().collect(Collectors.toList())) {
      sb.append(iso2 + " ");
    }
    LOGGER.info(sb.toString());
  }

  public class MyBeanTrue {
    public MyBeanTrue() {
    }

    @ValidStringCountryISO2(onlyCountry = true, message = "E92")
    private String state;

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }
  }

  public class MyBeanFalse {
    public MyBeanFalse() {
    }

    @ValidStringCountryISO2
    private String state;

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }
  }

  private void printMyBeanTrue(Set<ConstraintViolation<MyBeanTrue>> constraintViolations) {
    if (constraintViolations != null && constraintViolations.size() > 0) {
      boolean hasError = false;
      StringBuffer sb = new StringBuffer();
      sb.append("Impossible de valider les donnees du bean : ");
      sb.append(System.lineSeparator());
      for (ConstraintViolation<MyBeanTrue> contraintes : constraintViolations) {
        sb.append("  - SimpleName=" + contraintes.getRootBeanClass().getSimpleName());
        sb.append(" PropertyPath=" + contraintes.getPropertyPath());
        sb.append(" Message=" + contraintes.getMessage());
        sb.append(" InvalidValue=" + contraintes.getInvalidValue());
        sb.append(System.lineSeparator());
        hasError = true;
      }
      if (hasError) {
        LOGGER.info(sb.toString());
      }
    }
  }

  private void printMyBeanFalse(Set<ConstraintViolation<MyBeanFalse>> constraintViolations) {
    if (constraintViolations != null && constraintViolations.size() > 0) {
      boolean hasError = false;
      StringBuffer sb = new StringBuffer();
      sb.append("Impossible de valider les donnees du bean : ");
      sb.append(System.lineSeparator());
      for (ConstraintViolation<MyBeanFalse> contraintes : constraintViolations) {
        sb.append("  - SimpleName=" + contraintes.getRootBeanClass().getSimpleName());
        sb.append(" PropertyPath=" + contraintes.getPropertyPath());
        sb.append(" Message=" + contraintes.getMessage());
        sb.append(" InvalidValue=" + contraintes.getInvalidValue());
        sb.append(System.lineSeparator());
        hasError = true;
      }
      if (hasError) {
        LOGGER.info(sb.toString());
      }
    }
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de "DE"
   * Resultat attendu : True
   */
  @Test
  public void testStateIsDEOnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState("DE");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue(constraintViolations.size() == 0);
    assertTrue("Bonne detection de DE", constraintViolations.size() == 0);
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de "FR"
   * Resultat attendu : False
   */
  @Test
  public void testStateIsFROnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState("UK");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue("Bonne detection de UK", constraintViolations.size() > 0);
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de "XX"
   * Resultat attendu : False
   */
  @Test
  public void testStateIsXXOnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState("XX");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue("Bonne detection de XX", constraintViolations.size() > 0);
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de " "
   * Resultat attendu : False
   */
  @Test
  public void testStateIsBlanckOnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState("  ");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue("Bonne detection de blanc", constraintViolations.size() > 0);
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de ""
   * Resultat attendu : True
   */
  @Test
  public void testStateIsEmptyOnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState("");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue("Bonne detection de empty", constraintViolations.size() == 0);
  }

  /**
   * Seul les pays : "FR","DE","IT","BE","ES"
   * Vérification de null
   * Resultat attendu : True
   */
  @Test
  public void testStateIsNullOnlyCountry() {
    MyBeanTrue myBean = new MyBeanTrue();
    myBean.setState(null);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanTrue>> constraintViolations = validator.validate(myBean);
    printMyBeanTrue(constraintViolations);
    assertTrue("Bonne detection de empty", constraintViolations.size() == 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de "DE"
   * Resultat attendu : True
   */
  @Test
  public void testStateIsDENotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState("DE");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue(constraintViolations.size() == 0);
    assertTrue("Bonne detection de DE", constraintViolations.size() == 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de "FR"
   * Resultat attendu : True
   */
  @Test
  public void testStateIsFRNotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState("FR");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue("Bonne detection de FR", constraintViolations.size() == 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de "XX"
   * Resultat attendu : False
   */
  @Test
  public void testStateIsXXNotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState("XX");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue("Bonne detection de XX", constraintViolations.size() > 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de " "
   * Resultat attendu : False
   */
  @Test
  public void testStateIsBlanckNotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState("  ");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue("Bonne detection de blanc", constraintViolations.size() > 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de ""
   * Resultat attendu : True
   */
  @Test
  public void testStateIsEmptyNotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState("");
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue("Bonne detection de empty", constraintViolations.size() == 0);
  }

  /**
   * Tous les pays ISO-2 3166
   * Vérification de null
   * Resultat attendu : True
   */
  @Test
  public void testStateIsNullNotOnlyCountry() {
    MyBeanFalse myBean = new MyBeanFalse();
    myBean.setState(null);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<MyBeanFalse>> constraintViolations = validator.validate(myBean);
    printMyBeanFalse(constraintViolations);
    assertTrue("Bonne detection de empty", constraintViolations.size() == 0);
  }
}
