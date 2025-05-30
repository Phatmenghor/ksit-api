package com.menghor.ksit.exceptoins.error;

import com.menghor.ksit.exceptoins.response.ErrorObject;
import com.menghor.ksit.exceptoins.response.ErrorRequestObject;
import com.menghor.ksit.exceptoins.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(path);
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    // Handle BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> handleBadRequestException(BadRequestException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(path);
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    // Generic Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGlobalException(Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage("An unexpected error occurred: " + ex.getMessage());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(path);
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorObject> handleInvalidArgumentException(IllegalArgumentException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        errorObject.setPath(path);
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRequestObject> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorRequestObject errorObject = new ErrorRequestObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());

        // Collect all field errors as a list of maps
        List<Map<String, String>> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("field", error.getField());
            errorMap.put("message", error.getDefaultMessage());
            errorMessages.add(errorMap);
        }

        // Set the error messages as a list of maps
        errorObject.setMessage(errorMessages);
        errorObject.setTimestamp(new Date());
        errorObject.setPath(path);

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    // Handle duplicate name exception
    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNameException(
            DuplicateNameException ex, WebRequest request) {

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(
                "error",              // status
                ex.getMessage(),      // message (e.g., "Name already exists")
                HttpStatus.CONFLICT.value(), // status code (409)
                path                  // path
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
