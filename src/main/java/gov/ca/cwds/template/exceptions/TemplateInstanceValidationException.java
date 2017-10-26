package gov.ca.cwds.template.exceptions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author CWDS TPT-2 Team
 */
@SuppressWarnings("squid:S1948")
@SuppressFBWarnings("SE_BAD_FIELD")
public class TemplateInstanceValidationException extends RuntimeException {

  private static final long serialVersionUID = -6616692496235125080L;

  private final String report;

  public TemplateInstanceValidationException(String report) {
    super(String.valueOf(report));
    this.report = report;
  }

  public String getReport() {
    return report;
  }

}
