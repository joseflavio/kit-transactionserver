package main;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class DatabaseConfig extends AbstractCommonConfiguration implements StorageConfigurationI {

  private final String databaseDateFormat;

  private final String dbDriver;

  private final String dbUrl;

  private final String dbUser;

  private final String dbPassword;

  private final String dbCurrentDateTimeFunction;

  public DatabaseConfig(Properties properties) {
    super(properties);

    this.dbDriver = readStringProperty("database.driver", true);
    this.dbUrl = readStringProperty("database.url", true);

    this.dbUser = readStringProperty("database.username", true);
    this.dbPassword = readStringProperty("database.password", true);
    this.dbCurrentDateTimeFunction = readStringProperty("database.currentTimestampFunction", true);

    this.databaseDateFormat = readStringProperty("database.timestampFormat", true);

  }// Constructor

  public String format(Date realDate) {
    if (realDate != null) {
      return getDatabaseDateFormat().format(realDate);
    }
    return null;
  }

  public SimpleDateFormat getDatabaseDateFormat() {
    return new SimpleDateFormat(databaseDateFormat);
  }

  public String getDbDriver() {
    return dbDriver;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public String getDbUser() {
    return dbUser;
  }

  public String getDbCurrentDateTimeFunction() {
    return dbCurrentDateTimeFunction;
  }

  @Override
  public String toString() {
    String password = "(with password)";

    if (dbPassword.length() <= 0) {
      password = "(no password!)";
    }

    return "Database Connection in " + getDbUrl() + " .:. with User: '" + getDbUser() + "' " + password + ".";
  }

}// Class
