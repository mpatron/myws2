package org.jobjects.myws2.orm.address;

import java.util.List;

import org.jobjects.myws2.orm.tools.Facade;
import org.jobjects.myws2.orm.user.User;

/**
 * Classe de gestion des adresses.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
public interface AddressFacade extends Facade<Address> {
  /**
   * @param user
   *          Paramétre
   * @return La liste des adresses.
   */
  List<Address> findByFirstName(final User user);
}
