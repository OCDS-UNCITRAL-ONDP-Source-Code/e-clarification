package com.procurement.clarification.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.procurement.clarification.exception.ErrorInsertException;
import com.procurement.clarification.exception.ValidationException;
import com.procurement.clarification.model.dto.response.ErrorInsertResponse;
import com.procurement.clarification.model.dto.response.MappingErrorResponse;
import com.procurement.clarification.model.dto.response.ValidationErrorResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ValidationErrorResponse handleValidationContractProcessPeriod(
        final ValidationException e) {
        String message = "Houston we have a problem";
        return new ValidationErrorResponse(
            message,
            e.getErrors().getFieldErrors().stream()
             .map(f -> new ValidationErrorResponse.ErrorPoint(
                 f.getField(),
                 f.getDefaultMessage(),
                 f.getCode()))
             .collect(Collectors.toList()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonMappingException.class)
    public MappingErrorResponse handleJsonMappingExceptionException(final JsonMappingException e) {
        String message = "Houston we have a problem";
        return new MappingErrorResponse(message, e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErrorInsertException.class)
    public ErrorInsertResponse handleErrorInsertException(final ErrorInsertException e) {
        return new ErrorInsertResponse(e.getMessage());
    }

}
