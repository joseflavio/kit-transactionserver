package main;
/*
 * KiT Sistemas Inteligentes - All Rights Reserved.
 * Confidential file, the unauthorized use or disclosure are strictly forbidden.
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
public class InvalidPropertyValueRangeException extends InvalidPropertyException {

  private static final long serialVersionUID = 7218327831667033547L;
  private final int min;
  private final int max;

  public InvalidPropertyValueRangeException(
      String propertyName, String propertyValue, int min, int max) {
    super(propertyName, propertyValue);
    this.min = min;
    this.max = max;
  }//constructor

  @Override
  public String getMessage() {
    return super.getMessage() + " The values should be between " + min + " and " + max + ".";
  }

}// Exception Class
