package org.jobjects.myws2.orm.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.tools.AbstractUUIDBaseEntity;

/**
 * Table des utilisateurs.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@Entity
@Table(
    name = "USERS",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_names", columnList = "firstName,lastName", unique = false) })
@NamedQueries({
    @NamedQuery(name = User.FIND_BY_FIRSTNAME, query = "select t from User t where t.firstName = :firstName"),
    @NamedQuery(name = User.FIND_BY_EMAIL, query = "select t from User t where t.email = ?1") })
public class User extends AbstractUUIDBaseEntity implements Serializable {
  public final static String FIND_BY_FIRSTNAME = "User.findByFirstName";
  public final static String FIND_BY_EMAIL = "User.findByEmail";
  /**
   * 
   */
  private static final long serialVersionUID = -598324264166203579L;

  public User() {
  }

  /**
   * Prénom de l'utilisateur.
   */
  @Size(min = 2, max = 20, message = "La longueur du prénom est comprise entre 2 et 20 caractères.")
  @NotNull
  private String firstName;
  /**
   * Nom de l'utilisateur.
   */
  @Size(max = 20, message = "La longueur du nom est inférieur à 20 caractères.")
  private String lastName;
  /**
   * Email de l'utilisateur.
   */
  @Size(max = 320)
  @NotNull
  @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")
  private String email;
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
  protected List<Address> address = new ArrayList<>();

  /**
   * @return the firstName
   */
  public final String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName
   *          the firstName to set
   */
  public final void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public final String getLastName() {
    return lastName;
  }

  /**
   * @param lastName
   *          the lastName to set
   */
  public final void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the email
   */
  public final String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public final void setEmail(final String email) {
    this.email = email;
  }

  /**
   * @return the address
   */
  public final List<Address> getAddress() {
    return address;
  }

  /**
   * @param address
   *          the address to set
   */
  public final void setAddress(final List<Address> address) {
    this.address = address;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    // StringBuilder builder = new StringBuilder();
    // builder.append("User [firstName=");
    // builder.append(firstName);
    // builder.append(", lastName=");
    // builder.append(lastName);
    // builder.append(", email=");
    // builder.append(email);
    // builder.append("]");
    // return builder.toString();
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
