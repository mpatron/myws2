package org.jobjects.myws2.tools;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Tracked
public class AbstractEndPoint<T extends AbstractUUIDBaseEntity & Serializable> {
  /**
   * Instance du logger.
   */
  private Logger LOGGER = Logger.getLogger(getClass().getName());
  /**
   * Classe de l'entity.
   */
  private Class<T> entityClass;
  // Facade<T> facade;
  Facade<T> facade;

  public void setFacade(Facade<T> facade) {
    this.facade = facade;
  }

  /**
   * Constructeur de la classe.
   * @param entityClasse
   *          Classe de l'entity.
   */
  public AbstractEndPoint(final Class<T> entityClasse) {
    this.entityClass = entityClasse;
  }

  private boolean isValid(T entity) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<T>> errors = factory.getValidator().validate(entity);
    for (ConstraintViolation<T> error : errors) {
      LOGGER.severe(ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE) + " " + error.getMessage()
          + " due to " + error.getInvalidValue());
    }
    return (errors.size() == 0);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(T entity) {
    if (!isValid(entity)) {
      return Response.status(Response.Status.BAD_REQUEST)
          // 400
          .entity(entity).header("Location", "http://localhost:8880/api/users").build();
    }
    entity.setId(UUID.randomUUID());
    T returnValue = facade.save(entity);
    return Response.status(Response.Status.CREATED).entity(returnValue)
        .header("Location", "http://localhost:8880/api/users/" + returnValue.getId()).build();
  }

  @GET
  @Path("{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("id") UUID id) {
    LOGGER.info("public Response read(@PathParam(\"id\") UUID " + id + ")");
    T returnValue = facade.find(id);
    return Response.status(200).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header").allow("OPTIONS").build();
  }

  /**
   * Returns all resources (T) from the database
   *
   * @return
   * @throws WebApplicationException
   */
//  @GET
//  @Produces(MediaType.APPLICATION_JSON)
//  public List<T> readall(@DefaultValue("0") @QueryParam("rangeMin") Integer rangeMin,
//      @DefaultValue("" + Integer.MAX_VALUE) @QueryParam("rangeMax") Integer rangeMax) throws WebApplicationException {
//    LOGGER.info("public List<T> read(@QueryParam(\"rangeMin\") Integer \"" + rangeMin + "\", @QueryParam(\"rangeMax\") Integer \""
//        + rangeMax + "\")");
//    List<T> returnValue = facade.findRange(rangeMin, rangeMax);
//    return returnValue;
//  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") UUID id, T entity) throws WebApplicationException {
    T returnValue = facade.find(id);
    if (isValid(entity)) {
      if (returnValue == null) {
        /* Creation */
        T entitySaved = facade.create(entity);
        return Response.status(Response.Status.CREATED)
            // 201
            .entity(entitySaved).header("Location", "http://localhost:8880/api/users/" + entitySaved.getId()).build();
      } else {
        /* Mise à jour */
        T entitySaved = facade.save(entity);
        return Response.status(Response.Status.OK)
            // 200
            .entity(entitySaved).header("Location", "http://localhost:8880/api/users/" + entitySaved.getId()).build();
      }
    } else {
      if (returnValue == null) {
        return Response.status(Response.Status.BAD_REQUEST)
            // 400
            .entity("il faut mettre les erreurs 123456789").header("Location", "http://localhost:8880/api/users/" + entity.getId()).build();
      } else {
        return Response.status(Response.Status.CONFLICT)
            // 406
            .entity("il faut mettre les erreurs 123456789").header("Location", "http://localhost:8880/api/users/" + entity.getId()).build();
      }
    }
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") UUID id) throws WebApplicationException {
    T returnValue = facade.find(id);
    if (returnValue == null) {
      LOGGER.info(entityClass.getSimpleName() + " => NOT_FOUND");
      return Response.status(Response.Status.NO_CONTENT)// 204
          .build();
    } else {
      LOGGER.info(entityClass.getSimpleName() + " => NO_CONTENT" + ToStringBuilder.reflectionToString(returnValue));
      facade.remove(returnValue);
      return Response.status(Response.Status.OK)// 200
          .entity(returnValue).build();
    }
  }

  /**
   * j2ee rest annotation generic
   * https://stackoverflow.com/questions/15408337/using-java-generics-template-type-in-restful-response-object-via-genericentityl
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    
//    Query query = facade.getEntityManager().createNamedQuery(entityClass.getSimpleName() + ".findAll");
//    List<T> list = query.getResultList();
    List<T> list = facade.findAll();
    GenericEntity<List<T>> restEntity = new GenericEntity<List<T>>(list){};
    return Response.ok(restEntity).build();
  }
}
