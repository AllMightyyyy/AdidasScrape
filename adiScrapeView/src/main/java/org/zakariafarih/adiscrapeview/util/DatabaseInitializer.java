package org.zakariafarih.adiscrapeview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "adiproducts_db";
    private static final String USER = "root";
    private static final String PASSWORD = "27122000@ziko";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create Database if not exists
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDatabase);

            // Use the newly created or existing database
            stmt.execute("USE " + DATABASE_NAME);

            // Create products table
            String createTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "url VARCHAR(255)," +
                    "image VARCHAR(255)," +
                    "rating DOUBLE," +
                    "badge_style VARCHAR(50)," +
                    "badge_text VARCHAR(100)," +
                    "model_number VARCHAR(50)," +
                    "sub_title VARCHAR(255)," +
                    "price DOUBLE," +
                    "sale_price DOUBLE," +
                    "discount_text VARCHAR(100)," +
                    "sold_out BOOLEAN," +
                    "colour_variations TEXT," +
                    "rating_count INT," +
                    "hover_image VARCHAR(255)," +
                    "origin_url VARCHAR(255)" +
                    ")";
            stmt.executeUpdate(createTable);

            System.out.println("Database and products table initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize the database.");
        }
    }

    public static void main(String[] args) {
        initializeDatabase(); // Call this once to set up the database
    }
}
