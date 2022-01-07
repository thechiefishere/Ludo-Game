package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.jbaba.ludo.R;
import com.jbaba.ludo.abstractclasses.AbsSprite;

import java.util.ArrayList;

public class Houses extends AbsSprite
{
    private House[] houses;
    private Road road;
    private ArrayList<Piece> allPieces;

    public Houses(Road road)
    {
        super();
        this.road = road;
        allPieces = new ArrayList<>();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        initHouse();
    }

    private void initHouse()
    {
        houses = new House[4];
        for(int i = 0; i < houses.length; i++)
            houses[i] = new House();

        initHouseLoc();
        initHouseImage();
        initHouseRoadSegment();
        initHousePieces();
        initHouseDoors();
    }

    private void initHouseLoc()
    {
        int x = 0;
        int y = 0;
        int inc = 0;

        for(int i = 0; i < 2; i++)
        {
            if(i == 0)
                y = MyConsts.boardStartingY;
            else
                y = MyConsts.boardHeight - MyConsts.houseLength;
            for(int j = 0; j < 2; j++)
            {
                if(i == j)
                    x = MyConsts.boardStartingX;
                else
                    x = MyConsts.boardEndingX - MyConsts.houseLength;

                houses[inc].setX(x);
                houses[inc].setY(y);

                inc++;
            }
        }
    }

    private void initHouseImage()
    {
        int[] colors = {MyConsts.redPieceColor, MyConsts.yellowPieceColor, MyConsts.greenPieceColor, MyConsts.bluePieceColor};
        int[] lightColors = {MyConsts.lightRedPieceColor, MyConsts.lightYellowPieceColor, MyConsts.lightGreenPieceColor, MyConsts.lightBluePieceColor};
        int[] images = {R.drawable.davido, R.drawable.olamide, R.drawable.wizkid, R.drawable.tiwa};
        for(int i = 0; i < houses.length; i++)
        {
            houses[i].setHouseColor(colors[i]);
            houses[i].setLightHouseColor(lightColors[i]);
            Bitmap img = BitmapFactory.decodeResource(MyConsts.context.getResources(), images[i]);
            img = Bitmap.createScaledBitmap(img, MyConsts.houseLength, MyConsts.houseLength, false);
            houses[i].setHouseImg(img);
        }
    }

    private void initHouseRoadSegment()
    {
        int[] startingSegment = {7, 25, 43, 61};

        for(int i = 0; i < houses.length; i++)
        {
            RoadSegment[] houseRoadSegments = new RoadSegment[6];
            int segment = startingSegment[i];
            for(int j = 0; j < 6; j++)
            {

                houseRoadSegments[j] = road.getRoadSeg()[segment];
                houseRoadSegments[j].setColor(houses[i].getLightHouseColor());
                if(j < 4)
                    segment = segment + 1;
                else
                    segment = segment + 2;
            }

            houses[i].setHouseRoadSegments(houseRoadSegments);
        }
    }

    private void initHousePieces()
    {
        int num = 1;
        for(int i = 0; i < houses.length; i++)
        {
            int x1 = houses[i].getX() + (MyConsts.houseLength / 4) - MyConsts.pieceRadius;
            int y1 = houses[i].getY() + (MyConsts.houseLength / 4) - MyConsts.pieceRadius;
            int inc = 0;
            Piece[] housePieces = new Piece[4];
            for(int j = 0; j < 2; j++)
            {
                for(int k = 0; k < 2; k++)
                {
                    housePieces[inc] = new Piece();
                    housePieces[inc].setX(x1);
                    housePieces[inc].setY(y1);
                    housePieces[inc].setOriginX(x1);
                    housePieces[inc].setOriginY(y1);
                    housePieces[inc].setColor(houses[i].getHouseColor());
                    housePieces[inc].setPieceNum(num);
                    allPieces.add(housePieces[inc]);

                    y1 += ((2 * MyConsts.houseLength / 4) - MyConsts.pieceRadius);
                    inc++;
                    num++;
                }

                x1 += ((2 * MyConsts.houseLength / 4) - MyConsts.pieceRadius);
                y1 = houses[i].getY() + (MyConsts.houseLength / 4);
            }

            houses[i].setHousePieces(housePieces);
        }
    }

    private void initHouseDoors()
    {
        int segRoom = 13;
        int segPalour = 7;

        for(int i = 0; i < houses.length; i++)
        {
            houses[i].setHouseRoomDoor(road.getRoadSeg()[segRoom]);
            houses[i].setHousePalourDoor(road.getRoadSeg()[segPalour]);

            segRoom += 18;
            segPalour += 18;
        }
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        for(int i = 0; i < houses.length; i++)
            houses[i].drawSprite(canvas, paint);

        int inc = 0;
        int segmentPriority = 0;

        L : for(int i = 0; i < allPieces.size() + 2; i++)
        {
            for(int j = 0; j < allPieces.size(); j++)
            {
                Piece aPiece = allPieces.get(j);
                if(aPiece.getPieceSegPriority() == segmentPriority)
                {
                    inc++;
                    aPiece.drawSprite(canvas, paint);
                }

                if(inc == 16)
                    break L;
            }
            if(inc == 16)
                break;
            segmentPriority++;
        }
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public House[] getHouses() {
        return houses;
    }

    public void setHouses(House[] houses) {
        this.houses = houses;
    }
}
