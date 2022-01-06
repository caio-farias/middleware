package application.registration.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR)"
            );
        }
    }
}
