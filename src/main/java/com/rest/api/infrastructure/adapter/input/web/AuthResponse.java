package com.rest.api.infrastructure.adapter.input.web;

public record AuthResponse(String token, String username, long expiresIn) {}
