package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class Piece extends AbsSprite2D
{
    private boolean onRoad;
    private boolean inParlour;
    private int originX;
    private int originY;

    private int roadSegmentNum;
    private int pieceSegPriority;
    private int pieceNum;

    public Piece()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setColor(getColor());
        setVisible(true);
        setOnRoad(false);
        setRoadSegmentNum(-1);
        setInParlour(false);
        setPieceSegPriority(0);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        if(isVisible())
        {
            paint.setColor(Color.WHITE);
            int cx = getX() + MyConsts.pieceRadius;
            int cy = getY() + MyConsts.pieceRadius;
            canvas.drawCircle(cx, cy, MyConsts.pieceRadius, paint);

            int innerCircleRadius = MyConsts.pieceRadius - MyConsts.roadSpace;
            paint.setColor(getColor());
            canvas.drawCircle(cx, cy, innerCircleRadius, paint);
            if(getPieceSegPriority() > 1)
            {
                int startingX = cx - innerCircleRadius + (innerCircleRadius / 3);
                int startingY = cy - innerCircleRadius + (innerCircleRadius / 3);
                if(getColor() == MyConsts.redPieceColor || getColor() == MyConsts.bluePieceColor)
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.BLACK);
                canvas.save();
                canvas.rotate(90f, startingX, startingY);
                canvas.drawText("" + getPieceSegPriority(), startingX, startingY - 5, paint);
                canvas.restore();
            }

        }
    }

    public boolean isOnRoad() {
        return onRoad;
    }

    public void setOnRoad(boolean onRoad) {
        this.onRoad = onRoad;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int getRoadSegmentNum() {
        return roadSegmentNum;
    }

    public void setRoadSegmentNum(int roadSegmentNum) {
        this.roadSegmentNum = roadSegmentNum;
    }

    public boolean isInParlour() {
        return inParlour;
    }

    public void setInParlour(boolean inParlour) {
        this.inParlour = inParlour;
    }

    public int getPieceSegPriority() {
        return pieceSegPriority;
    }

    public void setPieceSegPriority(int pieceSegPriority) {
        this.pieceSegPriority = pieceSegPriority;
    }

    public int getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(int pieceNum) {
        this.pieceNum = pieceNum;
    }
}
