package gov.ca.cwds.template.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import gov.ca.cwds.security.jwt.JwtConfiguration;
import gov.ca.cwds.security.jwt.JwtService;
import gov.ca.cwds.template.TemplateApiConfiguration;
import gov.ca.cwds.template.web.rest.utils.TestModeUtils;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS TPT-2 Team
 */
public class RestClientTestRule implements TestRule {

  private static final Logger LOG = LoggerFactory.getLogger(RestClientTestRule.class);

  private final DropwizardAppRule<TemplateApiConfiguration> dropWizardApplication;

  private Client client;
  private ObjectMapper mapper;
  private String token;

  public RestClientTestRule(DropwizardAppRule<TemplateApiConfiguration> dropWizardApplication) {
    this.dropWizardApplication = dropWizardApplication;
    if (TestModeUtils.isIntegrationTestsMode()) {
      try {
        token = generateToken();
      } catch (Exception e) {
        LOG.warn("Cannot generate token");
      }
    }
  }

  public String generateToken() throws Exception {
    JwtConfiguration configuration = getJwtConfiguration();
    JwtService jwtService = new JwtService(configuration);
    return jwtService.generate("id", "subject", "identity");
  }

  private JwtConfiguration getJwtConfiguration() throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream("config/shiro.ini"));

    JwtConfiguration configuration = new JwtConfiguration();
    //JWT
    configuration.setTimeout(30);
    configuration.setIssuer(properties.getProperty("perryRealm.tokenIssuer"));
    configuration.setKeyStore(new JwtConfiguration.KeyStoreConfiguration());
    //KeyStore
    configuration.getKeyStore()
        .setPath(new File(properties.getProperty("perryRealm.keyStorePath")).getPath());
    configuration.getKeyStore().setPassword(properties.getProperty("perryRealm.keyStorePassword"));
    //Sign/Validate Key
    configuration.getKeyStore().setAlias(properties.getProperty("perryRealm.keyStoreAlias"));
    configuration.getKeyStore()
        .setKeyPassword(properties.getProperty("perryRealm.keyStoreKeyPassword"));
    //Enc Key
    configuration
        .setEncryptionEnabled(Boolean.valueOf(properties.getProperty("perryRealm.useEncryption")));
    configuration.getKeyStore()
        .setEncKeyPassword(properties.getProperty("perryRealm.encKeyPassword"));
    configuration.getKeyStore().setEncAlias(properties.getProperty("perryRealm.encKeyAlias"));
    configuration.setEncryptionMethod(properties.getProperty("perryRealm.encryptionMethod"));
    return configuration;
  }

  public WebTarget target(String pathInfo) {
    String restUrl = getUriString() + pathInfo;
    return client.target(restUrl).queryParam("token", token).register(new LoggingFilter());
  }

  protected String getUriString() {
    String serverUrlStr = System.getProperty(TestModeUtils.TEMPLATE_API_URL);
    if (StringUtils.isEmpty(serverUrlStr)) {
      serverUrlStr = composeUriString();
    }
    return serverUrlStr;
  }

  protected URI getServerUrl() {
    return dropWizardApplication.getEnvironment().getApplicationContext().getServer().getURI();
  }

  protected String composeUriString() {
    return String.format("http://localhost:%s/", dropWizardApplication.getLocalPort());
  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  @Override
  public Statement apply(Statement statement, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {

        JerseyClientBuilder clientBuilder = new JerseyClientBuilder()
            .property(ClientProperties.CONNECT_TIMEOUT, 5000)
            .property(ClientProperties.READ_TIMEOUT, 20000)
            .hostnameVerifier((hostName, sslSession) -> {
              // Just ignore host verification for test purposes
              return true;
            });

        client = clientBuilder.build();

        // Trust All certificates for test purposes
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {
          }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {
          }
        }};

        client.getSslContext().init(null, trustAllCerts, new SecureRandom());

        mapper = dropWizardApplication.getObjectMapper();
        client.register(new JacksonJsonProvider(mapper));
        statement.evaluate();
      }
    };
  }
}
