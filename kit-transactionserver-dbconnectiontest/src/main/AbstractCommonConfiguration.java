package main;


import java.util.Properties;

public abstract class AbstractCommonConfiguration {

  protected final Properties properties;

  protected AbstractCommonConfiguration(Properties properties) {
    this.properties = properties;
  }// Constructor

  protected AbstractCommonConfiguration(String propertiesFileName, boolean external) {

    if (external) {
      properties = ResourcesLoader.loadExternalPropertyFile(propertiesFileName);
    }
    else {
      properties = ResourcesLoader.loadInternalPropertyFile(propertiesFileName);
    }// if-else

  }// Constructor

  private String readPropertyAsString(String propertyName, boolean mandatory) {
    String propertyValue = properties.getProperty(propertyName);
    if( propertyValue == null ) {
      if(mandatory) {
        throw new MissingMandatoryPropertyException(propertyName);
      }
    }
    return propertyValue;
  }

  protected String readStringProperty(String propertyName, boolean mandatory) {
    return readPropertyAsString(propertyName, mandatory);
  }

  protected Long readLongProperty(String propertyName, boolean mandatory) {
    String propertyValue = readPropertyAsString(propertyName, mandatory);

    try {
      return Long.valueOf(propertyValue);
    }
    catch(Exception e) {
      throw new InvalidPropertyException(propertyName, propertyValue, e);
    }

  }

  protected Integer readIntegerProperty(String propertyName, boolean mandatory, int min, int max) {

    String propertyValue = readPropertyAsString(propertyName, mandatory);

    // Trying to read an integer
    Integer intValue = null;
    try {
      intValue = Integer.valueOf(propertyValue);
    }
    catch(Exception e) {
      throw new InvalidPropertyException(propertyName, propertyValue, e);
    }

    if(intValue < min || intValue > max) {
      throw new InvalidPropertyValueRangeException(propertyName, propertyValue, min, max);
    }

    return intValue;

 }

  public boolean isValid() {
    if (properties != null) {
      return true;
    }
    return false;
  }

}// abstract class
