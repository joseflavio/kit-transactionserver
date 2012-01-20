package main;



public class ResourceException extends KeepInTouchRuntimeException {

  private static final long serialVersionUID = 3850153076203185961L;

  private final String resourceName;

  public ResourceException(String message, String resourceName, Exception cause) {
    super(message, cause);
    this.resourceName = resourceName;
  }// constructor

  @Override
  public String getMessage() {
    return super.getMessage() + " (file: '" + resourceName + "').";
  }

}// exception class
