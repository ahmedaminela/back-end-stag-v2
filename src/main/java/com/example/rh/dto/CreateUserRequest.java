package com.example.rh.dto;

public record CreateUserRequest(String username, String password,String firstName,String lastName,String phoneNumber, String email, String role) {
}
