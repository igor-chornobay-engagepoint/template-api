package gov.ca.cwds.template.web;

import gov.ca.cwds.template.TemplateApiApplication;
import gov.ca.cwds.template.TemplateApiConfiguration;
import gov.ca.cwds.template.web.rest.RestClientTestRule;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.client.Client;
import org.glassfish.jersey.client.JerseyClient;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;

/**
 * @author CWDS TPT-3 Team
 */
public abstract class BaseApiIntegrationTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String configFile = "config/test-template-api.yml";

  @ClassRule
  public static final DropwizardAppRule<TemplateApiConfiguration> appRule =
      new DropwizardAppRule<TemplateApiConfiguration>(
          TemplateApiApplication.class, ResourceHelpers.resourceFilePath(configFile)) {

        @Override
        public Client client() {
          Client client = super.client();
          if (((JerseyClient) client).isClosed()) {
            client = clientBuilder().build();
          }
          return client;
        }
      };

  @Rule
  public RestClientTestRule clientTestRule = new RestClientTestRule(appRule);


  @After
  public void tearDown() throws Exception {
  }

}
