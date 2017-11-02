package gov.ca.cwds.template.service.dto.system;

import com.codahale.metrics.health.HealthCheck;
import gov.ca.cwds.template.service.dto.BaseDTO;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CWDS TPT-3 Team
 */
@SuppressWarnings("squid:S2160")
public class HealthCheckResultDTO extends BaseDTO {

  private boolean healthy;
  private String message;
  private Throwable error;
  private HashMap<String, Object> details;
  private String timestamp;

  public void setResult(HealthCheck.Result result) {
    setHealthy(result.isHealthy());
    setMessage(result.getMessage());
    setError(result.getError());
    setDetails(result.getDetails());
    setTimestamp(result.getTimestamp());
  }

  public boolean isHealthy() {
    return healthy;
  }

  public void setHealthy(boolean healthy) {
    this.healthy = healthy;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Throwable getError() {
    return error;
  }

  public void setError(Throwable error) {
    this.error = error;
  }

  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(Map<String, Object> details) {
    this.details = details == null ? null : new HashMap<>(details);
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
