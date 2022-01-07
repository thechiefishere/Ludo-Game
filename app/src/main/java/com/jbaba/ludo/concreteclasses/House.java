package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jbaba.ludo.abstractclasses.AbsSprite2D;

public class House extends AbsSprite2D
{
    private RoadSegment[] houseRoadSegments;
    private Piece[] housePieces;
    private transient Bitmap houseImg;
    private int houseColor;
    private int lightHouseColor;

    private RoadSegment houseRoomDoor;
    private RoadSegment housePalourDoor;

    public House()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setWidth(MyConsts.houseLength);
        setHeight(MyConsts.houseLength);
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
        paint.setColor(houseColor);
        canvas.drawBitmap(getHouseImg(), getX(), getY(), paint);
        /*for(int i = 0; i < 4; i++)
            getHousePieces()[i].drawSprite(canvas, paint);*/
    }

    public int getHouseColor() {
        return houseColor;
    }

    public void setHouseColor(int houseColor) {
        this.houseColor = houseColor;
    }

    public Bitmap getHouseImg() {
        return houseImg;
    }

    public void setHouseImg(Bitmap houseImg) {
        this.houseImg = houseImg;
    }

    public RoadSegment[] getHouseRoadSegments() {
        return houseRoadSegments;
    }

    public void setHouseRoadSegments(RoadSegment[] houseRoadSegments) {
        this.houseRoadSegments = houseRoadSegments;
    }

    public Piece[] getHousePieces() {
        return housePieces;
    }

    public void setHousePieces(Piece[] housePieces) {
        this.housePieces = housePieces;
    }

    public RoadSegment getHouseRoomDoor() {
        return houseRoomDoor;
    }

    public void setHouseRoomDoor(RoadSegment houseRoomDoor) {
        this.houseRoomDoor = houseRoomDoor;
    }

    public RoadSegment getHousePalourDoor() {
        return housePalourDoor;
    }

    public void setHousePalourDoor(RoadSegment housePalourDoor) {
        this.housePalourDoor = housePalourDoor;
    }

    public int getLightHouseColor() {
        return lightHouseColor;
    }

    public void setLightHouseColor(int lightHouseColor) {
        this.lightHouseColor = lightHouseColor;
    }
}
