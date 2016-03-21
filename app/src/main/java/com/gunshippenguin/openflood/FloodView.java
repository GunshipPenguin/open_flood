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
    private int xOffset;
    private int yOffset;
    private Paint textPaint;
    private Paint paints[];

    public FloodView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setDrawingInfo();
        return;
    }

    private void setDrawingInfo(){
        int dimension;
        if (getWidth() > getHeight()) {
            dimension = getHeight();
        } else {
            dimension = getWidth();
        }

        dimension -= (dimension % boardSize);
        if (boardSize != 0) {
            cellSize = dimension / boardSize;
            xOffset = (getWidth() - dimension) / 2 ;
            yOffset = (getHeight() - dimension) / 2 ;
            textPaint.setTextSize(cellSize);
        }
        return;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        setDrawingInfo();
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
                c.drawRect(x * cellSize + xOffset, y * cellSize + yOffset,
                        (x + 1) * cellSize + xOffset, (y + 1) * cellSize + yOffset, paints[gameToDraw.getColor(x, y)]);
            }
        }

        // Draw numbers if color blind mode is on
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("color_blind_mode", false)) {
            String numToDraw;
            Rect currRect = new Rect();
            for (int y = 0; y < gameToDraw.getBoardDimensions(); y++) {
                for (int x = 0; x < gameToDraw.getBoardDimensions(); x++) {
                    currRect.set(x * cellSize + xOffset, y * cellSize + yOffset,
                            (x + 1) * cellSize + xOffset, (y + 1) * cellSize + yOffset);
                    numToDraw = Integer.toString(gameToDraw.getColor(x, y) + 1);
                    c.drawText(numToDraw, currRect.centerX(),
                            (int) (currRect.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2)),
                            textPaint);
                }
            }
        }
        return;
    }
}
