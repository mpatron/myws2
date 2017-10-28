package org.jobjects.myws2.rest.tools;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 * @author MPT
 *
 */
@Tracked
@Provider
public class TrafficLogger implements ContainerRequestFilter, ContainerResponseFilter {
  private final transient Logger LOGGER = Logger.getLogger(getClass().getName());

  public TrafficLogger() {
    LOGGER.info("TrafficLogger loading.....");
  }

  /*
   * ContainerRequestFilter
   * 
   * @see
   * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container
   * .ContainerRequestContext)
   */
  public void filter(ContainerRequestContext requestContext) throws IOException {
    try {
      JsonObjectBuilder json = ContainerRequestContextToJSON(requestContext);
      String optionKey = "IN-" + requestContext.getUriInfo().getPath();
      String optionValue = json.build().toString();

      LOGGER.log(Level.INFO, "optionKey=" + optionKey + " optionValue=" + optionValue);

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "TrafficLogger IN WS Exception ", e);
    }
  }

  /*
   * ContainerResponseFilter
   * 
   * @see
   * javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container
   * .ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
   */
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    try {
      JsonObjectBuilder json = ContainerRequestContextToJSON(requestContext);
      json.add("BodyResponse", Objects.toString(responseContext.getEntity()));

      String optionKey = "OUT-" + requestContext.getUriInfo().getPath();
      String optionValue = json.build().toString();

      LOGGER.log(Level.INFO, "optionKey=" + optionKey + " optionValue=" + optionValue);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "TrafficLogger OUT WS Exception ", e);
    }
  }

  private JsonObjectBuilder ContainerRequestContextToJSON(ContainerRequestContext requestContext) {
    JsonObjectBuilder json = Json.createObjectBuilder();
    try {
      String userPrincipal = null;
      SecurityContext sc = requestContext.getSecurityContext();
      if (sc != null && sc.getUserPrincipal() != null) {
        userPrincipal = sc.getUserPrincipal().getName();
        json.add("UserPrincipal", userPrincipal);
      }

      json.add("Method", requestContext.getMethod());

      UriInfo uriInfo = requestContext.getUriInfo();
      if (uriInfo != null) {
        json.add("UriInfo", "" + uriInfo.getRequestUri().toString());
      }

      /**
       * Astuce le stream a déjà été lu par le CustomRequestWrapperFilter et à
       * mis en mémoire en chaine entityStream. Sans le bug, le code normal
       * aurait été : json.add("BodyRequest",
       * InputStreamToString(requestContext.getEntityStream()));
       */
      json.add("BodyRequest", (String) requestContext.getProperty(CustomRequestWrapperFilter.ENTITY_STREAM_COPY));

      MultivaluedMap<String, String> headers = requestContext.getHeaders();
      JsonObjectBuilder jsonHeaders = Json.createObjectBuilder();
      for (Entry<String, List<String>> entry : headers.entrySet()) {
        if(entry.getValue()!=null) {
          jsonHeaders.add(entry.getKey(), Arrays.toString(entry.getValue().toArray()));
        }
        
      }
      json.add("Headers", jsonHeaders);

      Collection<String> propertyNames = requestContext.getPropertyNames();
      JsonObjectBuilder jsonPropertyNames = Json.createObjectBuilder();
      for (String propertyName : propertyNames) {
        Object value = requestContext.getProperty(propertyName);
        jsonPropertyNames.add(propertyName, value == null ? "<null>" : Objects.toString(value));
      }
      json.add("Properties", jsonPropertyNames);

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "TrafficLogger OUT WS Exception ", e);
    }
    return json;
  }

//  private String InputStreamToString(InputStream is) {
//    StringBuilder returnValue = new StringBuilder();
//    try {
//
//      if (is.markSupported()) {
//        is.mark(1024 * 1024);
//      }
//      BufferedReader br = new BufferedReader(new InputStreamReader(is));
//      String line;
//      while ((line = br.readLine()) != null) {
//        returnValue.append(line);
//      }
//      if (is.markSupported()) {
//        is.reset();
//      }
//    } catch (Exception e) {
//      LOGGER.log(Level.SEVERE, "Internal error.", e);
//    }
//    return returnValue.toString();
//  }

}
