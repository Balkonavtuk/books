package com.example.book_catalog.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}