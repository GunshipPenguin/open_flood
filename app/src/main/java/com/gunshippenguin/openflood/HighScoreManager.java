package com.gunshippenguin.openflood;

import android.content.SharedPreferences;

public class HighScoreManager {
    SharedPreferences sp;

    public HighScoreManager(SharedPreferences sp) {
        this.sp = sp;
    }

    public boolean isHighScore(int boardSize, int numColors, int steps) {
        if (!highScoreExists(boardSize, numColors)) {
            return true;
        } else {
            return sp.getInt(getKey(boardSize, numColors), -1) > steps;
        }
    }

    public boolean highScoreExists(int boardSize, int numColors) {
        return sp.contains(getKey(boardSize, numColors));
    }

    public int getHighScore(int boardSize, int numColors) {
        return sp.getInt(getKey(boardSize, numColors), -1);
    }

    public void setHighScore(int boardSize, int numColors, int steps) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(getKey(boardSize, numColors), steps);
        editor.apply();
    }

    public void removeHighScore(int boardSize, int numColors) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getKey(boardSize, numColors));
        editor.apply();
        return;
    }

    private String getKey(int boardSize, int numColors) {
        return String.format("highscore_%1$d_%2$d", boardSize, numColors);
    }
}
