package com.gunshippenguin.openflood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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

    private String seed;
    private static final String SEED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    private static final int SEED_LENGTH_LOWER = 5;
    private static final int SEED_LENGTH_UPPER = 15;

    public Game(int boardSize, int numColors) {
        // Initialize board
        this.boardSize = boardSize;
        this.numColors = numColors;
        this.seed = generateRandomSeed();
        initBoard();

        maxSteps = (int) 30 * (boardSize * numColors) / (17 * 6);
    }

    public Game(int boardSize, int numColors, String seed) {
        // Initialize board
        this.boardSize = boardSize;
        this.numColors = numColors;
        this.seed = seed;
        initBoard();

        maxSteps = (int) 30 * (boardSize * numColors) / (17 * 6);
    }

    private String generateRandomSeed() {
        Random rand = new Random(System.currentTimeMillis());
        String currSeed = "";
        for(int i=0;i<rand.nextInt((SEED_LENGTH_UPPER-SEED_LENGTH_LOWER)+1)+SEED_LENGTH_LOWER;i++) {
            currSeed += SEED_CHARS.charAt(rand.nextInt(SEED_CHARS.length()));
        }
        return currSeed;
    }

    public int getColor(int x, int y) {
        return board[y][x];
    }

    public int getBoardDimensions() {
        return boardSize;
    }

    public String getSeed() {
        return seed;
    }

    public int getSteps() {
        return steps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    private void initBoard() {
        board = new int[boardSize][boardSize];
        Random r = new Random(seed.hashCode());
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = r.nextInt(numColors);
            }
        }
        return;
    }

    public void flood(int replacementColor) {
        int targetColor = board[0][0];
        if (targetColor == replacementColor) {
            return;
        }

        Queue<BoardPoint> queue = new LinkedList<BoardPoint>();
        ArrayList<BoardPoint> processed = new ArrayList<BoardPoint>();

        queue.add(new BoardPoint(0, 0));

        BoardPoint currPoint;
        while (!queue.isEmpty()) {
            currPoint = queue.remove();
            if (board[currPoint.getY()][currPoint.getX()] == targetColor) {
                board[currPoint.getY()][currPoint.getX()] = replacementColor;
                if (currPoint.getX() != 0 &&
                        !processed.contains(new BoardPoint(currPoint.getX() - 1, currPoint.getY()))) {
                    queue.add(new BoardPoint(currPoint.getX() - 1, currPoint.getY()));
                }
                if (currPoint.getX() != boardSize - 1 &&
                        !processed.contains(new BoardPoint(currPoint.getX() + 1, currPoint.getY()))) {
                    queue.add(new BoardPoint(currPoint.getX() + 1, currPoint.getY()));
                }
                if (currPoint.getY() != 0 &&
                        !processed.contains(new BoardPoint(currPoint.getX(), currPoint.getY() - 1))) {
                    queue.add(new BoardPoint(currPoint.getX(), currPoint.getY() - 1));
                }
                if (currPoint.getY() != boardSize - 1 &&
                        !processed.contains(new BoardPoint(currPoint.getX(), currPoint.getY() + 1))) {
                    queue.add(new BoardPoint(currPoint.getX(), currPoint.getY() + 1));
                }
            }
        }
        steps++;
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

    private class BoardPoint {
        int x, y;

        public BoardPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!BoardPoint.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            BoardPoint bp = (BoardPoint) obj;
            return (this.x == bp.getX()) && (this.y == bp.getY());
        }
    }
}
