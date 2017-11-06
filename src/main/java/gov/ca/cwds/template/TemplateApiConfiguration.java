package gov.ca.cwds.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.ca.cwds.rest.MinimalApiConfiguration;
import io.dropwizard.db.DataSourceFactory;

public class TemplateApiConfiguration extends MinimalApiConfiguration {

  private DataSourceFactory templateDataSourceFactory;

  private boolean upgradeDbOnStart = false;

  @JsonProperty
  public DataSourceFactory getTemplateDataSourceFactory() {
    return templateDataSourceFactory;
  }

  public void setTemplateDataSourceFactory(DataSourceFactory templateDataSourceFactory) {
    this.templateDataSourceFactory = templateDataSourceFactory;
  }

  @JsonProperty
  public boolean isUpgradeDbOnStart() {
    return upgradeDbOnStart;
  }

  public void setUpgradeDbOnStart(boolean upgradeDbOnStart) {
    this.upgradeDbOnStart = upgradeDbOnStart;
  }

}
