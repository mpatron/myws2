package org.jobjects.myws2.rest;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import org.jobjects.myws2.orm.address.Address;
import org.jobjects.myws2.orm.address.AddressFacade;
import org.jobjects.myws2.tools.AbstractEndPoint;
import org.jobjects.myws2.tools.Tracked;

@Tracked
@Path("/address")
public class AddressEndpoint extends AbstractEndPoint<Address> {
  @EJB
  protected AddressFacade facade;

  public AddressEndpoint() {
    super(Address.class);
  }

  @PostConstruct
  public void postConstruct() {
    setFacade(facade);
  }
}
