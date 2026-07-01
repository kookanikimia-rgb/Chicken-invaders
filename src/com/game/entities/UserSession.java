package com.game.entities;

public class UserSession {

    public static String currentUsername = null;

    public static void setUser(String username) {
        currentUsername = username;
    }

    public static String getUserName(){return currentUsername;}

    public static void clearSession() {
        currentUsername = null;
    }
}
