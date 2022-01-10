package application.registration.service;

import application.registration.model.Token;
import application.registration.repository.AuthRepository;
import application.registration.repository.SQLiteJDBCDriverConnection;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final SQLiteJDBCDriverConnection applicationUserRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
        this.applicationUserRepository = new SQLiteJDBCDriverConnection();
    }

    public Token logar(String username, String password){
        try {
            if(applicationUserRepository.logarUser(username, password) == null)
                return null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }

        try {
            Token token = new Token();
            token.setUsername(username);

            return authRepository.createToken(token);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

}
