package org.jobjects.myws2.tools;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jobjects.myws2.rest.AddressEndpoint;
import org.jobjects.myws2.rest.AddressReader;
import org.jobjects.myws2.rest.AddressWriter;
import org.jobjects.myws2.rest.HelloWorldEndpoint;
import org.jobjects.myws2.rest.MyBeanReader;
import org.jobjects.myws2.rest.MyBeanWriter;
import org.jobjects.myws2.rest.UserEndpoint;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Dans JEE7, il ne suffit pas de d'utiliser les annotations REST pour le faire
 * fonctionner. Glassfish est fourni avec Jersey, par la dependency est donc en
 * provided. Avec la technique "extends Application " pas la peine de faire
 * la déclartion dans le web.xml.
 * https://jersey.java.net/nonav/documentation/2.0/index.html
 * https://jersey.java.net/nonav/documentation/2.0/deployment.html
 * 
 * @author Mickaël PATRON 2014
 *
 */
/**
 * https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#swaggerdefinition
 * @author Mickael
 *
 */
@ApplicationPath("/api")
public class RestApplicationConfiguration extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(CORSFilter.class);
    // resources.add(CustomRequestWrapperFilter.class);
    resources.add(TrafficLogger.class);
    resources.add(HelloWorldEndpoint.class);
    resources.add(UserEndpoint.class);
    resources.add(MyBeanWriter.class);
    resources.add(MyBeanReader.class);
    resources.add(AddressEndpoint.class);
    resources.add(AddressWriter.class);
    resources.add(AddressReader.class);
    return resources;
  }

  @Override
  public Set<Object> getSingletons() {
    final Set<Object> instances = new HashSet<Object>();
    instances.add(new JacksonJsonProvider());
    return instances;
  }
}
