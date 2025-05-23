package com.ritk.contact.exception;

import com.ritk.contact.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException ex) {
        ApiResponse<String> response = new ApiResponse<>("Invalid phone number or password", false, null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
    }

    // 🧩 1. Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ApiResponse<Map<String, String>> response = new ApiResponse<>("Validation failed", false, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    // 🧩 2. Custom exception: Email already exists
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), false, null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    // 🧩 3. Custom exception: Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), false, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    // 🧩 4. Catch-all: Runtime errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntime(RuntimeException ex) {
        ApiResponse<String> response = new ApiResponse<>("Internal Server Error: " + ex.getMessage(), false, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }



    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }



}
