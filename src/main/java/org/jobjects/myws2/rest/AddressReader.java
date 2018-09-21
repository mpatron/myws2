package org.jobjects.myws2.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import org.jobjects.myws2.orm.address.Address;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe permettant la lecture d'un JSON au format texte pour le transformer en
 * Bean.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class AddressReader implements MessageBodyReader<Address> {
  /**
   * instance du log avec le type JVM pour la portabilité.
   */
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  /*
   * (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType)
   */
  @Override
  public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
    return Address.class.isAssignableFrom(type);
  }

  /*
   * (non-Javadoc)
   * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class,
   * java.lang.reflect.Type, java.lang.annotation.Annotation[],
   * javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
   * java.io.InputStream)
   */
  @Override
  public Address readFrom(Class<Address> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm,
      InputStream in) throws IOException, WebApplicationException {
    Address instance = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      instance = mapper.readValue(in, Address.class);
      LOGGER.info("in -> Address as json : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance));
    } catch (Exception e) {
      throw new WebApplicationException(e);
    }
    // try {
    // instance = new Address();
    // InputStreamReader reader = new InputStreamReader(in, "UTF-8");
    // JsonReader jsonReader = Json.createReader(reader);
    // JsonObject jsonObject = jsonReader.readObject();
    // LOGGER.warning("AddressReader.jsonObject="+jsonObject);
    //
    // String uuid = jsonObject.getString("id", null);
    // instance.setId(StringUtils.isNotEmpty(uuid)?UUID.fromString(uuid):null);
    // instance.setType(AddressEnum.valueOf(jsonObject.getString("type",
    // null)));
    // instance.setStreet(jsonObject.getString("street", null));
    // instance.setPostcode(jsonObject.getString("postcode", null));
    // instance.setCity(jsonObject.getString("city", null));
    // instance.setStateCode(jsonObject.getString("stateCode", null));
    // instance.setState(jsonObject.getString("state", null));
    //
    // JsonObject userJson = jsonObject.getJsonObject("user");
    // User user = new User();
    // String userUuid = userJson.getString("id", null);
    // user.setId(StringUtils.isNotEmpty(userUuid)?UUID.fromString(userUuid):null);
    // user.setFirstName(userJson.getString("firstName", null));
    // user.setLastName(userJson.getString("lastName", null));
    // user.setEmail(userJson.getString("email", null));
    // user.setAddress(new ArrayList<Address>());
    //
    // instance.setUser(user);
    //
    // } catch (Exception e) {
    // throw new WebApplicationException(e);
    // }
    return instance;
  }
}
