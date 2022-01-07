package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class Highlighter extends AbsSprite2D
{
    public Highlighter()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setVisible(false);
        setActive(false);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        paint.setColor(Color.argb(25, 0, 0, 0));
        canvas.drawRect(getX(), getY(), getWidth(), getHeight(), paint);

        setVisible(false);
    }
}
