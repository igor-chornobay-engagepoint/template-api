package gov.ca.cwds.template.web.rest.utils;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author CWDS TPT-3 Team
 */

public final class AssertFixtureUtils {

  private AssertFixtureUtils() {
  }

  public static void assertResponseByFixture(Response response, String fixture)
      throws IOException, JSONException {
    String entity = IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
    assertResponseByFixture(entity, fixture);
  }

  public static void assertResponseByFixture(String entity, String fixture)
      throws IOException, JSONException {
    assertEquals(fixture, entity, JSONCompareMode.STRICT);
  }

  public static void assertResponseByFixturePath(Response response, String fixturePath)
      throws IOException, JSONException {
    assertResponseByFixture(response, fixture(fixturePath));
  }

  public static void assertResponseByFixtureTemplate(String entity, String fixtureTemplate,
      Map templateParams)
      throws IOException, JSONException {
    VelocityHelper velocityHelper = new VelocityHelper();
    velocityHelper.setParameters(templateParams);
    assertResponseByFixture(entity, velocityHelper.process(fixtureTemplate));
  }

}
