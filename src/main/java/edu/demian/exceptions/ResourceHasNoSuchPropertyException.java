package edu.demian.exceptions;

public class ResourceHasNoSuchPropertyException extends RuntimeException {

  public ResourceHasNoSuchPropertyException() {
    super();
  }

  public ResourceHasNoSuchPropertyException(String message) {
    super(message);
  }

  public ResourceHasNoSuchPropertyException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceHasNoSuchPropertyException(Throwable cause) {
    super(cause);
  }

  protected ResourceHasNoSuchPropertyException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
