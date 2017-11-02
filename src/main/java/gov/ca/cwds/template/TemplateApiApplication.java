package gov.ca.cwds.template;

import com.google.inject.Module;
import gov.ca.cwds.template.inject.ApplicationModule;
import io.dropwizard.setup.Bootstrap;

/**
 * @author CWDS TPT-3 Team
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


      }

    };
  }

}
