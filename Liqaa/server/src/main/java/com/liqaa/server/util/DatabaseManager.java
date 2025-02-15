package com.liqaa.server.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("datasource.properties")) {
            if (input == null) {
                System.out.println("datasource.properties not found");
                throw new RuntimeException("Missing datasource.properties file");
            }

            Properties prop = new Properties();
            prop.load(input);

            // Ensure we load the correct properties
            String jdbcUrl = prop.getProperty("dataSource.url");
            String username = prop.getProperty("dataSource.user");
            String password = prop.getProperty("dataSource.password");

            // Validate properties before proceeding
            if (jdbcUrl == null || username == null || password == null) {
                throw new IllegalArgumentException("Missing required database properties: jdbcUrl, username, or password.");
            }

            // Setup the HikariConfig
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);

            // Additional configuration options (optional)
            config.setMaximumPoolSize(10); // Set the connection pool size
            config.setConnectionTimeout(30000); // Set the connection timeout

            // Create the DataSource
            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            System.err.println("Error loading datasource.properties: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load database properties", e);
        } catch (IllegalArgumentException e) {
            System.err.println("Configuration error: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Rethrow to ensure the program fails gracefully
        } catch (RuntimeException e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Rethrow to terminate the application
        }
    }

    // Method to get a connection from the DataSource
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized.");
        }
        return dataSource.getConnection();
    }
}
