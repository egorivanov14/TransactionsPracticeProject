package org.example.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHolder {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400,
                        "Invalid input data",
                        "Validation Failed",
                        LocalDateTime.now(),
                        errors));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(404,
                        exception.getMessage(),
                        "Not Found",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403,
                        exception.getMessage(),
                        "Access Denied",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409,
                        exception.getMessage(),
                        "Duplicate Resource",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(ExceedingBudgetException.class)
    public ResponseEntity<ErrorResponse> handleExceeding(ExceedingBudgetException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400,
                        exception.getMessage(),
                        "Budget Exceeded",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<ErrorResponse> handleWrongData(WrongDataException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400,
                        exception.getMessage(),
                        "Invalid Data",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400,
                        "Wrong type for field '" + exception.getPropertyName() + "'",
                        "Type Mismatch",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception exception) {
        return ResponseEntity.status(500)
                .body(new ErrorResponse(500,
                        "Internal server error",
                        "Server Error",
                        LocalDateTime.now(),
                        null));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenExceptions(InvalidRefreshTokenException exception){
        return ResponseEntity.status(401)
                .body(new ErrorResponse(
                        401,
                        exception.getMessage(),
                        "Unauthorized",
                        LocalDateTime.now(),
                        null
                ));
    }
}