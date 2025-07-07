package com.techlab.api.handlers;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NoHandlerFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<TokenizedData<?>> handleNoHandlerFoundException() {
         return ResponseEntity.status(HttpStatus.NOT_FOUND)
                 .body(new TokenizedData<>(TokenConstants.PATH_ERROR));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<TokenizedData<?>> handleHttpRequestMethodNotSupportedException() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new TokenizedData<>(TokenConstants.METHOD_ERROR));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<TokenizedData<?>> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenizedData<>(TokenConstants.PAYLOAD_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorData<?>> handleUnexpectedTypeException(
            HttpServletRequest req,
            MethodArgumentNotValidException ex) {
        ErrorData<?> errorData = new ErrorData<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errorData.addError(error.getField(), new TokenizedData<>(error.getDefaultMessage()));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorData);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorData<?>> handleApiException(HttpServletRequest req, ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorData());
    }
}
