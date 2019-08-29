package org.jobjects.myws2.tools;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class AbstractValidation<T> {
  /**
   * Instance du logger.
   */
  private Logger LOGGER = Logger.getLogger(getClass().getName());

  public boolean isValid(final T entity) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<T>> errors = factory.getValidator().validate(entity);
    for (ConstraintViolation<T> error : errors) {
      LOGGER
          .severe("AbstractEndPoint.isValid => " + ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE)
              + " " + error.getMessage() + " due to " + error.getInvalidValue());
    }
    return (errors.size() == 0);
  }

  public String getValidationMessages(final T entity) {
    String returnValue = null;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<T>> errors = factory.getValidator().validate(entity);
    if (errors.size() > 0) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        ObjectNode jsonReturnValue = mapper.createObjectNode();
        jsonReturnValue.putPOJO("entity", entity);
        ArrayNode arrayNode = mapper.createArrayNode();
        for (ConstraintViolation<T> error : errors) {
          ObjectNode jsonItem = mapper.createObjectNode();
          jsonItem.put("filedName", String.valueOf(error.getPropertyPath()));
          jsonItem.put("invalidValue", String.valueOf(error.getInvalidValue()));
          jsonItem.put("message", String.valueOf(error.getMessage()));
          arrayNode.add(jsonItem);
        }
        jsonReturnValue.set("errors", arrayNode);
        returnValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonReturnValue).toString();
      } catch (Throwable e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
      }
    }
    return returnValue;
  }
}
