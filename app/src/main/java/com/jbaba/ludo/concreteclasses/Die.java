package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jbaba.ludo.R;
import com.jbaba.ludo.abstractclasses.AbsSpriteImg;

public class Die extends AbsSpriteImg
{
    private transient Bitmap[] dieFaces;
    private int dieFaceValue;
    private String dieName;

    public Die()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setVisible(true);
        setActive(false);
        setDx(MyConsts.dieSpeed);
        setDy(MyConsts.dieSpeed);
        initDieFaces();
        setDieFaceValue(6);
    }

    private void initDieFaces()
    {
        int[] imageID = {R.drawable.face1, R.drawable.face2, R.drawable.face3,
                R.drawable.face4, R.drawable.face5, R.drawable.face6};
        dieFaces = new Bitmap[6];
        for(int i = 0; i < 6; i++)
        {
            dieFaces[i] = BitmapFactory.decodeResource(MyConsts.context.getResources(), imageID[i]);
            dieFaces[i] = Bitmap.createScaledBitmap(dieFaces[i], MyConsts.diceLength, MyConsts.diceLength, false);
        }
    }

    @Override
    public void updateSprite()
    {
        setX(getX() + getDx());
        setY(getY() + getDy());

        if(getX() <= (MyConsts.boardStartingX + MyConsts.roadSegmentLength) ||
                getX() >= (MyConsts.boardEndingX - MyConsts.roadSegmentLength) ||
                getY() <= (MyConsts.boardStartingY + MyConsts.roadSegmentLength) ||
                getY() >= (MyConsts.boardHeight - MyConsts.roadSegmentLength))
            setActive(false);
    }

    public void initDxDy()
    {
        int sign = (int) (Math.random() * 2);
        if(sign == 1)
        {
            setDx(-getDx());
            setDy(-getDy());
        }
    }

    public void getRollFaceValue()
    {
        int value = (int) (Math.random() * 6) + 1;
        setDieFaceValue(value);
    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        if(!isActive())
        {
            int toDraw = getDieFaceValue();
            canvas.drawBitmap(dieFaces[toDraw - 1], getX(), getY(), paint);
        }
        else
        {
            int[] id = {R.drawable.style1, R.drawable.style2, R.drawable.style3, R.drawable.style4,
                    R.drawable.style5, R.drawable.style6, R.drawable.style7};
            int rand = (int) (Math.random() * 7);
            Bitmap img = BitmapFactory.decodeResource(MyConsts.context.getResources(), id[rand]);
            img = Bitmap.createScaledBitmap(img, MyConsts.diceLength, MyConsts.diceLength, false);

            canvas.drawBitmap(img, getX(), getY(), paint);
        }
    }

    public void rollDie()
    {
        initDxDy();
        setActive(true);
    }

    public Bitmap[] getDieFaces() {
        return dieFaces;
    }

    public void setDieFaces(Bitmap[] dieFaces) {
        this.dieFaces = dieFaces;
    }

    public int getDieFaceValue() {
        return dieFaceValue;
    }

    public void setDieFaceValue(int dieFaceValue) {
        this.dieFaceValue = dieFaceValue;
    }

    public String getDieName() {
        return dieName;
    }

    public void setDieName(String dieName) {
        this.dieName = dieName;
    }
}
