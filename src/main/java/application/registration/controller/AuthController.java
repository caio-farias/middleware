package application.registration.controller;

import application.registration.model.Token;
import application.registration.service.AuthService;
import middleware.annotations.Post;
import middleware.annotations.RequestMap;
import org.json.JSONObject;

@RequestMap(router = "/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController() { this.authService = new AuthService(); }

    @Post(router = "/login")
    public JSONObject login(JSONObject jsonObject){
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        JSONObject result = new JSONObject();

        Token token = authService.logar(username, password);
        if(token != null){
            result.put("username", token.getUsername());
            result.put("access_token", token.getAccessToken());
        }

        return result;
    }

}
