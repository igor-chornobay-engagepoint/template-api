package gov.ca.cwds.template.web.rest.filters;

import com.google.inject.Inject;
import gov.ca.cwds.logging.AuditLogger;
import gov.ca.cwds.logging.LoggingContext;
import gov.ca.cwds.logging.LoggingContext.LogParameter;
import gov.ca.cwds.rest.api.ApiException;
import gov.ca.cwds.rest.api.domain.DomainChef;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS TPT-3 Team
 */
@Provider
public class RequestResponseLoggingFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

  private AuditLogger auditLogger;
  private LoggingContext loggingContext;

  /**
   * Constructor
   *
   * @param auditLogger The audit logger
   * @param loggingContext API logging context
   */
  @Inject
  public RequestResponseLoggingFilter(AuditLogger auditLogger, LoggingContext loggingContext) {
    this.auditLogger = auditLogger;
    this.loggingContext = loggingContext;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String uniqueId = loggingContext.initialize();

    if (request instanceof HttpServletRequest) {

      HttpServletRequest httpServletRequest = (HttpServletRequest) request;

      loggingContext.setLogParameter(LogParameter.USER_ID,
          RequestExecutionContext.instance().getUserId());
      loggingContext.setLogParameter(LogParameter.REQUEST_START_TIME,
          DomainChef.cookStrictTimestamp(RequestExecutionContext.instance().getRequestStartTime()));
      loggingContext.setLogParameter(LogParameter.REMOTE_ADDRESS,
          httpServletRequest.getRemoteAddr());
      loggingContext.setLogParameter(LogParameter.REQUEST_ID, Thread.currentThread().getName());

      RequestResponseLoggingHttpServletRequest wrappedRequest =
          new RequestResponseLoggingHttpServletRequest(httpServletRequest);

      auditLogger.audit(httpServletRequest.toString());
      auditLogger.audit(requestContent(wrappedRequest));

      final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      RequestResponseLoggingHttpServletResponseWrapper wrappedResponse =
          new RequestResponseLoggingHttpServletResponseWrapper(httpServletResponse);
      try {
        chain.doFilter(wrappedRequest, wrappedResponse);
        StringBuilder reponseStringBuilder = new StringBuilder();
        reponseStringBuilder.append(wrappedResponse)
            .append(wrappedResponse.getContent());
        auditLogger
            .audit(reponseStringBuilder.toString().replaceAll("\n", " ").replaceAll("\r", ""));
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
        throw new ApiException("Unable to handle request:" + uniqueId, e);
      } finally {
        loggingContext.close();
      }
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Shall override parent abstract method but nothing to do
  }

  @Override
  public void destroy() {
    // Shall override parent abstract method but nothing to do
  }

  private String requestContent(HttpServletRequest request) throws IOException {
    String headerName;
    StringBuilder sb = new StringBuilder();
    Enumeration<String> headerNames = request.getHeaderNames();
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        headerName = headerNames.nextElement();
        sb.append(headerName).append(": ").append(request.getHeader(headerName));
      }
    }
    InputStream bodyInputStream = request.getInputStream();
    sb.append(new String(IOUtils.toByteArray(bodyInputStream), StandardCharsets.UTF_8));

    return sb.toString().replace('\n', ' ');
  }

  private static class RequestResponseLoggingHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] body;
    private final HttpServletRequest wrappedRequest;

    public RequestResponseLoggingHttpServletRequest(HttpServletRequest request) throws IOException {
      super(request);
      body = IOUtils.toByteArray(request.getInputStream());
      wrappedRequest = request;
    }

    /**
     * {@inheritDoc}
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
      return wrappedRequest.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
      final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
      return new ServletInputStream() {

        @Override
        public int read() {
          return byteArrayInputStream.read();
        }

        @Override
        public boolean isFinished() {
          return false;
        }

        @Override
        public boolean isReady() {
          return false;
        }

        @Override
        public void setReadListener(ReadListener arg0) {
          // Shall override parent abstract method but nothing to do
        }
      };
    }
  }

  private static class RequestResponseLoggingHttpServletResponseWrapper
      extends HttpServletResponseWrapper {

    private TeeServletOutputStream teeStream;

    private PrintWriter teeWriter;

    private ByteArrayOutputStream bos;

    private HttpServletResponse wrappedResponse;

    public RequestResponseLoggingHttpServletResponseWrapper(HttpServletResponse response) {
      super(response);
      wrappedResponse = response;
    }

    public String getContent() {
      return bos == null ? "" : bos.toString();
    }

    @Override
    public PrintWriter getWriter() throws IOException {

      if (this.teeWriter == null) {
        this.teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
      }
      return this.teeWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

      if (teeStream == null) {
        bos = new ByteArrayOutputStream();
        teeStream = new TeeServletOutputStream(getResponse().getOutputStream(), bos);
      }
      return teeStream;
    }

    @Override
    public void flushBuffer() throws IOException {
      if (teeStream != null) {
        teeStream.flush();
      }
      if (this.teeWriter != null) {
        this.teeWriter.flush();
      }
    }

    /**
     * {@inheritDoc}
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
      return wrappedResponse.toString();
    }

    private static class TeeServletOutputStream extends ServletOutputStream {

      private final TeeOutputStream targetStream;

      public TeeServletOutputStream(OutputStream one, OutputStream two) {
        targetStream = new TeeOutputStream(one, two);
      }

      @Override
      public void write(int arg0) throws IOException {
        this.targetStream.write(arg0);
      }

      @Override
      public void flush() throws IOException {
        super.flush();
        this.targetStream.flush();
      }

      @Override
      public void close() throws IOException {
        super.close();
        this.targetStream.close();
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setWriteListener(WriteListener writeListener) {
        // Shall override parent abstract method but nothing to do
      }
    }
  }
}
