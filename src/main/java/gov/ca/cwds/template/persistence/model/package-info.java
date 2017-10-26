/**
 * @author CWDS TPT-2 Team
 */
@TypeDefs(
        value = {
                @TypeDef(
                        name = "FormInstanceType",
                        typeClass = JsonType.class,
                        parameters = {
                                @Parameter(name = SQL_TYPE, value = SQLTypes.OTHER_TYPE_NAME),
                                @Parameter(
                                        name = "returnedClassName",
                                        value = "com.fasterxml.jackson.databind.JsonNode"
                                )
                        }
                )
        }
)
package gov.ca.cwds.template.persistence.model;

import gov.ca.cwds.template.persistence.hibernate.JsonType;
import gov.ca.cwds.template.persistence.hibernate.SQLTypes;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import static gov.ca.cwds.template.Constants.SQL_TYPE;