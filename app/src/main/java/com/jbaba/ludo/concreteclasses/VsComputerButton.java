package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class VsComputerButton extends AbsSprite2D
{
    public VsComputerButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX((int)MyConsts.vsComputerButtonStartingX);
        setY((int)MyConsts.vsComputerButtonStartingY);
        setWidth((int)MyConsts.vsComputerButtonWidth);
        setHeight((int)MyConsts.vsComputerButtonHeight);
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
        canvas.drawRect(getX() - (getWidth() / 2), getY(), getX() + (getWidth() / 2), getY() + getHeight(), paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("COMPUTER", getX() - (getWidth() / 2), getY() + getHeight() - 15, paint);
    }
}
