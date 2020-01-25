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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jobjects.myws2.orm.user.User;
import org.jobjects.myws2.orm.validator.ValidStringCountryISO2;
import org.jobjects.myws2.tools.AbstractUUIDBaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
  @ValidStringCountryISO2
  @Size(max = 2, message = "La longueur de stateCode est inférieur à 2 caractères.")
  private String stateCode;
  @Size(max = 80, message = "La longueur de state est inférieur à 80 caractères.")
  private String state;
  @Size(max = 20, message = "La longueur de postcode est inférieur à 20 caractères.")
  private String postcode;
  /* Non utilisable pour le JSON, utilisé pour le JPA */
  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "USER_UUID_ID",
      nullable = false) /* UUID_ID vient de user */
  @NotNull(message = "Pas d'adresse sans utilisateur.")
  private User user;

  /**
   * @return the type
   */
  public AddressEnum getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(final AddressEnum type) {
    this.type = type;
  }

  /**
   * @return the street
   */
  public String getStreet() {
    return street;
  }

  /**
   * @param street
   *          the street to set
   */
  public void setStreet(final String street) {
    this.street = street;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city
   *          the city to set
   */
  public void setCity(final String city) {
    this.city = city;
  }

  /**
   * @return the stateCode
   */
  public String getStateCode() {
    return stateCode;
  }

  /**
   * @param stateCode
   *          the stateCode to set
   */
  public void setStateCode(final String stateCode) {
    this.stateCode = stateCode;
  }

  /**
   * @return the postcode
   */
  public String getPostcode() {
    return postcode;
  }

  /**
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @param state
   *          the state to set
   */
  public void setState(String state) {
    this.state = state;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false);
  }
}
