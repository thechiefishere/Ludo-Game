package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite;

public class Dice extends AbsSprite
{
    private Die[] dice;
    private boolean rollStart;

    public Dice()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        initDice();
        setVisible(true);
        setActive(false);
        setRollStart(false);
    }

    private void initDice()
    {
        dice = new Die[2];
        dice[0] = new Die();
        dice[1] = new Die();

        resetDiceLocation();
    }

    private void resetDiceLocation()
    {
        dice[0].setX(MyConsts.die1X);
        dice[0].setY(MyConsts.die1Y);

        dice[1].setX(MyConsts.die2X);
        dice[1].setY(MyConsts.die1Y);
    }

    @Override
    public void updateSprite()
    {
        for(int i = 0; i < dice.length; i++)
            dice[i].updateSprite();

        if(!dice[0].isActive())
        {
           dice[1].setActive(false);
           resetDiceLocation();

           setActive(false);
        }
        else if(!dice[1].isActive())
        {
            dice[0].setActive(false);
            resetDiceLocation();

            setActive(false);
        }
        setRollStart(false);
    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        for(int i = 0; i < dice.length; i++)
            dice[i].drawSprite(canvas, paint);
    }

    public void rollDice()
    {
        setActive(true);
        for (int i = 0; i < dice.length; i++)
            dice[i].rollDie();
    }

    public Die[] getDice() {
        return dice;
    }

    public void setDice(Die[] dice) {
        this.dice = dice;
    }

    public boolean isRollStart() {
        return rollStart;
    }

    public void setRollStart(boolean rollStart) {
        this.rollStart = rollStart;
        if(isRollStart())
        {
            for(int i = 0; i < dice.length; i++)
                dice[i].getRollFaceValue();
        }
    }
}
