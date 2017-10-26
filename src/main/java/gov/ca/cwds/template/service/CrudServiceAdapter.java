package gov.ca.cwds.template.service;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.services.CrudsService;
import java.io.Serializable;

/**
 * @author CWDS TPT-2 Team
 */
public class CrudServiceAdapter implements CrudsService {

  @Override
  public Response find(Serializable params) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Response delete(Serializable params) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Response create(Request request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Response update(Serializable params, Request request) {
    throw new UnsupportedOperationException();
  }
}
