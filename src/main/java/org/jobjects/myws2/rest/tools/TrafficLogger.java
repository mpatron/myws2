package org.jobjects.myws2.rest.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jobjects.myws2.tools.Tracked;

/**
 * @author MPT
 *
 */
@Tracked
@Provider
public class TrafficLogger implements ContainerRequestFilter, ContainerResponseFilter {
  
  private enum StateEnum {
    REQUEST, RESPONSE
  }

  
  public TrafficLogger() {
    LOGGER.info("@Provider : TrafficLogger loading.....");
  }

  private final transient Logger LOGGER = Logger.getLogger(getClass().getName());

  /*
   * ContainerRequestFilter
   * @see
   * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container
   * .ContainerRequestContext)
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    try {
      JsonObjectBuilder json = containerRequestContextToJSON(requestContext, null, StateEnum.REQUEST);
      String optionKey = "IN-" + requestContext.getUriInfo().getPath();
      String optionValue = json.build().toString();
      LOGGER.log(Level.INFO, "optionKey=" + optionKey + " optionValue=" + optionValue);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "TrafficLogger IN WS Exception ", e);
      throw e;
    }
  }

  /*
   * ContainerResponseFilter
   * @see
   * javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container
   * .ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    try {
      JsonObjectBuilder json = containerRequestContextToJSON(requestContext, responseContext, StateEnum.RESPONSE);
      String optionKey = "OUT-" + requestContext.getUriInfo().getPath();
      String optionValue = json.build().toString();
      LOGGER.log(Level.INFO, "optionKey=" + optionKey + " optionValue=" + optionValue);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "TrafficLogger OUT WS Exception ", e);
      throw e;
    }
  }

  private JsonObjectBuilder containerRequestContextToJSON(ContainerRequestContext requestContext, ContainerResponseContext responseContext, StateEnum state) {
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
      
      switch (state) {
      case REQUEST:
        if (requestContext.hasEntity()) {
          String bodyValue = null;
          bodyValue = IOUtils.toString(requestContext.getEntityStream(), StandardCharsets.UTF_8);
          requestContext.setEntityStream(IOUtils.toInputStream(bodyValue, StandardCharsets.UTF_8));
          json.add("BodyRequest", StringUtils.isBlank(bodyValue) ? "<empty>" : bodyValue);        
        }
        break;
      case RESPONSE:
        if (responseContext.hasEntity()) {
          String bodyValue = null;
          bodyValue = Objects.toString(responseContext.getEntity());
          json.add("BodyResponse", StringUtils.isBlank(bodyValue) ? "<empty>" : bodyValue);        
        }
        break;
      default:
        break;
      }
      
      
      MultivaluedMap<String, String> headers = requestContext.getHeaders();
      JsonObjectBuilder jsonHeaders = Json.createObjectBuilder();
      for (Entry<String, List<String>> entry : headers.entrySet()) {
        if (entry.getValue() != null) {
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
}
