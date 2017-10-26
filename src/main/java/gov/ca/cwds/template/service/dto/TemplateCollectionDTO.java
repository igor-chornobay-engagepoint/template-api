package gov.ca.cwds.template.service.dto;

import java.util.Collection;

/**
 * @author CWDS TPT-2 Team
 */
public final class TemplateCollectionDTO extends CollectionDTO<TemplateInstanceDTO> {

  public TemplateCollectionDTO() {
  }

  public TemplateCollectionDTO(Collection<TemplateInstanceDTO> collection) {
    super(collection);
  }
}
