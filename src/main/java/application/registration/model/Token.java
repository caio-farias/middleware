package application.registration.model;

import lombok.Data;

@Data
public class Token {
    int accessToken;
    String username;
}