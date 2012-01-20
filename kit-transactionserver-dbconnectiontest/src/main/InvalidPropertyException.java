package main;
/*
 * KiT Sistemas Inteligentes - All Rights Reserved. Confidential file, the unauthorized use or
 * disclosure are strictly forbidden.
 */

/**
 * @filecreation 2008-11-12
 * @filecreator José Flávio Aguilar Paulino
 * @copyright 2007-2008 - KiT Sistemas Inteligentes - All Rights Reserved.
 */


/**
 * @author José Flávio Aguilar Paulino
 * @date 2008-11-12
 */
public class InvalidPropertyException extends RuntimeException {

  private static final long serialVersionUID = 330635469351926455L;

  protected final String propertyName;

  protected final String propertyValue;

  public InvalidPropertyException(String propertyName, String propertyValue) {
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
  }// Constructor

  public InvalidPropertyException(String propertyName, String propertyValue, Throwable cause) {
    super(cause);
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
  }// Constructor

  @Override
  public String getMessage() {
    return "The property: '" + propertyName + "' has an invalid value: '" + propertyValue + "'.";
  }

}// Exception Class
