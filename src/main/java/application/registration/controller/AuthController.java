package application.registration.controller;

import application.registration.service.AuthService;
import middleware.annotations.RequestMap;

@RequestMap(router = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }
}
