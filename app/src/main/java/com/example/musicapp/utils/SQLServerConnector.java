package com.example.musicapp.utils;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLServerConnector {

    private static final String URL = "jdbc:sqlserver://<PHUNG_DAT>:<1433>;databaseName=<AppMusic>";
    private static final String USER = "<sa>";
    private static final String PASSWORD = "<161003>";

    public static Connection connect() {
        Connection connection = null;
        try {
            // Allow network operation on main thread (not recommended for production)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Load the JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection failed.");
        }
        return connection;
    }

    public static String checkConnection() {
        Connection connection = connect();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT 1");

                if (resultSet.next()) {
                    return "SQL Server connection is successful.";
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "Failed to establish connection.";
        }
        return "Failed to establish connection.";
    }
}
