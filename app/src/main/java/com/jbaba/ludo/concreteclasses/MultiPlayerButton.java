package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class MultiPlayerButton extends AbsSprite2D
{
    public MultiPlayerButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX((int)MyConsts.multiPlayerButtonStartingX);
        setY((int)MyConsts.multiPlayerButtonStartingY);
        setWidth((int)MyConsts.multiPlayerButtonWidth);
        setHeight((int)MyConsts.multiPlayerButtonHeight);
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

        paint.setColor(Color.WHITE);
        canvas.drawRect(getX() - (getWidth() / 2), getY(), getX() + (getWidth()/ 2), getY() + getHeight(), paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("FRIENDS", getX() - (getWidth() / 2), getY() + getHeight() - 15, paint);
    }
}
