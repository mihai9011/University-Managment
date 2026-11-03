package com.example.UniversityMan.service;

public interface ValidatableService {
    default boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.trim().matches(emailRegex);
    }

    boolean isEmailTaken(String email, String ownId);
}