package gov.ca.cwds.template.persistence.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL9Dialect;

/**
 * @author CWDS TPT-2 Team
 */
public class JsonbSupportPostgreSQL9Dialect extends PostgreSQL9Dialect {

  public JsonbSupportPostgreSQL9Dialect() {
    this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
  }
}
