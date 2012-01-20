package main;
/*
 * KiT Sistemas Inteligentes - All Rights Reserved. Confidential file, the unauthorized use or disclosure are
 * strictly forbidden.
 */

/**
 * @filecreation 2008-11-13
 * @filecreator José Flávio Aguilar Paulino
 * @copyright 2007-2008 - KiT Sistemas Inteligentes - All Rights Reserved.
 */


/**
 * @author José Flávio Aguilar Paulino
 * @date 2008-11-13
 */
public class MissingMandatoryPropertyException extends InvalidPropertyException {

  private static final long serialVersionUID = -6348938222953984164L;

  public MissingMandatoryPropertyException(String propertyName) {
    super(propertyName, null);
  }// Constructor

  @Override
  public String getMessage() {
    return "The property: '" + propertyName + "' could not be found and it is mandatory.";
  }

}// Exception Class
