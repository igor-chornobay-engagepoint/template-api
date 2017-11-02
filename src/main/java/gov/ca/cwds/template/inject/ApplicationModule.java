package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import gov.ca.cwds.template.TemplateApiConfiguration;
import gov.ca.cwds.inject.AuditingModule;
import gov.ca.cwds.rest.WebSecurityConfiguration;
import io.dropwizard.setup.Bootstrap;

/**
 * Bootstraps and configures the CWDS RESTful TEMPLATE_UNIT_OF_WORK-API application.
 *
 * @author CWDS TPT-3 Team
 */

public class ApplicationModule<T extends TemplateApiConfiguration> extends AbstractModule {

  private Bootstrap<T> bootstrap;

  public ApplicationModule(Bootstrap<T> bootstrap) {
    super();
    this.bootstrap = bootstrap;
  }

  /**
   * Configure and initialize API components, including services, rest, data access objects (DAO),
   * web service filters, and auditing.
   *
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    install(new ServicesModule());
    install(new ResourcesModule());
    install(new AuditingModule());
    install(new MappingModule());
    install(new FiltersModule());
  }

  public Bootstrap<T> getBootstrap() {
    return bootstrap;
  }

  @Provides
  public WebSecurityConfiguration provideWebSecurityConfiguration(T configuration) {
    return configuration.getWebSecurityConfiguration();
  }

  @Provides
  @Named("app.name")
  public String appName(T configuration) {
    return configuration.getApplicationName();
  }

  @Provides
  @Named("app.version")
  public String appVersion(T configuration) {
    return configuration.getVersion();
  }


}
