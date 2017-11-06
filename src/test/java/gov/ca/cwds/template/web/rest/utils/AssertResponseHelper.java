package gov.ca.cwds.template.web.rest.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import java.io.IOException;
import java.util.Map;

/**
 * @author CWDS TPT-2 Team
 */

public final class AssertResponseHelper {

  private AssertResponseHelper() {
  }

  @SuppressWarnings("unchecked")
  public static void assertEqualsResponse(String fixture, String actualString) throws IOException {
    ObjectMapper objectMapper = Jackson.newObjectMapper();
    Map<String, String> expectedMap = (Map<String, String>) objectMapper
        .readValue(fixture, Map.class);
    Map<String, String> actualMap = (Map<String, String>) objectMapper
        .readValue(actualString, Map.class);
    assertThat(actualMap).isEqualTo(expectedMap);
  }

}
