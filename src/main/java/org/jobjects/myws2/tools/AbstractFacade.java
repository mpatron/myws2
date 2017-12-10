package org.jobjects.myws2.tools;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Mickaël Patron
 * @version 2016-05-08
 * @param <T>
 *          classe abstraite ORM générique.
 */
public abstract class AbstractFacade<T extends AbstractUUIDBaseEntity & Serializable> implements Facade<T> {
  /**
   * Instance du logger.
   */
  private Logger logger = Logger.getLogger(getClass().getName());
  /**
   * Type de transaction pour la portabilité J2EE ou J2SE.
   */
  private PersistenceContextType transactionLocal;
  /**
   * Classe de l'entity.
   */
  private Class<T> entityClass;

  /**
   * Constructeur de la classe.
   * @param entityClasse
   *          Classe de l'entity.
   */
  public AbstractFacade(final Class<T> entityClasse) {
    this.entityClass = entityClasse;
    try {
      getEntityManager().getTransaction();
      transactionLocal = PersistenceContextType.EXTENDED;
    } catch (Throwable t) {
      transactionLocal = PersistenceContextType.TRANSACTION;
    }
  }

  /**
   * @return l'EntityManager.
   */
  public abstract EntityManager getEntityManager();

  /**
   * (non-Javadoc)
   * @see Facade#create(java.lang.Object)
   */
  public void create(final T entity) {
    EntityTransaction trx = null;
    if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
      trx = getEntityManager().getTransaction();
      trx.begin();
    }
    try {
      getEntityManager().persist(entity);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.commit();
      }
    } catch (Throwable t) {
      logger.log(Level.SEVERE, "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
  }

  /**
   * (non-Javadoc)
   * @see Facade#save(java.lang.Object)
   */
  public T save(final T entity) {
    T returnValue = null;
    EntityTransaction trx = null;
    if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
      trx = getEntityManager().getTransaction();
      trx.begin();
    }
    try {
      returnValue = getEntityManager().merge(entity);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.commit();
      }
    } catch (Throwable t) {
      logger.log(Level.SEVERE, "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
    return returnValue;
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#remove(java.lang.Object)
   */
  public void remove(final T entity) {
    EntityTransaction trx = null;
    if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
      trx = getEntityManager().getTransaction();
      trx.begin();
    }
    try {
      getEntityManager().remove(getEntityManager().merge(entity));
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.commit();
      }
    } catch (Throwable t) {
      logger.log(Level.SEVERE, "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#find(java.lang.Object)
   */
  public T find(final Object id) {
    return getEntityManager().find(entityClass, id);
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#findAll()
   */
  public List<T> findAll() {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#findRange(int, int)
   */
  public List<T> findRange(final int rangeMin, final int rangeMax) {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
    cq.select(cq.from(entityClass));
    TypedQuery<T> q = getEntityManager().createQuery(cq);
    q.setMaxResults(rangeMax - rangeMin);
    q.setFirstResult(rangeMin);
    return q.getResultList();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#count()
   */
  public long count() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(entityClass)));
    return getEntityManager().createQuery(cq).getSingleResult();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.Facade#findByNamedQuery(java.lang.String,
   * java.lang.Object[])
   */
  public List<T> findByNamedQuery(final String name, final Object... params) {
    TypedQuery<T> query = getEntityManager().createNamedQuery(name, entityClass);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    return query.getResultList();
  }
}
