package org.jobjects.myws2.tools;

import java.util.List;

/**
 * Classe générique pour manipuler les entity.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 * @param <T>
 *          En paramètre le nom de la class Entity
 */
public interface Facade<T> {
  /**
   * Création dans la table, INSERT.
   * @param entity
   *          Paramètre
   */
  void create(T entity);

  /**
   * Sauvegarde dans la table, UPDATE.
   * @param entity
   *          Paramètre
   * @return La ligne sauvegardée
   */
  T save(T entity);

  /**
   * Supprime de la table, DELETE.
   * @param entity
   *          Paramètre
   */
  void remove(T entity);

  /**
   * Recherche par la clef primaire de la table, SELECT.
   * @param id
   *          Paramètre
   * @return La ligne sauvegardée
   */
  T find(Object id);

  /**
   * Recherche de toutes le lignes de la table, SELECT.
   * @return toutes les lignes sauvegardées
   */
  List<T> findAll();

  /**
   * Recherche de toutes le lignes de la table, SELECT.
   * @param rangeMin
   *          Borne inférieure
   * @param rangeMax
   *          Borne supérieure
   * @return les lignes trouvées.
   */
  List<T> findRange(int rangeMin, int rangeMax);

  /**
   * Recherche le nombre de le lignes de la table, SELECT.
   * @return le nombre de la table.
   */
  long count();

  /**
   * @param name
   *          Nom de la réquete paramétré.
   * @param params
   *          Liste des paramètres
   * @return les lignes trouvées.
   */
  List<T> findByNamedQuery(String name, Object... params);
}
