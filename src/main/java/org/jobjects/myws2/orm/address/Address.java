package org.jobjects.myws2.orm.address;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.tools.AbstractUUIDBaseEntity;

/**
 * Table des adresses.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Entity
@Table(name = "ADDRESS", indexes = { @Index(name = "idx_address_unique", columnList = "user_uuid_id,type", unique = true) })
@NamedQueries({
    @NamedQuery(name = Address.FIND_BY_USER, query = "select t from Address t where t.user = :user"),
    @NamedQuery(name = Address.FIND_BY_USER_TYPE, query = "select t from Address t where t.user = ?1 and t.type = ?2") })
public class Address extends AbstractUUIDBaseEntity implements Serializable {
  public static final String FIND_BY_USER = "Address.findByUser";
  public static final String FIND_BY_USER_TYPE = "Address.findByUserType";
  /**
   * Numero de série
   */
  private static final long serialVersionUID = -598324264166203579L;

  public Address() {
  }

  @Enumerated(EnumType.STRING)
  @Column(length = 4)
  private AddressEnum type;
  @Size(max = 80, message = "La longueur de street est inférieur à 80 caractères.")
  private String street;
  @Size(max = 40, message = "La longueur de city est inférieur à 40 caractères.")
  private String city;
  @Size(max = 2, message = "La longueur de state est inférieur à 2 caractères.")
  private String state;
  @Size(max = 20, message = "La longueur de postcode est inférieur à 20 caractères.")
  private String postcode;
  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "USER_UUID_ID",
      nullable = false) /* UUID_ID vient de user */
  private User user;

  /**
   * @return the type
   */
  public final AddressEnum getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public final void setType(final AddressEnum type) {
    this.type = type;
  }

  /**
   * @return the street
   */
  public final String getStreet() {
    return street;
  }

  /**
   * @param street
   *          the street to set
   */
  public final void setStreet(final String street) {
    this.street = street;
  }

  /**
   * @return the city
   */
  public final String getCity() {
    return city;
  }

  /**
   * @param city
   *          the city to set
   */
  public final void setCity(final String city) {
    this.city = city;
  }

  /**
   * @return the state
   */
  public final String getState() {
    return state;
  }

  /**
   * @param state
   *          the state to set
   */
  public void setState(final String state) {
    this.state = state;
  }

  /**
   * @return the postcode
   */
  public String getPostcode() {
    return postcode;
  }

  /**
   * @param postcode
   *          the postcode to set
   */
  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }
}
