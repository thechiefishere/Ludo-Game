package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class RoadSegment extends AbsSprite2D
{
    private int segmentNum;
    private int numOfElementInSegment;

    public RoadSegment()
    {
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setWidth(MyConsts.roadSegmentLength);
        setHeight(MyConsts.roadSegmentLength);
        setVisible(true);
        setActive(false);
        setColor(Color.WHITE);
        setNumOfElementInSegment(0);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        paint.setColor(Color.BLACK);
        canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);
        paint.setColor(getColor());
        canvas.drawRect(getX() + MyConsts.roadSpace, getY() + MyConsts.roadSpace, getX() + getWidth() - MyConsts.roadSpace,
                getY() + getHeight() - MyConsts.roadSpace, paint);
    }

    public int getSegmentNum() {
        return segmentNum;
    }

    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    public int getNumOfElementInSegment() {
        return numOfElementInSegment;
    }

    public void setNumOfElementInSegment(int numOfElementInSegment) {
        this.numOfElementInSegment = numOfElementInSegment;
    }
}
