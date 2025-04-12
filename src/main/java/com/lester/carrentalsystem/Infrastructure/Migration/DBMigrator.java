package com.lester.carrentalsystem.Infrastructure.Migration;

import com.lester.carrentalsystem.Infrastructure.Data.DBConnection;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMigrator extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DbQuery();
    }

    public void DbQuery() {
        String dbQuery =
            """
            
            """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(dbQuery);
            System.out.println("Query executed successfully!");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
