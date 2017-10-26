package gov.ca.cwds.template.persistence.model;

import com.fasterxml.jackson.databind.JsonNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.data.persistence.PersistentObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

/**
 * @author CWDS TPT-2 Team
 */

@SuppressWarnings("squid:S1948") //JsonNode is serializable
@SuppressFBWarnings("SE_BAD_FIELD")
@NamedQuery(name = TemplateInstance.NAMED_QUERY_FIND_ALL, query = "FROM TemplateInstance ORDER BY id DESC")
@NamedQuery(
    name = TemplateInstance.NAMED_QUERY_FIND_BY_TEMPLATE_NAME,
    query = "FROM TemplateInstance f WHERE f.name = :templateName ORDER BY id DESC")

@Entity
@Table(name = "template_instance")
public class TemplateInstance implements PersistentObject {

  public static final String NAMED_QUERY_FIND_BY_TEMPLATE_NAME =
      "gov.ca.cwds.template.persistence.model.TemplateInstance.find.by.templateName";

  public static final String NAMED_QUERY_FIND_ALL =
      "gov.ca.cwds.template.persistence.model.TemplateInstance.find.all";
  private static final long serialVersionUID = 8433047669468840813L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "template_name", length = 32, nullable = false)
  private String name;

  @Type(type = "FormInstanceType")
  @Column(name = "content")
  private JsonNode content;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonNode getContent() {
    return content;
  }

  public void setContent(JsonNode content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder
        .reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder
        .reflectionHashCode(this);
  }


  @Override
  public Serializable getPrimaryKey() {
    return getId();
  }
}
