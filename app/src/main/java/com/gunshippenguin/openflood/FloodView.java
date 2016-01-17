package com.gunshippenguin.openflood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * View that displays the game board to the user.
 */
public class FloodView extends View {
    private Game gameToDraw;
    private int boardSize;
    private Paint paints[];

    public FloodView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (boardSize != 0) {
            widthSize -= (widthSize % boardSize);
            heightSize = widthSize;
        }

        setMeasuredDimension(widthSize, heightSize);
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
        if (gameToDraw != null) {
            int cellSize = getWidth() / gameToDraw.getBoardDimensions();
            for (int y = 0; y < gameToDraw.getBoardDimensions(); y++) {
                for (int x = 0; x < gameToDraw.getBoardDimensions(); x++) {
                    c.drawRect(x * cellSize, y * cellSize,
                            (x + 1) * cellSize, (y + 1) * cellSize,
                            paints[gameToDraw.getColor(x, y)]);
                }
            }
        }
    }
}
