package gov.ca.cwds.template.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by leonid.marushevskiy on 6/30/2017.
 */
public class LoggingFilter implements ClientRequestFilter, ClientResponseFilter {

  private final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    LOG.info(System.getProperty("line.separator") + "!!! Test Request" + System
            .getProperty("line.separator") + "!!! URL: {}" + System.getProperty("line.separator")
            + "!!! Method: {}" + System.getProperty("line.separator") + "!!! Body: {}",
        requestContext.getUri(),
        requestContext.getMethod(),
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestContext.getEntity()));
  }

  @Override
  public void filter(ClientRequestContext clientRequestContext,
      ClientResponseContext clientResponseContext) throws IOException {
    LOG.info(System.getProperty("line.separator") + "!!! Test Response " + System
            .getProperty("line.separator") + "!!! Body: {} " + System.getProperty("line.separator")
            + "!!! Status: {}",
        responseToString(clientResponseContext),
        clientResponseContext.getStatus());
  }

  private String responseToString(ClientResponseContext clientResponseContext) {
    InputStream inputStream = clientResponseContext.getEntityStream();
    if (inputStream == null) {
      return "";
    } else {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        IOUtils.copy(inputStream, baos);
        InputStream restoredStream = new ByteArrayInputStream(
            baos.toString().getBytes(StandardCharsets.UTF_8));
        clientResponseContext.setEntityStream(restoredStream);
      } catch (IOException e) {
        // nothing to do
      }
      return baos.toString();
    }
  }
}
