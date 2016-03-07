package com.gunshippenguin.openflood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class ColorButton extends View {

    Drawable buttonDrawable;
    public ColorButton(Context context) {
        super(context);
        buttonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.button);
    }

    public void setColor(int color) {
        buttonDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return;
    }

    @Override
    public void onDraw(Canvas c) {
        buttonDrawable.setBounds(getPaddingLeft(), getPaddingTop(),
                c.getWidth() - getPaddingRight(),
                c.getHeight() - getPaddingBottom());
        buttonDrawable.draw(c);
    }
}
