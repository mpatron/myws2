//package org.jobjects.myws2.rest.tools;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.annotation.Priority;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.ext.Provider;
//import org.apache.commons.io.input.TeeInputStream;
//
//@Provider
//@Priority(Priorities.ENTITY_CODER)
//public class CustomRequestWrapperFilter implements ContainerRequestFilter {
//  public CustomRequestWrapperFilter() {
//    LOGGER.info("@Provider : CustomRequestWrapperFilter loading.....");
//  }
//
//  private transient Logger LOGGER = Logger.getLogger(getClass().getName());
//  public final static String ENTITY_STREAM_COPY = "ENTITY_STREAM_COPY";
//
//  @Override
//  public void filter(ContainerRequestContext requestContext) throws IOException {
//    try (ByteArrayOutputStream proxyOutputStream = new ByteArrayOutputStream()) {
//      requestContext.setEntityStream(new TeeInputStream(requestContext.getEntityStream(), proxyOutputStream));
//      requestContext.setProperty(ENTITY_STREAM_COPY, proxyOutputStream.toString("UTF-8"));
//    } catch (Exception e) {
//      LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
//      throw new IOException(e);
//    }
//  }
//}
