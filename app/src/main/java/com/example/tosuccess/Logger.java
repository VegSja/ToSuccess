package com.example.tosuccess;

public class Logger {

    public void errorMessage(String message){
        System.out.println("[ERROR]: " + message);
    }
    public void loggerMessage(String message){
        System.out.println("[LOGGER]: " + message);
    }
    public void successMessage(String message){
        System.out.println("[SUCCESS]: " + message);
    }
    public void statusMessage(String message){
        System.out.println("[STATUS]: " + message);
    }
}
