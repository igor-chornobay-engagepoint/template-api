package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;
import gov.ca.cwds.template.TemplateApiApplication;
import gov.ca.cwds.template.web.rest.filters.RequestExecutionContextFilter;
import gov.ca.cwds.template.web.rest.filters.RequestResponseLoggingFilter;
import gov.ca.cwds.rest.filters.WebSecurityFilter;

/**
 * Dependency injection (DI) for Filter classes.
 *
 * <p> Register filters her with Guice and configure them in {@link TemplateApiApplication}, method
 * registerFilters. </p>
 *
 * @author CWDS TPT-3 Team
 */
public class FiltersModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(RequestExecutionContextFilter.class);
    bind(RequestResponseLoggingFilter.class);
    bind(WebSecurityFilter.class);
  }

}
