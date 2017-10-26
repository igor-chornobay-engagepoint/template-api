package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import gov.ca.cwds.template.service.TemplateInstancesCollectionService;
import gov.ca.cwds.template.service.TemplateInstancesService;
import gov.ca.cwds.template.service.dto.TemplateInstanceDTO;
import gov.ca.cwds.template.web.rest.TemplateInstancesResource;
import gov.ca.cwds.template.web.rest.parameters.TemplateParameterObject;
import gov.ca.cwds.template.web.rest.system.SystemInformationResource;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
import gov.ca.cwds.rest.resources.TypedServiceBackedResourceDelegate;

/**
 * Identifies all TEMPLATE_UNIT_OF_WORK API domain resource classes available for dependency injection
 * by Guice.
 *
 * @author TPT-2 Team
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

    bind(TemplateInstancesResource.class);
  }

  @Provides
  @TemplateServiceBackedResource
  public TypedResourceDelegate<TemplateParameterObject, TemplateInstanceDTO> templateServiceBackedResource(
      Injector injector) {
    return new TypedServiceBackedResourceDelegate<>(
        injector.getInstance(TemplateInstancesService.class));
  }

  @Provides
  @TemplateCollectionServiceBackedResource
  public ResourceDelegate templateCollectionServiceBackedResource(
      Injector injector) {
    return new ServiceBackedResourceDelegate(
        injector.getInstance(TemplateInstancesCollectionService.class));
  }

}
