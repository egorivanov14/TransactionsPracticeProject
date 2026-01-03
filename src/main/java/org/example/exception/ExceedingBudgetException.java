package org.example.exception;

public class ExceedingBudgetException extends RuntimeException {

    public ExceedingBudgetException(String message){
        super(message);
    }
}
