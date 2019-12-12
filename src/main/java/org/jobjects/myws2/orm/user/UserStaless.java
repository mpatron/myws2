package org.jobjects.myws2.orm.user;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.jobjects.myws2.tools.AbstractFacade;
import org.jobjects.myws2.tools.AppConstants;

/**
 * EJB de la gestion des User.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Stateless
@Local({ UserFacade.class })
public class UserStaless extends AbstractFacade<User> implements UserFacade {
  /**
   * Constructeur pour passer en paramètre le nom de la classe.
   */
  public UserStaless() {
    super(User.class);
  }

  /**
   * EntityManager de serveur J2EE.
   */
  @PersistenceContext(unitName = AppConstants.PERSISTENCE_UNIT_NAME)
  private EntityManager entityManager;

  /**
   * @param entityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.tools.AbstractFacade#getEntityManager()
   */
  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  /*
   * (non-Javadoc)
   * @see
   * org.jobjects.myws.orm.user.UserFacade#findByFirstName(java.lang.String)
   */
  public List<User> findByFirstName(final String firstName) {
    TypedQuery<User> query = getEntityManager().createNamedQuery(User.FIND_BY_FIRSTNAME, User.class);
    return query.setParameter("firstName", firstName).getResultList();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.user.UserFacade#findByEmail(java.lang.String)
   */
  public User findByEmail(final String email) {
    User returnValue = null;
    List<User> users = findByNamedQuery(User.FIND_BY_EMAIL, email);
    for (User user : users) {
      returnValue = user;
      break;
    }
    return returnValue;
  }
}
