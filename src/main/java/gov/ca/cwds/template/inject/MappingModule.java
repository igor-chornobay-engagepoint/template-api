package gov.ca.cwds.template.inject;

import com.google.inject.AbstractModule;
import gov.ca.cwds.template.service.mapper.TemplateInstanceMapper;

/**
 * DI (dependency injection) setup for mapping classes.
 *
 * @author CWDS TPT-2 Team
 */

public class MappingModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TemplateInstanceMapper.class).to(TemplateInstanceMapper.INSTANCE.getClass()).asEagerSingleton();
  }

}


