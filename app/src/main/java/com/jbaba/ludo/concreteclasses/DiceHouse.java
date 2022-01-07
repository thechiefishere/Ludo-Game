package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class DiceHouse extends AbsSprite2D
{
    public DiceHouse()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX(MyConsts.boardStartingX + MyConsts.houseLength);
        setY(MyConsts.boardStartingY + MyConsts.houseLength);
        setWidth(MyConsts.diceHouseLength);
        setHeight(MyConsts.diceHouseLength);
        setColor(Color.CYAN);
        setActive(false);
        setVisible(true);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        paint.setColor(getColor());
        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(MyConsts.boardStartingX + MyConsts.houseLength,
                MyConsts.boardStartingY + MyConsts.houseLength, MyConsts.boardEndingX - MyConsts.houseLength,
                MyConsts.boardHeight - MyConsts.houseLength, paint);
        canvas.drawLine(MyConsts.boardStartingX + MyConsts.houseLength,
                MyConsts.boardHeight - MyConsts.houseLength, MyConsts.boardEndingX - MyConsts.houseLength,
                MyConsts.boardStartingY + MyConsts.houseLength, paint);
    }
}
