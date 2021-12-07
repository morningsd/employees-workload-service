package edu.demian.controllers;

import edu.demian.exceptions.ExceptionInformation;
import edu.demian.exceptions.InvalidInputDataException;
import edu.demian.exceptions.JwtAuthenticationException;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceHasNoSuchPropertyException;
import edu.demian.exceptions.ResourceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  ExceptionInformation handleNotFound(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  ExceptionInformation handleConflict(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ResourceHasNoSuchPropertyException.class)
  ExceptionInformation handleUnprocessableEntity(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(JwtAuthenticationException.class)
  ExceptionInformation handleInvalidAuthenticationData(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidInputDataException.class)
  ExceptionInformation handleInvalidInputData(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

}