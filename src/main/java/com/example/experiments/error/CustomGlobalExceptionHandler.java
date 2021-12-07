package com.example.experiments.error;

import com.example.experiments.error.exceptions.UserApiException;
import com.example.experiments.error.exceptions.UserApiNotFoundException;
import com.example.experiments.error.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

// NOTE: ControllerAdvice handles exceptions across the whole application in one global handling component
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // NOTE: render custom error page with custom error response
    @ExceptionHandler({UserNotFoundException.class})
    public String customHandleNotFound(Exception ex, Model model) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setError(HttpStatus.NOT_FOUND.toString());
        errors.setMessage(ex.getMessage());
        model.addAttribute("errors", errors);
        return "error";
    }

    // NOTE: API returns custom JSON error response, and overrides status code
    @ExceptionHandler({UserApiNotFoundException.class})
    public ResponseEntity<CustomErrorResponse> userApiNotFound(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.NOT_FOUND.value());
        errors.setError(HttpStatus.NOT_FOUND.toString());
        errors.setMessage(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserApiException.class})
    public ResponseEntity<CustomErrorResponse> userApiInternalError(Exception ex) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errors.setMessage(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
