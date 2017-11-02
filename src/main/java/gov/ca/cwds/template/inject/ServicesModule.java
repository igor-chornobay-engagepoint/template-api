package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;

/**
 * Identifies all TEMPLATE_UNIT_OF_WORK API business layer (services) classes available for dependency
 * injection by Guice.
 *
 * @author TPT-2 Team
 */
public class ServicesModule extends AbstractModule {

  /**
   * Default constructor
   */
  public ServicesModule() {
    // Do nothing - Default constructor
  }

  @Override
  protected void configure() {
  }

}
