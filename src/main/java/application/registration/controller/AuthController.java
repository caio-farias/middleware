package application.registration.controller;

import application.registration.service.AuthService;
import middleware.annotations.Post;
import middleware.annotations.RequestMap;
import org.json.JSONObject;

@RequestMap(router = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController() { this.authService = new AuthService(); }

    @Post(router = "/login")
    public Integer login(JSONObject jsonObject){
        return 12;
    }

}
