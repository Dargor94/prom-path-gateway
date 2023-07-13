package org.dargor.gateway.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> argsNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        var error = new ErrorResponse(String.format("%s: %s", ErrorDefinition.INVALID_FIELDS.getMessage(), errors), HttpStatus.BAD_REQUEST.value());
        log.error(String.format("Exception found with code %s for field validation didn't passed.", error.getCode()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class})
    public final ResponseEntity<ErrorResponse> accessDenied(Exception e) {
        var error = new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        log.error(String.format("Exception found with code %d.", error.getCode()));
        return new ResponseEntity<>(error, null, error.getCode());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> genericError(Exception e) {
        var error = new ErrorResponse(ErrorDefinition.UNKNOWN_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error(String.format("Exception found with code %d.", error.getCode()));
        return new ResponseEntity<>(error, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
