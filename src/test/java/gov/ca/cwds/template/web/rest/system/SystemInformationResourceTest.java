package gov.ca.cwds.template.web.rest.system;

import gov.ca.cwds.template.Constants;
import gov.ca.cwds.template.service.dto.system.HealthCheckResultDTO;
import gov.ca.cwds.template.service.dto.system.SystemInformationDTO;
import gov.ca.cwds.template.web.BaseApiIntegrationTest;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SystemInformationResourceTest extends BaseApiIntegrationTest {

  @Test
  public void testSystemInformationGet() throws IOException {
    SystemInformationDTO systemInformationDTO = clientTestRule
        .target(Constants.API.SYSTEM_INFORMATION_PATH)
        .request(MediaType.APPLICATION_JSON)
        .get(SystemInformationDTO.class);
    assertEquals("Template API", systemInformationDTO.getApplication());
    assertNotNull(systemInformationDTO.getVersion());
    assertNotNull(systemInformationDTO.getDeadlocks());

    assertDataSource(systemInformationDTO.getTemplateDb());
  }

  public void assertDataSource(HealthCheckResultDTO healthCheckResultDTO) {
    assertNotNull(healthCheckResultDTO);
    assertTrue(healthCheckResultDTO.isHealthy());
  }
}