package gov.ca.cwds.template.web.rest;

import static gov.ca.cwds.template.Constants.API.TEMPLATE_INSTANCES_PATH;
import static gov.ca.cwds.template.Constants.API.TEMPLATE_INSTANCE_TAG;
import static gov.ca.cwds.template.Constants.API.PathParams.TEMPLATE_ID_PARAMETER;
import static gov.ca.cwds.template.Constants.API.PathParams.TEMPLATE_NAME_PARAMETER;
import static gov.ca.cwds.template.Constants.UnitOfWork.TEMPLATE_UNIT_OF_WORK;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import gov.ca.cwds.template.inject.TemplateCollectionServiceBackedResource;
import gov.ca.cwds.template.inject.TemplateServiceBackedResource;
import gov.ca.cwds.template.service.dto.TemplateCollectionDTO;
import gov.ca.cwds.template.service.dto.TemplateInstanceDTO;
import gov.ca.cwds.template.web.rest.parameters.TemplateParameterObject;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author CWDS TPT-2 Team
 */
@Api(tags = {TEMPLATE_INSTANCE_TAG})
@Path(TEMPLATE_INSTANCES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateInstancesResource {

  private TypedResourceDelegate<TemplateParameterObject, TemplateInstanceDTO> resourceDelegate;
  private ResourceDelegate collectionResourceDelegate;

  @Inject
  public TemplateInstancesResource(
      @TemplateServiceBackedResource
          TypedResourceDelegate<TemplateParameterObject, TemplateInstanceDTO> resourceDelegate,
      @TemplateCollectionServiceBackedResource
          ResourceDelegate collectionResourceDelegate) {
    this.resourceDelegate = resourceDelegate;
    this.collectionResourceDelegate = collectionResourceDelegate;
  }

  @UnitOfWork(TEMPLATE_UNIT_OF_WORK)
  @POST
  @Timed
  @ApiResponses(
      value = {
          @ApiResponse(code = 201, message = "Created"),
          @ApiResponse(code = 401, message = "Not Authorized"),
          @ApiResponse(code = 406, message = "Accept Header not supported")
      }
  )
  @ApiOperation(value = "Creates and returns Form Instance", response = TemplateInstanceDTO.class)
  public Response createFormInstance(
      @ApiParam(name = "form", value = "The Form object")
      @Valid
          TemplateInstanceDTO form) {
    return resourceDelegate.create(form);
  }

  @UnitOfWork(TEMPLATE_UNIT_OF_WORK)
  @GET
  @Path("/{" + TEMPLATE_NAME_PARAMETER + "}")
  @Timed
  @ApiResponses(
      value = {
          @ApiResponse(code = 401, message = "Not Authorized"),
          @ApiResponse(code = 404, message = "Not found"),
          @ApiResponse(code = 406, message = "Accept Header not supported")
      }
  )
  @ApiOperation(value = "Returns All available Form Instances for name", response = TemplateCollectionDTO.class)
  public Response getAllForms(
      @PathParam(TEMPLATE_NAME_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_NAME_PARAMETER, value = "Form Name")
          String formName
  ) {
    return collectionResourceDelegate.get(new TemplateParameterObject(formName));
  }

  @UnitOfWork(TEMPLATE_UNIT_OF_WORK)
  @GET
  @Path("/{" + TEMPLATE_NAME_PARAMETER + "}" + "/{" + TEMPLATE_ID_PARAMETER + "}")
  @Timed
  @ApiResponses(
      value = {
          @ApiResponse(code = 401, message = "Not Authorized"),
          @ApiResponse(code = 404, message = "Not found"),
          @ApiResponse(code = 406, message = "Accept Header not supported")
      }
  )
  @ApiOperation(value = "Returns Form by name and Id", response = TemplateInstanceDTO.class)
  public Response getForm(
      @PathParam(TEMPLATE_NAME_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_NAME_PARAMETER, value = "Form Name")
          String formName,

      @PathParam(TEMPLATE_ID_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_ID_PARAMETER, value = "Form Instance Id")
          Long formId) {
    return resourceDelegate.get(new TemplateParameterObject(formName, formId));
  }

  @UnitOfWork(TEMPLATE_UNIT_OF_WORK)
  @PUT
  @Path("/{" + TEMPLATE_NAME_PARAMETER + "}" + "/{" + TEMPLATE_ID_PARAMETER + "}")
  @Timed
  @ApiResponses(
      value = {
          @ApiResponse(code = 400, message = "Bad request"),
          @ApiResponse(code = 404, message = "Not Found"),
          @ApiResponse(code = 401, message = "Not Authorized"),
          @ApiResponse(code = 406, message = "Accept Header not supported")
      }
  )
  @ApiOperation(value = "Updates Form Schema", response = TemplateInstanceDTO.class)
  public Response updateForm(
      @PathParam(TEMPLATE_NAME_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_NAME_PARAMETER, value = "Form Name")
          String formName,

      @PathParam(TEMPLATE_ID_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_ID_PARAMETER, value = "Form Instance Id")
          Long formId,

      @ApiParam(name = "form", value = "The Form object")
      @Valid
          TemplateInstanceDTO formInstanceDTO) {
    return resourceDelegate.update(new TemplateParameterObject(formName, formId), formInstanceDTO);
  }

  @UnitOfWork(TEMPLATE_UNIT_OF_WORK)
  @DELETE
  @Path("/{" + TEMPLATE_NAME_PARAMETER + "}" + "/{" + TEMPLATE_ID_PARAMETER + "}")
  @Timed
  @ApiResponses(
      value = {
          @ApiResponse(code = 400, message = "Bad request"),
          @ApiResponse(code = 404, message = "Not Found"),
          @ApiResponse(code = 401, message = "Not Authorized"),
          @ApiResponse(code = 406, message = "Accept Header not supported")
      }
  )
  @ApiOperation(value = "Delete Form Instance", response = TemplateInstanceDTO.class)
  public Response deleteForm(
      @PathParam(TEMPLATE_NAME_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_NAME_PARAMETER, value = "Form Name")
          String formName,

      @PathParam(TEMPLATE_ID_PARAMETER)
      @ApiParam(required = true, name = TEMPLATE_ID_PARAMETER, value = "Form Instance Id")
          Long formId
  ) {
    return resourceDelegate.delete(new TemplateParameterObject(formName, formId));
  }

}
