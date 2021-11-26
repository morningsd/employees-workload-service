package edu.demian.controllers;

import edu.demian.exceptions.ExceptionInformation;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceHasNoSuchPropertyException;
import edu.demian.exceptions.ResourceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseBody
  ExceptionInformation handleNotFound(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  @ResponseBody
  ExceptionInformation handleConflict(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ResourceHasNoSuchPropertyException.class)
  @ResponseBody
  ExceptionInformation handleUnprocessableEntity(HttpServletRequest request, Exception exception) {
    return new ExceptionInformation(request.getRequestURL().toString(), exception);
  }

}
