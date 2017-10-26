package gov.ca.cwds.template.exceptions;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.logging.LoggingContext;
import gov.ca.cwds.rest.exception.BaseExceptionResponse;
import gov.ca.cwds.rest.exception.IssueDetails;
import gov.ca.cwds.rest.exception.IssueType;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author CWDS TPT-2 Team
 */
public class TemplateValidationExceptionMapperImpl implements
    ExceptionMapper<TemplateInstanceValidationException> {

  private LoggingContext loggingContext;

  public TemplateValidationExceptionMapperImpl(LoggingContext loggingContext) {
    this.loggingContext = loggingContext;
  }

  @Override
  public Response toResponse(TemplateInstanceValidationException pe) {
    String report = pe.getReport();
    Set<IssueDetails> validationDetailsList = new HashSet<>();
        IssueDetailsExt details = new IssueDetailsExt();
        details.setType(IssueType.JSON_PROCESSING_EXCEPTION);
        details.setIncidentId(loggingContext.getUniqueId());
        details.setUserMessage("Template Instance validation issue");
        details.setTechnicalDetails(report);
        validationDetailsList.add(details);

    BaseExceptionResponse response = new BaseExceptionResponse();
    response.setIssueDetails(validationDetailsList);
    return Response.status(422).type(MediaType.APPLICATION_JSON_TYPE).entity(response).build();
  }


  @SuppressWarnings("squid:S1948")
  @SuppressFBWarnings("SE_BAD_FIELD")
  @JsonNaming(SnakeCaseStrategy.class)
  static class IssueDetailsExt extends IssueDetails {

    private Object technicalDetails;

    public Object getTechnicalDetails() {
      return technicalDetails;
    }

    public void setTechnicalDetails(Object technicalDetails) {
      this.technicalDetails = technicalDetails;
    }
  }
}
