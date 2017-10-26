package gov.ca.cwds.template;

import com.google.inject.Module;
import com.google.inject.Provides;
import gov.ca.cwds.template.inject.ApplicationModule;
import gov.ca.cwds.template.inject.DataAccessModule;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;

/**
 * @author CWDS TPT-2 Team
 */

public class TemplateApiApplication extends BaseTemplateApiApplication<TemplateApiConfiguration> {

  public static void main(String[] args) throws Exception {
    new TemplateApiApplication().run(args);
  }

  @Override
  public Module applicationModule(Bootstrap<TemplateApiConfiguration> bootstrap) {
    return new ApplicationModule<TemplateApiConfiguration>(bootstrap) {

      @Override
      protected void configure() {
        super.configure();
        install(new DataAccessModule(bootstrap) {

          @Provides
          UnitOfWorkAwareProxyFactory provideUnitOfWorkAwareProxyFactory() {
            return new UnitOfWorkAwareProxyFactory(
                getTemplateHibernateBundle()
            );
          }

        });
      }

    };
  }

}
