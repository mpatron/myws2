package org.jobjects.myws2.orm.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ValidStringCountryISO2Validator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStringCountryISO2 {
	String message() default "{org.jobjects.myws2.orm.validator.ValidStringCountryISO2.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	boolean onlyCountry() default false;
}
