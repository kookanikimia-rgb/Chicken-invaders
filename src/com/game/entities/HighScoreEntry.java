package com.game.entities;

public class HighScoreEntry {

    private final String username;
    private final int score;
    private final int level;

    public HighScoreEntry(String username, int score, int level) {
        this.username = username;
        this.score = score;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }
}