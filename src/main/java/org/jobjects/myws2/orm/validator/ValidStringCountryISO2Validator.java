package org.jobjects.myws2.orm.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidStringCountryISO2Validator implements ConstraintValidator<ValidStringCountryISO2, String> {
  public static final List<String> ISO_COUNTRIES = Arrays.asList(Locale.getISOCountries());
  public static final List<String> ISO_ONLY_COUNTRIES = Arrays.asList("FR", "DE", "IT", "BE", "ES");
  private boolean onlyCountry = false;

  @Override
  public void initialize(ValidStringCountryISO2 constraintAnnotation) {
    this.onlyCountry = constraintAnnotation.onlyCountry();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean returnValue = false;
    if (StringUtils.isEmpty(value)) {
      // On ne valide rien car c'est vide.
      returnValue = true;
    } else {
      // On valide avec ou pas la restriction.
      if (onlyCountry) {
        returnValue = ISO_ONLY_COUNTRIES.contains(value);
      } else {
        returnValue = ISO_COUNTRIES.contains(value);
      }
    }
    return returnValue;
  }
}
