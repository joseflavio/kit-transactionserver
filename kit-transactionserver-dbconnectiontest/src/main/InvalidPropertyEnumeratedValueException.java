package main;


import java.util.Arrays;

public class InvalidPropertyEnumeratedValueException extends InvalidPropertyException {

  private static final long serialVersionUID = 6794532356643871944L;

  private final String[] validValues;

  public InvalidPropertyEnumeratedValueException(String propertyName, String propertyValue, String... validValues) {
    super(propertyName, propertyValue);
    if (validValues != null) {
      this.validValues = validValues;
    }
    else {
      final String[] missingValues = { "null valid values" };
      this.validValues = missingValues;
    }
  }// Constructor

  @Override
  public String getMessage() {
    return super.getMessage() + " Valid values are: " + Arrays.asList(validValues) + ".";
  }

}// exception class
