package org.jobjects.myws2.orm.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidStringCountryISO2Validator implements ConstraintValidator<ValidStringCountryISO2, String> {
  // private static final Set<String> ISO_LANGUAGES = new
  // HashSet<String>(Arrays.asList(Locale.getISOLanguages()));
  public static final Set<String> ISO_COUNTRIES = new HashSet<String>(Arrays.asList(Locale.getISOCountries()));
  public static final Set<String> ISO_ONLY_COUNTRIES = new HashSet<String>(Arrays.asList("DE","TH","SG","MY","AU","NZ"));
  
  private boolean onlyCountry=false;

  @Override
  public void initialize(ValidStringCountryISO2 constraintAnnotation) {
    this.onlyCountry = constraintAnnotation.onlyCountry();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean returnValue = false;
    if (StringUtils.isEmpty(value)) {
      //On ne valide rien car c'est vide.
      returnValue = true;
    } else {
    //On valide avec ou pas la restriction.
      if(onlyCountry) {
        returnValue = ISO_ONLY_COUNTRIES.contains(value);
      } else {
        returnValue = ISO_COUNTRIES.contains(value);
      }
    }
    return returnValue;
  }
}
