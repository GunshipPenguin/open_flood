package com.gunshippenguin.openflood;

import java.util.Random;

/**
 * Class representing a game in progress.
 */
public class Game {
    private int board[][];
    private int boardSize;

    private int numColors;

    private int steps = 0;
    private int maxSteps;

    public Game(int boardSize, int numColors) {
        // Initialize board
        this.boardSize = boardSize;
        this.numColors = numColors;
        initBoard();

        maxSteps = (int) 30 * (boardSize * numColors) / (17 * 6);
    }

    public int getColor(int x, int y) {
        return board[y][x];
    }

    public void setColor(int x, int y, int color) {
        assert (color < numColors);
        board[y][x] = color;
        return;
    }

    public int getBoardDimensions() {
        return boardSize;
    }

    public int getSteps() {
        return steps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    private void initBoard() {
        board = new int[boardSize][boardSize];
        Random r = new Random(System.currentTimeMillis());
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = r.nextInt(numColors);
            }
        }
        return;
    }

    private void floodFill(int x, int y, int targetColor, int replacementColor) {
        // See if we have a valid position
        int currCellColor;
        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            currCellColor = board[y][x];
        } else {
            return;
        }

        // Set the color for this cell
        if (targetColor == replacementColor) {
            return;
        } else if (currCellColor != targetColor) {
            return;
        }
        board[y][x] = replacementColor;

        // Continue with recursion
        floodFill(x + 1, y, targetColor, replacementColor);
        floodFill(x - 1, y, targetColor, replacementColor);
        floodFill(x, y + 1, targetColor, replacementColor);
        floodFill(x, y - 1, targetColor, replacementColor);
        return;
    }

    public boolean checkWin() {
        int lastColor = board[0][0];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (lastColor != board[y][x]) {
                    return false;
                }
                lastColor = board[y][x];
            }
        }
        return true;
    }

    public void flood(int color) {
        floodFill(0, 0, board[0][0], color);
        steps++;
        return;
    }
}
