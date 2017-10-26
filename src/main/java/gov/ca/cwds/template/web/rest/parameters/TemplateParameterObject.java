package gov.ca.cwds.template.web.rest.parameters;

import java.io.Serializable;

/**
 * @author CWDS TPT-2 Team
 */
public class TemplateParameterObject implements Serializable {

  private static final long serialVersionUID = 1033572121202014389L;

  private String name;

  private Long id;

  public TemplateParameterObject(String name, Long id) {
    this.name = name;
    this.id = id;
  }

  public TemplateParameterObject(String name) {
    this(name, null);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

}
