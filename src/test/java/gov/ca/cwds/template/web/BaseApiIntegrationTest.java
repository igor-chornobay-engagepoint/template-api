package gov.ca.cwds.template.web;

import gov.ca.cwds.template.DatabaseHelper;
import gov.ca.cwds.template.TemplateApiApplication;
import gov.ca.cwds.template.TemplateApiConfiguration;
import gov.ca.cwds.template.web.rest.RestClientTestRule;
import gov.ca.cwds.template.web.rest.utils.TestModeUtils;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;

/**
 * @author CWDS TPT-2 Team
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


  protected static DatabaseHelper getFormsDatabaseHelper() {
    DataSourceFactory dataSourceFactory = appRule.getConfiguration().getTemplateDataSourceFactory();
    return new DatabaseHelper(
        dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword());
  }


  public static void setUpFormsDb() throws Exception {
    if (!TestModeUtils.isIntegrationTestsMode()) {
      getFormsDatabaseHelper().runScript("liquibase/forms_database_master.xml");
    }
  }

  public static LocalDateTime toLocalDateTime(String dateTime) {
    return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
  }

  public static String getDataFromRawResponse(Response response) throws IOException {
    return IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
  }

  public String transformDTOtoJSON(Object o) throws Exception {
    return clientTestRule.getMapper().writeValueAsString(o);
  }

  @After
  public void tearDown() throws Exception {
  }

}
