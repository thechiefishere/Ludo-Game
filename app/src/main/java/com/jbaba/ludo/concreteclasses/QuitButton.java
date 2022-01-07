package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class QuitButton extends AbsSprite2D
{
    public QuitButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX((int)MyConsts.quitButtonStartingX);
        setY((int)MyConsts.quitButtonStartingY);
        setWidth((int)MyConsts.quitButtonWidth);
        setHeight((int)MyConsts.quitButtonHeight);
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
        float textSize2 = paint.measureText("QUIT");
        float textSizeDiffHalf = (textSize - textSize2) / 2;

        paint.setColor(Color.RED);
        canvas.drawRect(getX(), getY(), getX() + textSize + 20, getY() + getHeight() + 10, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("QUIT", getX() + textSizeDiffHalf + 10, getY() + getHeight(), paint);
    }
}
