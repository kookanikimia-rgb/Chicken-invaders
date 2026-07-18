package com.game.database;

import com.game.entities.PlaneInfo;

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
                game_over_sound INTEGER DEFAULT 1,
                selected_plane TEXT DEFAULT 'DEFAULT'    
            );
            """;

        String createGameHistoryTable = """
            CREATE TABLE IF NOT EXISTS game_history(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                score INTEGER NOT NULL,
                level INTEGER NOT NULL,
                played_at TEXT NOT NULL,
                bg_music INTEGER NOT NULL,
                shot_sound INTEGER NOT NULL,
                crash_sound INTEGER NOT NULL,
                game_over_sound INTEGER NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            try {
                stmt.execute("""
                     ALTER TABLE users
                     ADD COLUMN selected_plane TEXT DEFAULT 'DEFAULT'
            """);
            } catch (SQLException ignored) {
            }
            stmt.execute(createGameHistoryTable);
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

    public static boolean getSoundSetting(String userName, String settingName) {
        boolean value = true;

        String sql = "SELECT " + settingName + " FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                value = rs.getInt(settingName) == 1;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting setting " + settingName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return value;
    }

    public static void updateUserStats(String username, int score, int level) {

        String sql = """
         UPDATE users
                    SET
                        high_score = MAX(high_score, ?),
                        current_level = CASE
                                WHEN ? > high_score THEN ?
                                ELSE current_level
                        END
                    WHERE username = ?
        """;

        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, score);      // برای MAX
            pstmt.setInt(2, score);      // برای شرط WHEN
            pstmt.setInt(3, level);      // لولی که باید ذخیره شود
            pstmt.setString(4, username);

            pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void saveGame(String username,
                                int score,
                                int level,
                                boolean bgMusic,
                                boolean shotSound,
                                boolean crashSound,
                                boolean gameOverSound) {

        String sql = """
        INSERT INTO game_history
            (username,score,level,played_at,
             bg_music,shot_sound,crash_sound,game_over_sound)
        VALUES(?,?,?,?,?,?,?,?)
        """;

        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1,username);
            pstmt.setInt(2,score);
            pstmt.setInt(3,level);

            pstmt.setString(4,
                    java.time.LocalDateTime.now().toString());

            pstmt.setInt(5,bgMusic ? 1 : 0);
            pstmt.setInt(6,shotSound ? 1 : 0);
            pstmt.setInt(7,crashSound ? 1 : 0);
            pstmt.setInt(8,gameOverSound ? 1 : 0);

            pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static ResultSet getHighScores() {

        String sql = """
        SELECT u.username,
               MAX(g.score) AS bestScore,
               u.current_level
        FROM game_history g
        JOIN users u ON g.username = u.username
        GROUP BY u.username
        ORDER BY bestScore DESC
        """;

        try{

            Connection conn = DriverManager.getConnection(URL);

            PreparedStatement pstmt = conn.prepareStatement(sql);

            return pstmt.executeQuery();

        }catch(SQLException e){

            e.printStackTrace();
        }

        return null;
    }

    public static String getSelectedPlane(String username) {

        String sql = """
        SELECT selected_plane
        FROM users
        WHERE username = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("selected_plane");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "DEFAULT";
    }

    public static void updateSelectedPlane(String username, String plane) {

        String sql = """
        UPDATE users
        SET selected_plane = ?
        WHERE username = ?
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plane);
            pstmt.setString(2, username);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlaneInfo getPlaneInfo(String planeName) {

        switch (planeName) {

            case "FAST":
                return new PlaneInfo("FAST",5000,7,250,3,false);

            case "HEAVY":
                return new PlaneInfo("HEAVY",8000,4,200,5,false);

            case "SNIPER":
                return new PlaneInfo("SNIPER",10000,5,150,3,true);

            default:
                return new PlaneInfo("DEFAULT",0,5,300,3,false);
        }
    }

    public static int getHighScore(String username) {

        String sql = "SELECT high_score FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("high_score");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
