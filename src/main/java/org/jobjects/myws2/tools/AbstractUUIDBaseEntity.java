package org.jobjects.myws2.tools;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Mickaël Patron
 * @version 2016-05-08
 */
@MappedSuperclass
public abstract class AbstractUUIDBaseEntity implements Serializable {
  /**
   * La classe est Serializable.
   */
  protected static final long serialVersionUID = 1L;
  /**
   * Champs identifiant.
   */
  @Id
  @Column(name = "UUID_ID", nullable = false, length = 36)
  private UUID id;

  /**
   * @return the id
   */
  public final UUID getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public final void setId(final UUID id) {
    this.id = id;
  }

  /**
   * Constructeur de clef UUID.
   */
  public AbstractUUIDBaseEntity() {
    this.id = UUID.randomUUID();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public final int hashCode() {
    return id.hashCode();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof AbstractUUIDBaseEntity)) {
      return false;
    }
    AbstractUUIDBaseEntity other = (AbstractUUIDBaseEntity) obj;
    return getId().equals(other.getId());
  }
}
