package com.game.database;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:game.db";

    public static void initializeDatabase(){
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                high_score INTEGER DEFAULT 0,
                current_level INTEGER DEFAULT 1,
                bg_music INTEGER DEFAULT 1,
                shot_sound INTEGER DEFAULT 1,
                crash_sound INTEGER DEFAULT 1,
                game_over_sound INTEGER DEFAULT 1
            );
            """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean register(String username, String password) {
        if (userExists(username)) {
            return false;
        }

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setString(1, username);
             ResultSet rs = pstmt.executeQuery();
             return rs.next();
        } catch (SQLException e) {
        System.out.println(e.getMessage());
        }
    return false;
    }

    public static void updateSoundSetting(String username, String settingColumn, boolean status) {
        String sql = "UPDATE users SET " + settingColumn + " = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, status ? 1 : 0);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
