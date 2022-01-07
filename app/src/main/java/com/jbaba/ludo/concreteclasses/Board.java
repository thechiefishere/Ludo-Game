package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.RotateAnimation;

import com.jbaba.ludo.R;
import com.jbaba.ludo.abstractclasses.AbsSprite;

public class Board extends AbsSprite
{
    private Houses houses;
    private Road road;
    private Dice dice;
    private DiceHouse diceHouse;

    private RoadSegment[] roomDoor;
    private RoadSegment[] parlourDoor;
    public static final int JUMPVALUE = 5;

    public Board()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        road = new Road();
        diceHouse = new DiceHouse();
        dice = new Dice();
        houses = new Houses(road);
        setVisible(true);
        setActive(true);
    }

    @Override
    public void updateSprite()
    {
        if(dice.isActive())
            dice.updateSprite();
    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        paint.setColor(Color.CYAN);
        canvas.drawRect(MyConsts.boardStartingX, MyConsts.boardStartingY, MyConsts.boardStartingX + MyConsts.boardWidth,
                MyConsts.boardStartingY + MyConsts.boardHeight, paint);

        road.drawSprite(canvas, paint);
        houses.drawSprite(canvas, paint);
        diceHouse.drawSprite(canvas, paint);

        dice.drawSprite(canvas, paint);
    }

    public boolean movePiece(Piece piece, int faceValue)
    {
        if(!piece.isVisible())
            return false;
        if(!piece.isOnRoad() && !piece.isInParlour())
        {
            if(faceValue == 6)
            {
                piece.setOnRoad(true);
                movePieceToRoadSeg(piece, getPieceHouse(piece).getHouseRoomDoor());
                piece.setRoadSegmentNum(getPieceHouse(piece).getHouseRoomDoor().getSegmentNum());

                return true;
            }
            else
                return false;
        }
        else if(!piece.isOnRoad() && piece.isInParlour())
        {
            Log.d("Debugging", "Piece is in Parlour");
            int pieceSegNum = piece.getRoadSegmentNum();
            int pieceRoomDoorNum = getPieceHouse(piece).getHouseRoomDoor().getSegmentNum();
            int pieceWinningNum = (pieceRoomDoorNum - 1) - pieceSegNum;
            if(faceValue <= pieceWinningNum)
            {
                if(faceValue == pieceWinningNum)
                {
                    piece.setInParlour(false);
                    piece.setVisible(false);
                    RoadSegment aRoadSegment = getRoad().getRoadSeg()[piece.getRoadSegmentNum()];
                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                    piece.setRoadSegmentNum(-2);
                }
                else
                {
                    int whereTo = (piece.getRoadSegmentNum() + faceValue) % 72;
                    RoadSegment aRoadSegment = getRoad().getRoadSeg()[piece.getRoadSegmentNum()];
                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                    movePieceToRoadSeg(piece, road.getRoadSeg()[whereTo]);
                    piece.setRoadSegmentNum(whereTo);
                }
                return true;
            }
        }
        else
        {
            int whereTo = (piece.getRoadSegmentNum() + faceValue) % 72;
            for(int i = 0; i < getHouses().getHouses().length; i++)
            {
                int parlourNum = getHouses().getHouses()[i].getHousePalourDoor().getSegmentNum();
                int startingPoint = piece.getRoadSegmentNum();

                House aHouse = getPieceHouse(piece);
                int houseParlourDoorNum = aHouse.getHousePalourDoor().getSegmentNum();
                if(startingPoint == houseParlourDoorNum - 1 && faceValue == 6)
                {
                    piece.setInParlour(false);
                    piece.setVisible(false);
                    RoadSegment aRoadSegment = getRoad().getRoadSeg()[piece.getRoadSegmentNum()];
                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                    piece.setRoadSegmentNum(-2);

                    return true;
                }
                if (parlourNum <= whereTo && parlourNum > startingPoint)
                {
                    if(getPieceHouse(piece).getHousePalourDoor().getSegmentNum() != parlourNum)
                    {
                        whereTo = (whereTo + JUMPVALUE) % 72 ;
                        break;
                    }
                    else
                    {
                        Log.d("Debugging", "Am in else of movePiece in board");
                        piece.setInParlour(true);
                        piece.setOnRoad(false);
                        break;
                    }
                }
            }
            RoadSegment aRoadSegment = getRoad().getRoadSeg()[piece.getRoadSegmentNum()];
            aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
            movePieceToRoadSeg(piece, road.getRoadSeg()[whereTo]);
            piece.setRoadSegmentNum(whereTo);
            return true;
        }

        return false;
    }

    public House getPieceHouse(Piece piece)
    {
        for(int i = 0; i < houses.getHouses().length; i++)
        {
            House aHouse = houses.getHouses()[i];
            if(piece.getColor() == aHouse.getHouseColor())
                return aHouse;
        }

        return null;
    }

    public Piece getAnotherPieceInSegment(RoadSegment roadSegment, Piece piece)
    {
        Piece aPiece = null;
        int inc = 0;
        for(int i = 0; i < getHouses().getHouses().length; i++)
        {
            for(int j = 0; j < getHouses().getHouses()[i].getHousePieces().length; j++)
            {
                aPiece = getHouses().getHouses()[i].getHousePieces()[j];
                if(aPiece.getRoadSegmentNum() == roadSegment.getSegmentNum())
                    if(piece.getPieceSegPriority() == aPiece.getPieceSegPriority() + 1)
                        if(!aPiece.equals(piece))
                            return aPiece;
            }
        }

        return null;
    }

    public Piece returnPieceAtPosition(int x, int y)
    {
        Piece aPiece = null;
        for(int i = 0; i < getHouses().getHouses().length; i++)
        {
            for(int j = 0; j < getHouses().getHouses()[i].getHousePieces().length; j++)
            {
                aPiece = getHouses().getHouses()[i].getHousePieces()[j];
                RectF pieceRect = new RectF();
                pieceRect.left = aPiece.getX();
                pieceRect.top = aPiece.getY();
                pieceRect.right = pieceRect.left + 2 * MyConsts.pieceRadius;
                pieceRect.bottom = pieceRect.top + 2 * MyConsts.pieceRadius;

                if(pieceRect.contains(x, y))
                    return aPiece;
            }
        }

        return null;
    }

    public void movePieceToRoadSeg(Piece piece, RoadSegment roadSegment)
    {
        piece.setX(roadSegment.getX());
        piece.setY(roadSegment.getY());
        int numOfPiecesInSegment = roadSegment.getNumOfElementInSegment();
        piece.setPieceSegPriority(numOfPiecesInSegment + 1);
        roadSegment.setNumOfElementInSegment(roadSegment.getNumOfElementInSegment() + 1);
    }

    public Houses getHouses() {
        return houses;
    }

    public void setHouses(Houses houses) {
        this.houses = houses;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public DiceHouse getDiceHouse() {
        return diceHouse;
    }

    public void setDiceHouse(DiceHouse diceHouse) {
        this.diceHouse = diceHouse;
    }

    public RoadSegment[] getRoomDoor() {
        return roomDoor;
    }

    public void setRoomDoor(RoadSegment[] roomDoor) {
        this.roomDoor = roomDoor;
    }

    public RoadSegment[] getParlourDoor() {
        return parlourDoor;
    }

    public void setParlourDoor(RoadSegment[] parlourDoor) {
        this.parlourDoor = parlourDoor;
    }
}
