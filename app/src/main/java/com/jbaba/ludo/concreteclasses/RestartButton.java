package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class RestartButton extends AbsSprite2D
{
    public RestartButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX((int)MyConsts.replayButtonStartingX);
        setY((int)MyConsts.replayButtonStartingY);
        setWidth((int)MyConsts.replayButtonWidth);
        setHeight((int)MyConsts.replayButtonHeight);
        setVisible(true);
        setActive(false);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        paint.setTextSize(getHeight());
        float textSize = paint.measureText("RESTART");

        paint.setColor(Color.RED);
        canvas.drawRect(getX(), getY(), getX() + textSize + 20, getY() + getHeight() + 10, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("RESTART", getX() + 10, getY() + getHeight(), paint);
    }
}
