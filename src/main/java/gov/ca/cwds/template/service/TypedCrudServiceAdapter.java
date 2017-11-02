package gov.ca.cwds.template.service;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.services.TypedCrudsService;
import java.io.Serializable;

/**
 * @author CWDS TPT-3 Team
 */
public class TypedCrudServiceAdapter<P extends Serializable, Q extends Request, S extends Response>
    implements TypedCrudsService<P, Q, S> {

  @Override
  public S find(P params) {
    throw new UnsupportedOperationException();
  }

  @Override
  public S delete(P params) {
    throw new UnsupportedOperationException();
  }

  @Override
  public S create(Q request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public S update(P params, Q request) {
    throw new UnsupportedOperationException();
  }
}
