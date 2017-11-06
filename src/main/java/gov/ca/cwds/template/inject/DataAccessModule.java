package gov.ca.cwds.template.inject;

import static gov.ca.cwds.template.Constants.UnitOfWork.TEMPLATE_UNIT_OF_WORK;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import gov.ca.cwds.template.TemplateApiConfiguration;
import gov.ca.cwds.template.persistence.model.TemplateInstance;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import org.hibernate.SessionFactory;

/**
 * @author CWDS TPT-2 Team
 */
public class DataAccessModule extends AbstractModule {


  private final ImmutableList<Class<?>> templateEntities = ImmutableList.<Class<?>>builder().add(
      TemplateInstance.class
  ).build();

  private final HibernateBundle<TemplateApiConfiguration> templateHibernateBundle =
      new HibernateBundle<TemplateApiConfiguration>(templateEntities, new SessionFactoryFactory()) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(TemplateApiConfiguration configuration) {
          return configuration.getTemplateDataSourceFactory();
        }

        @Override
        public String name() {
          return TEMPLATE_UNIT_OF_WORK;
        }

        @Override
        public void configure(org.hibernate.cfg.Configuration configuration) {
          configuration.addPackage("gov.ca.cwds.template.persistence.model");
        }
      };


  public DataAccessModule(Bootstrap<? extends TemplateApiConfiguration> bootstrap) {
    bootstrap.addBundle(templateHibernateBundle);
  }

  @Override
  protected void configure() {
    //do nothing
  }

  @Provides
  @TemplateSessionFactory
  SessionFactory templateSessionFactory() {
    return templateHibernateBundle.getSessionFactory();
  }


  @Provides
  @TemplateHibernateBundle
  public HibernateBundle<TemplateApiConfiguration> getTemplateHibernateBundle() {
    return templateHibernateBundle;
  }

}
