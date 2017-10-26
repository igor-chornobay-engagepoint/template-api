package gov.ca.cwds.template.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.inject.Inject;
import gov.ca.cwds.template.exceptions.TemplateInstanceValidationException;
import gov.ca.cwds.template.persistence.dao.TemplateDao;
import gov.ca.cwds.template.persistence.model.TemplateInstance;
import gov.ca.cwds.template.service.dto.TemplateInstanceDTO;
import gov.ca.cwds.template.service.mapper.TemplateInstanceMapper;
import gov.ca.cwds.template.web.rest.parameters.TemplateParameterObject;

/**
 * @author CWDS TPT-2 Team
 */
public class TemplateInstancesService extends
    TypedCrudServiceAdapter<TemplateParameterObject, TemplateInstanceDTO, TemplateInstanceDTO> {

  private TemplateDao dao;

  @Inject
  private TemplateInstanceMapper templateInstanceMapper;

  @Inject
  public TemplateInstancesService(TemplateDao dao) {
    this.dao = dao;
  }

  @Override
  public TemplateInstanceDTO create(TemplateInstanceDTO dto) {
    validateContent(dto);
    TemplateInstance entity = templateInstanceMapper.toForm(dto);
    return templateInstanceMapper.toFormDTO(dao.create(entity));
  }

  @Override
  public TemplateInstanceDTO find(TemplateParameterObject params) {
    return templateInstanceMapper.toFormDTO(dao.find(params.getId()));
  }

  @Override
  public TemplateInstanceDTO update(TemplateParameterObject params, TemplateInstanceDTO dto) {
    validateContent(dto);
    TemplateInstance entity = templateInstanceMapper.toForm(dto);
    entity.setId(params.getId());
    return templateInstanceMapper.toFormDTO(dao.update(entity));
  }

  @Override
  public TemplateInstanceDTO delete(TemplateParameterObject params) {
    return templateInstanceMapper.toFormDTO(dao.delete(params.getId()));
  }

  private void validateContent(TemplateInstanceDTO dto) {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonSchema schema;
    JsonNode contentJson = objectMapper.valueToTree(dto.getContent());
    try {
      if (!false) {
        throw new TemplateInstanceValidationException("Validation Error");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }


}
