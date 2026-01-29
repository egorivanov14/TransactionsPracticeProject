package org.example.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String massage){
        super(massage);
    }
}
