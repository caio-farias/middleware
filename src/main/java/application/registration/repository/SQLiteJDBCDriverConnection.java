package application.registration.repository;

import application.registration.model.ApplicationUser;
import application.registration.model.Token;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
public class SQLiteJDBCDriverConnection {
    private Connection connection = null;

    private static SQLiteJDBCDriverConnection uniqueInstance;

    public static synchronized SQLiteJDBCDriverConnection getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new SQLiteJDBCDriverConnection();

        return uniqueInstance;
    }

    private SQLiteJDBCDriverConnection(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:banco.db");
            this.createDatabase();
        } catch (SQLException throwables) {
            log.error("Error to stabilise a connection with database banco.db");
            throwables.printStackTrace();
        }
    }


    private void createDatabase() throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " +
                    "application_user(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username VARCHAR, " +
                        "email VARCHAR, " +
                        "password VARCHAR)"
            );
            statement.execute("CREATE TABLE IF NOT EXISTS " +
                    "token(" +
                    "access_token INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username VARCHAR)"
            );
        }
    }

    public Integer insertApplicationUser(ApplicationUser applicationUser) throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO application_user(username, email, password) VALUES (" +
                    "'" + applicationUser.getUsername() + "', " +
                    "'" + applicationUser.getEmail() + "', " +
                    "'" + applicationUser.getPassword() + "')";
            statement.execute(sql);

            sql = "SELECT id FROM application_user WHERE email = " + applicationUser.getEmail();
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.getInt("id");
        }
        return null;
    }

    public ApplicationUser getUserByID(Integer integer) throws SQLException {
        ApplicationUser applicationUser = new ApplicationUser();
        if (this.connection != null){
            String sql = "SELECT * FROM application_user WHERE id = " + integer.toString();
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            applicationUser.setId(resultSet.getInt("id"));
            applicationUser.setUsername(resultSet.getString("username"));
            applicationUser.setEmail(resultSet.getString("email"));
            applicationUser.setPassword(resultSet.getString("password"));
        }
        return applicationUser;
    }

    public ArrayList<ApplicationUser> listAllUsers() throws SQLException {
        ArrayList<ApplicationUser> users = new ArrayList<>();
        if (this.connection != null){
            String sql = "SELECT * FROM application_user";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                var applicationUser = new ApplicationUser();
                applicationUser.setId(resultSet.getInt("id"));
                applicationUser.setUsername(resultSet.getString("username"));
                applicationUser.setEmail(resultSet.getString("email"));
                applicationUser.setPassword(resultSet.getString("password"));
                users.add(applicationUser);
            }
        }
        return users;
    }

    public ApplicationUser logarUser(String username, String password) throws SQLException {
        if (this.connection != null){
            ApplicationUser applicationUser = new ApplicationUser();

            String sql = "SELECT * FROM application_user WHERE username = '" + username.toString() + "' AND password = '" + password + "'";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            applicationUser.setId(resultSet.getInt("id"));
            applicationUser.setUsername(resultSet.getString("username"));
            applicationUser.setEmail(resultSet.getString("email"));
            applicationUser.setPassword(resultSet.getString("password"));

            return applicationUser;
        }
        return null;
    }


    //Auth
    public Token createToken(Token token) throws SQLException {
        if (this.connection != null){
            Statement statement = this.connection.createStatement();
            String sql = "INSERT INTO token(username) VALUES (" +
                    "'" + token.getUsername() + "')";
            statement.execute(sql);

            sql = "SELECT access_token FROM token WHERE username = '" + token.getUsername()+"'";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            token.setAccessToken(resultSet.getInt("access_token"));

            return token;
        }
        return null;
    }

    public boolean existToken(Integer accessToken) throws SQLException {
        if (this.connection != null){
            String sql = "SELECT * FROM token WHERE access_token = " + accessToken.toString();
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next();
        }
        return false;
    }

}
