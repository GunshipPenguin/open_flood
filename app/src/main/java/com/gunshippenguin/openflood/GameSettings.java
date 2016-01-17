package com.gunshippenguin.openflood;

/**
 * Class to store Game Settings.
 */
public class GameSettings {
    private int boardSize;
    private int numColors;

    public GameSettings(int boardSize, int numColors) {
        this.boardSize = boardSize;
        this.numColors = numColors;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getNumColors() {
        return numColors;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        return;
    }

    public void setNumColors(int numColors) {
        this.numColors = numColors;
        return;
    }
}
