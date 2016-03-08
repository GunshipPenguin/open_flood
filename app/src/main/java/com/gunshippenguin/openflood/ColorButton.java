package com.gunshippenguin.openflood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class ColorButton extends View {
    String text;
    Paint textPaint;
    Drawable buttonDrawable;


    public ColorButton(Context context) {
        super(context);
        buttonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.button);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setColor(int color) {
        buttonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        textPaint.setTextSize(getHeight());
    }

    @Override
    public void onDraw(Canvas c) {
        buttonDrawable.setBounds(getPaddingLeft(), getPaddingTop(),
                c.getWidth() - getPaddingRight(),
                c.getHeight() - getPaddingBottom());
        buttonDrawable.draw(c);

        boolean colorBlindMode = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getBoolean("color_blind_mode", false);
        if (colorBlindMode && text != null) {
            c.drawText(text, getWidth() / 2,
                    (int) (getHeight() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2)),
                    textPaint);
        }
    }
}
