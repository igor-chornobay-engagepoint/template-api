package gov.ca.cwds.template.inject;

import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * @author CWDS TPT-2 Team
 */
public final class InjectorHolder {

  public static final InjectorHolder INSTANCE = new InjectorHolder();

  private Injector injector;

  private InjectorHolder() {
  }

  public Injector getInjector() {
    return injector;
  }

  public void setInjector(Injector injector) {
    this.injector = injector;
  }

  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(Key.get(clazz));
  }

}
