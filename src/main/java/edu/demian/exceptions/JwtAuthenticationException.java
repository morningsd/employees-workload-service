package edu.demian.exceptions;

public class JwtAuthenticationException extends RuntimeException {

  public JwtAuthenticationException(String message) {
    super(message);
  }

}
