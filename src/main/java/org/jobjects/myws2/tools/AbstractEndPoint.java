package org.jobjects.myws2.tools;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * https://restdb.io/docs/rest-api
 * GET  https://<dburl>.restdb.io/rest/<collection>/ID/<subcollection>/ID
 * 
 * @author Mickael
 *
 * @param <T>
 */
public class AbstractEndPoint<T extends AbstractUUIDBaseEntity & Serializable> { 
  /**
   * Instance du logger.
   */
  private Logger LOGGER = Logger.getLogger(getClass().getName());
  /**
   * Classe de l'entity.
   */
  private Class<T> entityClass;
  private Facade<T> facade;
  
  @Context
  UriInfo uriInfo;

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

  public boolean isValid(final T entity) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<T>> errors = factory.getValidator().validate(entity);
    for (ConstraintViolation<T> error : errors) {
      LOGGER.severe("AbstractEndPoint.isValid => "+ReflectionToStringBuilder.toString(error.getRootBean(), ToStringStyle.SHORT_PREFIX_STYLE) + " " + error.getMessage()
          + " due to " + error.getInvalidValue());
    }
    return (errors.size() == 0);
  }

  public String getValidationMessages(final T entity) {
    StringBuffer returnValue = new StringBuffer();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    LOGGER.info(ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
    Set<ConstraintViolation<T>> errors = factory.getValidator().validate(entity);
    if(errors.size() > 0) {
      returnValue.append("Invalide : "+ReflectionToStringBuilder.toString(entity, ToStringStyle.SHORT_PREFIX_STYLE));
      returnValue.append(System.lineSeparator());
      for (ConstraintViolation<T> error : errors) {
        returnValue.append("    - " + error.getPropertyPath() + "="  + error.getInvalidValue() + " : "+ error.getMessage());
        returnValue.append(System.lineSeparator());
      }
    }
    return returnValue.toString();
  }


  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(final T entity) {
    if (!isValid(entity)) {
      LOGGER.warning("AbstractEndPoint.create");
      return Response.status(Response.Status.BAD_REQUEST)
          // 400
          .entity(getValidationMessages(entity)).header("Location", uriInfo.getAbsolutePath() ).build();
    }
    entity.setId(UUID.randomUUID());
    T returnValue = facade.save(entity);
    return Response.status(Response.Status.CREATED).entity(returnValue)
        .header("Location", uriInfo.getAbsolutePath().toString() + returnValue.getId()).build();
  }

  @PUT
  @Path("{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") final UUID id, final T entity) throws WebApplicationException {
    T returnValue = facade.find(id);
    if (isValid(entity)) {
      if (returnValue == null) {
        /* Creation */
        T entitySaved = facade.create(entity);
        return Response.status(Response.Status.CREATED)
            // 201
            .entity(entitySaved).header("Location", uriInfo.getAbsolutePath().toString() + entitySaved.getId()).build();
      } else {
        /* Mise à jour */
        T entitySaved = facade.save(entity);
        return Response.status(Response.Status.OK)
            // 200
            .entity(entitySaved).header("Location", uriInfo.getAbsolutePath().toString() + entitySaved.getId()).build();
      }
    } else {
      if (returnValue == null) {
        return Response.status(Response.Status.BAD_REQUEST)
            // 400
            .entity(getValidationMessages(entity)).header("Location", uriInfo.getAbsolutePath().toString() + entity.getId()).build();
        
      } else {
        return Response.status(Response.Status.CONFLICT)
            // 406
            .entity(getValidationMessages(entity)).header("Location", uriInfo.getAbsolutePath().toString() + entity.getId()).build();
      }
    }
  }

  @DELETE
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") final UUID id) throws WebApplicationException {
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
  
  @GET
  @Path("{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("id") final UUID id) {
    LOGGER.info("public Response read(@PathParam(\"id\") UUID " + id + ")");
    T returnValue = facade.find(id);
    return Response.status(Response.Status.OK).entity(returnValue).header("Access-Control-Allow-Headers", "X-extra-header").allow("OPTIONS").build();
  }

  /**
   * Returns all resources (T) from the database
   *
   * @return
   * @throws WebApplicationException
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<T> readall(@DefaultValue("0") @QueryParam("rangeMin") final Integer rangeMin,
      @DefaultValue("" + Integer.MAX_VALUE) @QueryParam("rangeMax") final Integer rangeMax) throws WebApplicationException {
    LOGGER.info("public List<T> read(@QueryParam(\"rangeMin\") Integer \"" + rangeMin + "\", @QueryParam(\"rangeMax\") Integer \""
        + rangeMax + "\")");
    List<T> returnValue = facade.findRange(rangeMin, rangeMax);
    return returnValue;
  }

  /**
   * j2ee rest annotation generic
   * https://stackoverflow.com/questions/15408337/using-java-generics-template-type-in-restful-response-object-via-genericentityl
   */
//  Indentique à readall car même implémentation
//  @GET
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response findAll() {
//    List<T> list = facade.findAll();
//    GenericEntity<List<T>> restEntity = new GenericEntity<List<T>>(list){};
//    return Response.ok(restEntity).build();
//  }
  
  /**
   * search
   * page
   * sort
   * filter
   */
}