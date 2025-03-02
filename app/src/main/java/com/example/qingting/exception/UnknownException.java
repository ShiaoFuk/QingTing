package com.example.qingting.exception;

public class UnknownException extends Exception {
    public UnknownException(Class<?> throwsClass) {
        super("UnknownException from class: " + throwsClass.getName());
    }

    public UnknownException(Class<?> throwsClass, String appendMessage) {
        super("UnknownException from class: " + throwsClass.getName() + " append message: " + appendMessage);
    }
}
