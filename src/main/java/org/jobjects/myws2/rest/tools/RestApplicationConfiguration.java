package org.jobjects.myws2.rest.tools;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jobjects.myws2.rest.HelloWorldEndpoint;
import org.jobjects.myws2.rest.MyBeanReader;
import org.jobjects.myws2.rest.MyBeanWriter;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

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
@SwaggerDefinition(
    info = @Info(
        description = "Gets the hello world",
        version = "v1.3",
        title = "The hello API",
        termsOfService = "https://help.github.com/articles/github-terms-of-service/#a-definitions",
        contact = @Contact(name = "Alice Savoie", email = "alice@jobjects.org", url = "https://github.com/mpatron/myws2"),
        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0")),
    consumes = { "application/json", "application/xml" },
    produces = { "application/json", "application/xml" },
    schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS },
    tags = { @Tag(name = "Private", description = "Tag used to denote operations as private") },
    externalDocs = @ExternalDocs(value = "Readme a lot", url = "https://github.com/mpatron/myws2/blob/master/readme.md"))
@ApplicationPath("/api")
public class RestApplicationConfiguration extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(CORSFilter.class);
    resources.add(CustomRequestWrapperFilter.class);
    resources.add(TrafficLogger.class);
    resources.add(HelloWorldEndpoint.class);
    resources.add(MyBeanWriter.class);
    resources.add(MyBeanReader.class);
    /**
     * Activation de la génération de swagger.json
     * http://localhost:8880/api/swagger.json
     */
    resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
    resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    return resources;
  }

  @Override
  public Set<Object> getSingletons() {
    final Set<Object> instances = new HashSet<Object>();
    instances.add(new JacksonJsonProvider());
    return instances;
  }
}
