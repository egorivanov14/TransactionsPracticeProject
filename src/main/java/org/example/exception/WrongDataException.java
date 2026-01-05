package org.example.exception;

public class WrongDataException extends RuntimeException{
    public WrongDataException(String message){
        super(message);
    }
}
