package gov.ca.cwds.template;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.inject.Injector;
import gov.ca.cwds.template.exceptions.TemplateValidationExceptionMapperImpl;
import gov.ca.cwds.template.inject.InjectorHolder;
import gov.ca.cwds.template.web.rest.filters.RequestExecutionContextFilter;
import gov.ca.cwds.template.web.rest.filters.RequestResponseLoggingFilter;
import gov.ca.cwds.logging.LoggingContext;
import gov.ca.cwds.rest.BaseApiApplication;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS TPT-2 Team
 */
public abstract class BaseTemplateApiApplication<T extends TemplateApiConfiguration> extends
    BaseApiApplication<T> {

  private static final Logger LOG = LoggerFactory.getLogger(BaseTemplateApiApplication.class);
  private static final String LIQUIBASE_DATABASE_CREATE_SCHEMA_XML = "liquibase/template_schema.xml";
  private static final String LIQUIBASE_DATABASE_MASTER_XML = "liquibase/template_database_master.xml";
  private static final String HIBERNATE_DEFAULT_SCHEMA_PROPERTY_NAME = "hibernate.default_schema";

  @Override
  public void runInternal(T configuration, Environment environment) {

    registerCustomExceptionMappers(environment);

    environment.getObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    if (configuration.isUpgradeDbOnStart()) {
      upgardeDB(configuration);
    }

    environment
        .jersey()
        .getResourceConfig()
        .packages(getClass().getPackage().getName())
        .register(DeclarativeLinkingFeature.class);

    runDataSourceHealthChecks(environment);

    Injector injector = guiceBundle.getInjector();

    // Providing access to the guice injector from external classes such as custom validators
    InjectorHolder.INSTANCE.setInjector(injector);

    environment.servlets()
        .addFilter("RequestExecutionContextManagingFilter",
            injector.getInstance(RequestExecutionContextFilter.class))
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

    environment.servlets()
        .addFilter("AuditAndLoggingFilter",
            injector.getInstance(RequestResponseLoggingFilter.class))
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }

  private void runDataSourceHealthChecks(Environment environment) {
    HealthCheckRegistry healthCheckRegistry = environment.healthChecks();
    doHealthCheck(healthCheckRegistry, Constants.UnitOfWork.TEMPLATE_UNIT_OF_WORK);
  }

  private void doHealthCheck(HealthCheckRegistry healthCheckRegistry, String key) {
    HealthCheck.Result result = healthCheckRegistry.runHealthCheck(key);
    if (!result.isHealthy()) {
      LOG.error("Fail - {}: {}", key, result.getMessage());
    }
  }

  private void registerCustomExceptionMappers(Environment environment) {
    LoggingContext loggingContext = guiceBundle.getInjector().getInstance(LoggingContext.class);
    environment.jersey().register(new TemplateValidationExceptionMapperImpl(loggingContext));
  }

  private void upgardeDB(TemplateApiConfiguration configuration) {
    LOG.info("Upgrading DB...");

    DataSourceFactory formsDataSourceFactory = configuration.getTemplateDataSourceFactory();
    DatabaseHelper databaseHelper = new DatabaseHelper(
        formsDataSourceFactory.getUrl(),
        formsDataSourceFactory.getUser(),
        formsDataSourceFactory.getPassword()
    );
    try {
      databaseHelper.runScript(LIQUIBASE_DATABASE_CREATE_SCHEMA_XML);
      databaseHelper.runScript(
          LIQUIBASE_DATABASE_MASTER_XML,
          formsDataSourceFactory.getProperties().get(HIBERNATE_DEFAULT_SCHEMA_PROPERTY_NAME)
      );
    } catch (Exception e) {
      LOG.error("Upgarding of DB is failed. ", e);
      throw new IllegalStateException(e);
    }

    LOG.info("Finish Upgrading DB");
  }
}
