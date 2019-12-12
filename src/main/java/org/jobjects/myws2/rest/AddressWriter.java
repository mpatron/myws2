package org.jobjects.myws2.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.jobjects.myws2.orm.address.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe permettant l'écriture d'un Bean en le transformant en un JSON au
 * format texte.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AddressWriter implements MessageBodyWriter<Address> {
  /**
   * instance du log avec le type JVM pour la portabilité.
   */
  private transient Logger logger = Logger.getLogger(getClass().getName());

  /*
   * (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    logger.info("TypeName=" + genericType.getTypeName() + " CanonicalName=" + type.getCanonicalName() + " Address.class.isAssignableFrom="
        + Address.class.isAssignableFrom(type));
    return Address.class.isAssignableFrom(type);
  }

  /*
   * (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public long getSize(Address t, Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    // http://docs.oracle.com/javaee/7/api/javax/ws/rs/ext/MessageBodyWriter.html#getSize-T-java.lang.Class-java.lang.reflect.Type-java.lang.annotation.Annotation:A-javax.ws.rs.core.MediaType-
    // As of JAX-RS 2.0, the method has been deprecated and the
    // value returned by the method is ignored by a JAX-RS runtime.
    // All MessageBodyWriter implementations are advised to return -1 from
    // the method.
    return -1;
  }

  /*
   * (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object,
   * java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.OutputStream)
   */
  @Override
  public void writeTo(Address instance, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm,
      OutputStream out) throws IOException, WebApplicationException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.writeValue(out, instance);
    logger.finest("out -> instance as json : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance));
    // JsonGenerator gen = Json.createGenerator(out);
    // gen.writeStartObject();
    // if (null != instance.getMessage())
    // gen.write("message", instance.getMessage());
    // gen.writeEnd();
    // gen.flush();
  }
}
