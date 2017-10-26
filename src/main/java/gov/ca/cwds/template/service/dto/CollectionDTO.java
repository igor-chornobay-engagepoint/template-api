package gov.ca.cwds.template.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.ca.cwds.rest.api.Response;
import java.util.Collection;

/**
 * @author CWDS TPT-2
 */
@SuppressWarnings("squid:S2160") //reflection equals hashcode is used in superclass
public class CollectionDTO<T extends BaseDTO> extends BaseDTO implements Response {

  private static final long serialVersionUID = 7775898799478004935L;

  @JsonProperty("items")
  private Collection<T> collection;

  public CollectionDTO() {
  }

  public CollectionDTO(Collection<T> collection) {
    this.collection = collection;
  }

  public Collection<T> getCollection() {
    return collection;
  }
}
