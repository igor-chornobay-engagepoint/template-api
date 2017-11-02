package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import gov.ca.cwds.template.web.rest.system.SystemInformationResource;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
import gov.ca.cwds.rest.resources.TypedServiceBackedResourceDelegate;

/**
 * Identifies all TEMPLATE_UNIT_OF_WORK API domain resource classes available for dependency injection
 * by Guice.
 *
 * @author CWDS TPT-3 Team
 */
public class ResourcesModule extends AbstractModule {

  /**
   * Default constructor
   */
  public ResourcesModule() {
    // Do nothing - Default Constructor
  }

  @Override
  protected void configure() {
    bind(SystemInformationResource.class);

  }
}
