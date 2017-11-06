package gov.ca.cwds.template.web.rest.utils;

/**
 * @author CWDS TPT-2 Team
 */
public class TestModeUtils {

  public static final String CALS_API_URL = "api.url";

  private TestModeUtils() {
  }

  public static boolean isIntegrationTestsMode() {
    return System.getProperty(CALS_API_URL) != null;
  }
}
