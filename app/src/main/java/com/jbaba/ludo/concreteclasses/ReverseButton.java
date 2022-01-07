package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.R;
import com.jbaba.ludo.abstractclasses.AbsSpriteImg;

public class ReverseButton extends AbsSpriteImg
{
    public ReverseButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX(MyConsts.reverseButtonX);
        setY(MyConsts.reverseButtonY);
        setWidth(MyConsts.reverseButtonWidth);
        setHeight(MyConsts.reverseButtonHeight);
        setActive(false);
        setVisible(true);

        Bitmap reverse = BitmapFactory.decodeResource(MyConsts.context.getResources(), R.drawable.reverse);
        reverse = Bitmap.createScaledBitmap(reverse, MyConsts.reverseButtonWidth, MyConsts.reverseButtonHeight, false);
        setBitmap(reverse);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        canvas.drawBitmap(getBitmap(), getX(), getY(), paint);
    }
}
