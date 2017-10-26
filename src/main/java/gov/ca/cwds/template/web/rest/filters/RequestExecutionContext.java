package gov.ca.cwds.template.web.rest.filters;

import java.util.Date;

/**
 * Request execution context.
 *
 * @author CWDS API Team
 */
public interface RequestExecutionContext {

  /**
   * Get instance of RequestExecutionContext from registry
   *
   * @return RequestExecutionContext instance
   */
  static RequestExecutionContext instance() {
    return RequestExecutionContextRegistry.get();
  }

  /**
   * Store request execution parameter
   *
   * @param parameter Parameter
   * @param value Parameter value
   */
  void put(Parameter parameter, Object value);

  /**
   * Retrieve request execution parameter
   *
   * @param parameter Parameter
   * @return The parameter value
   */
  Object get(Parameter parameter);

  /**
   * Get user id if stored.
   *
   * @return The user id
   */
  String getUserId();

  /**
   * Get request start time if stored
   *
   * @return The request start time
   */
  Date getRequestStartTime();

  /**
   * Known request execution parameters
   */
  enum Parameter {
    USER_IDENTITY, REQUEST_START_TIME, SEQUENCE_EXTERNAL_TABLE
  }
}
