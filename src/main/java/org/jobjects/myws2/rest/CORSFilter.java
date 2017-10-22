package org.jobjects.myws2.rest;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Swagger génère cette erreur :
 * Can't read from server. It may not have the appropriate access-control-origin
 * settings.
 * Si les headers suivants ne sont pas positionnés.
 * http://www.developerscrappad.com/1781/java/java-ee/rest-jax-rs/java-ee-7-jax-rs-2-0-cors-on-rest-how-to-make-rest-apis-accessible-from-a-different-domain/
 * @author Mickael
 *
 */
@Provider
@PreMatching
public class CORSFilter implements ContainerResponseFilter {
  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres)
      throws IOException {
    cres.getHeaders().add("Access-Control-Allow-Origin", "*");
    cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
    cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    cres.getHeaders().add("Access-Control-Max-Age", "1209600");/* 14 jours */
  }
}
