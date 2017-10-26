package gov.ca.cwds.template.persistence.hibernate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.template.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * @author CWDS TPT-2 Team.
 */
public class JsonType implements UserType, ParameterizedType {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.registerModule(new JavaTimeModule());
  }

  private int sqlType;

  private String returnedClassName;

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.JAVA_OBJECT};
  }

  @Override
  public boolean equals(Object x, Object y) {
    return x == y || x.equals(y);
  }

  @Override
  public int hashCode(Object x) {
    return x.hashCode();
  }

  @Override
  public Class returnedClass() {
    try {
      return Class.forName(returnedClassName);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Class: " + returnedClassName + " is not found.", e);
    }
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws SQLException {
    try {
      final String cellContent = rs.getString(names[0]);
      if (cellContent == null) {
        return null;
      }
      return mapper.readValue(cellContent.getBytes(StandardCharsets.UTF_8), returnedClass());
    } catch (final SQLException sqle) {
      throw sqle;
    } catch (final Exception ex) {
      throw new ConvertingException(
          String.format(
              "Failed to convert String to %s: %s",
              returnedClass().getSimpleName(), ex.getMessage()),
          ex);
    }
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws SQLException {
    try {
      if (value == null) {
        st.setNull(index, sqlType);
        return;
      }
      final StringWriter stringWriter = new StringWriter();
      mapper.writeValue(stringWriter, value);
      stringWriter.flush();
      st.setObject(index, stringWriter.toString(), sqlType);
    } catch (final SQLException sqle) {
      throw sqle;
    } catch (final Exception ex) {
      throw new ConvertingException(
          String.format(
              "Failed to convert %s to String: %s",
              returnedClass().getSimpleName(), ex.getMessage()),
          ex);
    }
  }

  @Override
  @SuppressFBWarnings("OBJECT_DESERIALIZATION") // There is no external objects
  public Object deepCopy(Object value) {
    if (value instanceof JsonNode) {
      return ((JsonNode) value).deepCopy();
    } else {
      try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(value);
        oos.flush();
        oos.close();
        bos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
        return new ObjectInputStream(bais).readObject();
      } catch (ClassNotFoundException | IOException ex) {
        throw new HibernateException(ex);
      }
    }
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(Object value) {
    return (Serializable) deepCopy(value);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) {
    return deepCopy(cached);
  }

  @Override
  public Object replace(Object original, Object target, Object owner) {
    return deepCopy(original);
  }

  @Override
  public void setParameterValues(Properties parameters) {
    String sqlTypeName = parameters.getProperty(Constants.SQL_TYPE);
    SQLTypes sqlTypeEnum = SQLTypes.valueOf(sqlTypeName);
    if (sqlTypeEnum != null) {
      sqlType = sqlTypeEnum.getType();
    } else {
      throw new IllegalArgumentException("The sqlType: " + sqlTypeName + " is not supported");
    }

    returnedClassName = parameters.getProperty(Constants.RETURNED_CLASS_NAME_PARAM);

  }
}
