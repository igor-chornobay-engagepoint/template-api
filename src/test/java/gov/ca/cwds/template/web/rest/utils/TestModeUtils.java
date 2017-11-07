package gov.ca.cwds.template.web.rest.utils;

/**
 * @author CWDS TPT-2 Team
 */
public class TestModeUtils {

  public static final String TEMPLATE_API_URL = "api.url";

  private TestModeUtils() {
  }

  public static boolean isIntegrationTestsMode() {
    return System.getProperty(TEMPLATE_API_URL) != null;
  }
}
