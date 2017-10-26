package gov.ca.cwds.template.persistence.hibernate;

/**
 * @author CWDS TPT-2 Team
 */
public class ConvertingException extends RuntimeException {

  public ConvertingException(String s, Exception ex) {
    super(s, ex);
  }
}
