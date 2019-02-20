package com.dm.teamquery.execption;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String INVALID_PAGING_PARAMETER = "You have entered an invalid offset or limit";
    private final static String INVALID_PARAMETER = "You have entered an invalid query parameter";
    private final static String RESOURCE_NOT_FOUND = "The requested resource could not be found. ";
    private final static String UNSUPPORTED_MESSAGE = "The request method is not supported";

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException e ) {
        return buildResponseEntity(INVALID_PAGING_PARAMETER, HttpStatus.BAD_REQUEST, e.getErrorList());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e ) {
        return buildResponseEntity(INVALID_PARAMETER, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException e ) {
        return buildResponseEntity(INVALID_PARAMETER, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported (
            HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request){
        return buildResponseEntity(e, UNSUPPORTED_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errorDetails = new ArrayList<>();
        errorDetails.add("The URL is invalid: " + e.getHeaders().getHost() + e.getRequestURL());


        return buildResponseEntity(RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, errorDetails);
    }

    @ExceptionHandler
    public String constraintViolationHandler(ConstraintViolationException ex) {
        return ex.getConstraintViolations().iterator().next()
                .getMessage();
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status, List<String> errorDetails) {
        ApiError apiError = new ApiError(status);
        apiError.setMessage(message);
        apiError.getErrorMessageList().addAll(errorDetails);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(Exception e, String message, HttpStatus status) {
        List<String> errorDetails = new ArrayList<>();
        errorDetails.add(ExceptionUtils.getRootCauseMessage(e));
        return buildResponseEntity(message, status, errorDetails);
    }
}

@Getter @Setter
class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errorMessageList = new ArrayList<>();

    ApiError(HttpStatus status) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }
}
