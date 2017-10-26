package gov.ca.cwds.template.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import javax.validation.constraints.NotNull;

/**
 * @author CWDS TPT-2 Team
 */
@SuppressWarnings({"squid:S2160", "squid:S1948"})
//Default reflection hashcode and equals resides in BaseDTO
@SuppressFBWarnings("SE_BAD_FIELD")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TemplateInstanceDTO extends BaseDTO implements Request, Response {

  private static final long serialVersionUID = -8643054699905680947L;

  private String formId;

  @NotNull
  private String name;

  @NotNull
  private JsonNode content;

  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonNode getContent() {
    return content;
  }

  public void setContent(JsonNode content) {
    this.content = content;
  }
}
