package application.registration.repository;

import application.registration.model.Token;
import java.sql.*;

public class AuthRepository {
    private Connection connection = null;

    public AuthRepository(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:banco.db");
            this.createDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void createDatabase() throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " +
                    "token(" +
                    "access_token INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username VARCHAR"+
                    ")"
            );
        }
    }

    public Token createToken(Token token) throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO token(username) VALUES (" +
                    "'" + token.getUsername() + "')";
            statement.execute(sql);

            sql = "SELECT access_token FROM token WHERE username = " + token.getUsername();
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            token.setAccessToken(resultSet.getInt("access_token"));

            return token;
        }
        return null;
    }

    public boolean existToken(Integer accessToken) throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            String sql = "SELECT username FROM token WHERE access_token = " + accessToken;
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.getString("username") != null;
        }
        return false;
    }

}
