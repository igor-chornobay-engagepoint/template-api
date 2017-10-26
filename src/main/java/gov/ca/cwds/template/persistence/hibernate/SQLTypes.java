package gov.ca.cwds.template.persistence.hibernate;

import java.sql.Types;

/**
 * @author CWDS TPT-2 Team
 */
public enum SQLTypes {
  CLOB(Types.CLOB),
  OTHER(Types.OTHER);

  public static final String CLOB_TYPE_NAME = "CLOB";
  public static final String OTHER_TYPE_NAME = "OTHER";

  private int type = -1;
  private String name;

  SQLTypes(int sqlType) {
    this.type = sqlType;
  }

  public int getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
