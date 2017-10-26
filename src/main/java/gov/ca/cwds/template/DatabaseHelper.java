package gov.ca.cwds.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * @author CWDS TPT-2 Team
 */
public class DatabaseHelper {

  private Database database;
  private String url;
  private String user;
  private String password;

  public DatabaseHelper(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  public void runScript(String script) throws LiquibaseException {
    try {
      Liquibase liquibase = new Liquibase(script, new ClassLoaderResourceAccessor(), getDatabase());
      liquibase.update((String) null);
    } catch (Exception e) {
      throw new LiquibaseException(e);
    }
  }
  public void runScript(String script, Map<String, Object> parameters, String schema)
      throws LiquibaseException {
    try {
      String defaultSchema = getDatabase().getDefaultSchemaName();
      getDatabase().setDefaultSchemaName(schema);
      Liquibase liquibase = new Liquibase(script, new ClassLoaderResourceAccessor(), getDatabase());
      parameters.forEach(liquibase::setChangeLogParameter);
      liquibase.update((String) null);
      getDatabase().setDefaultSchemaName(defaultSchema);
    } catch (Exception e) {
      throw new LiquibaseException(e);
    }
  }

  public void runScript(String script, String schema) throws LiquibaseException {
    try {
      String defaultSchema = getDatabase().getDefaultSchemaName();
      getDatabase().setDefaultSchemaName(schema);
      runScript(script);
      getDatabase().setDefaultSchemaName(defaultSchema);
    } catch (Exception e) {
      throw new LiquibaseException(e);
    }
  }

  private Database getDatabase() throws SQLException, DatabaseException {
    if (database == null) {
      Connection connection = DriverManager.getConnection(url, user, password);
      database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
    }

    return database;
  }
}
