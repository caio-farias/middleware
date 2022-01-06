package application.registration.service;

import application.registration.repository.AuthRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService() { this.authRepository = new AuthRepository(); }
}
