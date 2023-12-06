package nl.abn.assignment.exception.handler;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.exception.ErrorResponse;
import nl.abn.assignment.exception.RecipeApplicationAccessDeniedException;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Handle validation failure exceptions which returns 400 server error
     *
     * @return response entity with internal server error status code
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse response =  ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message(ex.getMessage())
                .build();
        response.addValidationErrors(ex.getConstraintViolations());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation failure exceptions which returns 500 server error
     *
     * @return response entity with internal server error status code
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse response =  ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message("Validation errors at the DTO objects")
                .build();
        response.addValidationErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecipeApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> customExceptionFromTheApplications(final RecipeApplicationNotFoundException ex) {
        log.error("Exception: {} ", ex);
        ErrorResponse response =  ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.toString())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(RecipeApplicationAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> customAccessDeniedExceptionFromTheApplications(final RecipeApplicationAccessDeniedException ex) {
        log.error("Exception: {} ", ex);
        ErrorResponse response =  ErrorResponse.builder()
            .code(HttpStatus.FORBIDDEN.toString())
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }


    /**
     * Handle validation failure exceptions which returns 500 server error
     *
     * @return response entity with internal server error status code
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: {} ", e);
        ErrorResponse response =  ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("Oops! Something went wrong!")
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
