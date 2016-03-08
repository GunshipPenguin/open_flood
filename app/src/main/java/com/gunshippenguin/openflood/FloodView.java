package com.gunshippenguin.openflood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * View that displays the game board to the user.
 */
public class FloodView extends View {
    private Game gameToDraw;
    private int boardSize;
    private int cellSize;
    private Paint textPaint;
    private Paint paints[];

    public FloodView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int dimension = widthSize;
        if (boardSize != 0) {
            dimension -= (widthSize % boardSize);
        }

        cellSize = dimension / boardSize;
        textPaint.setTextSize(cellSize);
        setMeasuredDimension(dimension, dimension);
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        requestLayout();
        return;
    }

    public void drawGame(Game game) {
        gameToDraw = game;
        invalidate();
        return;
    }

    public void setPaints(Paint[] paints) {
        this.paints = paints;
        return;
    }

    @Override
    protected void onDraw(Canvas c) {
        if (gameToDraw == null) {
            return;
        }

        // Draw colors
        for (int y = 0; y < gameToDraw.getBoardDimensions(); y++) {
            for (int x = 0; x < gameToDraw.getBoardDimensions(); x++) {
                c.drawRect(x * cellSize, y * cellSize,
                        (x + 1) * cellSize, (y + 1) * cellSize, paints[gameToDraw.getColor(x, y)]);
            }
        }

        // Draw numbers if color blind mode is on
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("color_blind_mode", false)) {
            String numToDraw;
            Rect currRect = new Rect();
            for (int y = 0; y < gameToDraw.getBoardDimensions(); y++) {
                for (int x = 0; x < gameToDraw.getBoardDimensions(); x++) {
                    currRect.set(x * cellSize, y * cellSize,
                            (x + 1) * cellSize, (y + 1) * cellSize);
                    numToDraw = Integer.toString(gameToDraw.getColor(x, y) + 1);
                    c.drawText(numToDraw, currRect.centerX(),
                            (int) (y * cellSize + currRect.height() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2)),
                            textPaint);
                }
            }
        }
        return;
    }
}
