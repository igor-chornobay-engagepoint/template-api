package gov.ca.cwds.template.service.mapper;

import gov.ca.cwds.template.persistence.model.TemplateInstance;
import gov.ca.cwds.template.service.dto.TemplateInstanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author CWDS TPT-2 Team
 */
@SuppressWarnings("squid:S1214")
@Mapper
public interface TemplateInstanceMapper {

  TemplateInstanceMapper INSTANCE = Mappers.getMapper(TemplateInstanceMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "content", source = "content")
  TemplateInstance toForm(TemplateInstanceDTO dto);


  @Mapping(target = "formId", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "messages", ignore = true)
  TemplateInstanceDTO toFormDTO(TemplateInstance formInstance);
}
