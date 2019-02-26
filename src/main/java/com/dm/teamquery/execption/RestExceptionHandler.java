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
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String INVALID_PAGING_PARAMETER = "You have entered an invalid paging parameter";
    private final static String INVALID_ENTITY_ID = "You have entered an invalid entity id (UUID format required)";
    private final static String ENTITY_NOT_FOUND = "The specified entity does not exist";
    private final static String INVALID_PARAMETER = "You have entered an invalid query parameter";
    private final static String UNSUPPORTED_MESSAGE = "The request method is not supported";
    private final static String UNKNOWN_FAILURE = "Request failed for unknown reason";
    private final static String FIELD_ERROR_DETAIL = "Error: field '%s' - value '%s' is invalid: %s";
    private final static String FIELD_ERROR_DESCRIPTION = "Field errors were encountered during the requests";

    // Entity CRUD
    @ExceptionHandler(InvalidEntityIdException.class)
    protected ResponseEntity<Object> handleInvalidEntityIdException(InvalidEntityIdException e) {
        return buildResponseEntity(INVALID_ENTITY_ID, HttpStatus.BAD_REQUEST, e.getErrorList());
    }

    @ExceptionHandler(EntityNotFoundForIdException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundForIdException e) {
        return buildResponseEntity(ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND, e.getErrorList());
    }

    @ExceptionHandler({DeleteFailedException.class, SearchFailedException.class})
    protected ResponseEntity<Object> handleFailedException(TeamQueryException e) {
        return buildResponseEntity(UNKNOWN_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR, (e.getErrorList()));
    }

    // Paging
    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException e) {
        return buildResponseEntity(INVALID_PAGING_PARAMETER, HttpStatus.BAD_REQUEST, e.getErrorList());
    }

    // Controller
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildResponseEntity(INVALID_PARAMETER, HttpStatus.BAD_REQUEST, Arrays.asList(ExceptionUtils.getRootCauseMessage(e)));
    }

    // Request method
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(UNSUPPORTED_MESSAGE, HttpStatus.BAD_REQUEST, Arrays.asList(ExceptionUtils.getRootCauseMessage(e)));
    }

    // Field error handling
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorDetails =
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(f -> (FieldError) f)
                        .map(f -> String.format(FIELD_ERROR_DETAIL,
                                f.getField(),
                                (f.getRejectedValue() != null) ? f.getRejectedValue().toString() : "null",
                                f.getDefaultMessage())).collect(Collectors.toList());

        return buildResponseEntity(FIELD_ERROR_DESCRIPTION, HttpStatus.BAD_REQUEST, errorDetails);
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status, List<String> errorDetails) {
        ApiError apiError = new ApiError(status);
        apiError.setMessage(message);
        apiError.getErrorMessageList().addAll(errorDetails);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}

@Getter
@Setter
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

//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(
//            NoHandlerFoundException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<String> errorDetails = new ArrayList<>();
//        errorDetails.add("The URL is invalid: " + e.getHeaders().getHost() + e.getRequestURL());
//        return buildResponseEntity(RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, errorDetails);
//    }

//    @ExceptionHandler
//    public String constraintViolationHandler(ConstraintViolationException ex) {
//        return ex.getConstraintViolations().iterator().next()
//                .getMessage();
//    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
//        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//    }