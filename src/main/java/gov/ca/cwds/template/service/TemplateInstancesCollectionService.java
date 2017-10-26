package gov.ca.cwds.template.service;

import com.google.inject.Inject;
import gov.ca.cwds.template.persistence.dao.TemplateDao;
import gov.ca.cwds.template.persistence.model.TemplateInstance;
import gov.ca.cwds.template.service.dto.TemplateCollectionDTO;
import gov.ca.cwds.template.service.dto.TemplateInstanceDTO;
import gov.ca.cwds.template.service.mapper.TemplateInstanceMapper;
import gov.ca.cwds.template.web.rest.parameters.TemplateParameterObject;
import gov.ca.cwds.rest.api.Response;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CWDS CALS API Team
 */
public class TemplateInstancesCollectionService extends CrudServiceAdapter {

  private TemplateDao dao;

  @Inject
  private TemplateInstanceMapper formInstanceMapper;

  @Inject
  public TemplateInstancesCollectionService(TemplateDao dao) {
    this.dao = dao;
  }

  @Override
  public Response find(Serializable params) {
    TemplateParameterObject paramObj = (TemplateParameterObject) params;
    Response res;
    if (paramObj.getName() != null) {
      res = getFormsByName(paramObj.getName());
    } else {
      throw new IllegalArgumentException("Form name expected but got NULL");
    }
    return res;
  }

  private Response getFormsByName(String name) {
    List<TemplateInstance> allSchemasEntities = dao.findByName(name);
    return convertToCollectionDTO(allSchemasEntities);
  }

  private Response convertToCollectionDTO(List<TemplateInstance> allSchemasEntities) {
    List<TemplateInstanceDTO> collection = allSchemasEntities.stream()
        .map(formInstanceMapper::toFormDTO)
        .collect(Collectors.toList());
    return new TemplateCollectionDTO(collection);
  }

}
