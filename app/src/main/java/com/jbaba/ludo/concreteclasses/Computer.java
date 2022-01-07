package com.jbaba.ludo.concreteclasses;

import com.jbaba.ludo.abstractclasses.AbsPlayer;
import com.jbaba.ludo.canvases.GameCanvas1;

import java.util.ArrayList;

public class Computer extends AbsPlayer
{
    ArrayList<Piece> piecesThatCanKill;
    ArrayList<Piece> piecesThatCanOnlyMove;

    //private GameCanvas1 gameCanvas1;

    public Computer()
    {
        super();
        setPlayerName("Computer");
    }

    public void makeCount()
    {
        int numOfTimesToIterate = getPlayerDieValue().size();

        while(numOfTimesToIterate >= 0)
        {
            int entered = -1;

            for(int i = 0; i < getPlayerDieValue().size(); i++)
            {
                int value = getPlayerDieValue().get(i);
                if(thereIsPieceToMove(value))
                {
                    ArrayList<Piece> moveAblePieces = piecesThatCanMakeTheMove(value);
                    ArrayList<Piece> piecesThatCanKill = piecesThatCanMakeAKillWithTheMove(moveAblePieces, value);
                    ArrayList<Piece> piecesThatCanMakePerfectMove = piecesThatCanDoAPerfectMove(moveAblePieces, value);
                    Piece aPiece = null;

                    if(piecesThatCanKill.size() > 0)
                    {
                        int rand = (int) (Math.random() * piecesThatCanKill.size());
                        aPiece = piecesThatCanKill.get(rand);
                        aPiece = topMostPieceInSegment(aPiece);
                        getBoard().movePiece(aPiece, value);
                        kill(aPiece);

                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException iex) {

                        }
                        entered = i;
                        break;
                    }
                    else
                    {
                        if(piecesThatCanMakePerfectMove.size() > 0)
                        {
                            int rand = (int) (Math.random() * piecesThatCanMakePerfectMove.size());
                            aPiece = piecesThatCanMakePerfectMove.get(rand);
                            aPiece = topMostPieceInSegment(aPiece);
                            getBoard().movePiece(aPiece, value);

                            try {
                                Thread.sleep(750);
                            } catch (InterruptedException iex) {

                            }
                            entered = i;
                            break;
                        }
                    }

                    moveAblePieces.clear();
                    piecesThatCanKill.clear();
                    piecesThatCanMakePerfectMove.clear();
                }
            }

            if(entered != -1)
                getPlayerDieValue().remove(entered);
            numOfTimesToIterate--;
            if(getPlayerDieValue().size() == 0 || numOfTimesToIterate == 0)
                break;
        }
    }

    public ArrayList<Piece> piecesThatCanMakeTheMove(int faceValue)
    {
        ArrayList<Piece> moveAblePieces = new ArrayList<>();

        for(int i = 0; i < getPlayerHouse().length; i++)
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(aPiece.isVisible())
                {
                    if(aPiece.isOnRoad())
                        moveAblePieces.add(aPiece);
                    else if(aPiece.getRoadSegmentNum() == -1 && faceValue == 6)
                        moveAblePieces.add(aPiece);
                }

            }
        for(int i = 0; i < getPlayerHouse().length; i++)
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(aPiece.isVisible())
                {
                    int pieceRoomDoorNum = getPlayerHouse()[i].getHouseRoomDoor().getSegmentNum();
                    if(isPieceInParlourMoveable(aPiece, faceValue, pieceRoomDoorNum))
                        moveAblePieces.add(aPiece);

                }
            }

        return moveAblePieces;
    }

    public ArrayList<Piece> piecesThatCanMakeAKillWithTheMove(ArrayList<Piece> moveAblePieces, int faceValue)
    {
        ArrayList<Piece> piecesThatCanKill = new ArrayList<>();
        for(int i = 0; i < moveAblePieces.size(); i++)
        {
            Piece aPiece = moveAblePieces.get(i);
            boolean changePieceSegNum = false;
            int pieceHouseDoorSegmentNum = getBoard().getPieceHouse(aPiece).getHouseRoomDoor().getSegmentNum();
            int parlourNum = getBoard().getPieceHouse(aPiece).getHousePalourDoor().getSegmentNum();
            int pieceStartingPriority = aPiece.getPieceSegPriority();;

            RoadSegment aRoadSegment = null;
            int startingPoint = 0;
            int whereTo = 0;

            if(aPiece.getRoadSegmentNum() == -2)
                continue;
            if(aPiece.getRoadSegmentNum() == -1)
            {
                aPiece.setRoadSegmentNum(pieceHouseDoorSegmentNum);
                aRoadSegment = getBoard().getRoad().getRoadSeg()[pieceHouseDoorSegmentNum];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 1);
                aPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment());

                changePieceSegNum = true;
            }
            else
            {
                startingPoint = aPiece.getRoadSegmentNum();
                aRoadSegment = getBoard().getRoad().getRoadSeg()[startingPoint];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                whereTo = (startingPoint + faceValue) % 72;

                for(int j = 0; j < getBoard().getHouses().getHouses().length; j++)
                {
                    House aHouse = getBoard().getHouses().getHouses()[j];
                    int houseParlourNum = aHouse.getHousePalourDoor().getSegmentNum();
                    if (houseParlourNum <= whereTo && houseParlourNum > startingPoint)
                    {
                        if(parlourNum != houseParlourNum)
                        {
                            whereTo = (whereTo + getBoard().JUMPVALUE) % 72;
                            break;
                        }
                    }
                }

                aPiece.setRoadSegmentNum(whereTo);
                aRoadSegment = getBoard().getRoad().getRoadSeg()[whereTo];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 1);
                aPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment());
            }

            if(killPossible(aPiece))
                if(anotherFaceValuesCanBeUsedAfterKill(aPiece, faceValue))
                    piecesThatCanKill.add(aPiece);

            if(changePieceSegNum)
            {
                aPiece.setRoadSegmentNum(-1);
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                aPiece.setPieceSegPriority(pieceStartingPriority);
            }
            else
            {
                aPiece.setRoadSegmentNum(startingPoint);

                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                aRoadSegment = getBoard().getRoad().getRoadSeg()[startingPoint];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 1);
                aPiece.setPieceSegPriority(pieceStartingPriority);
            }
        }

        return piecesThatCanKill;
    }

    public ArrayList<Piece> piecesThatCanDoAPerfectMove(ArrayList<Piece> moveAblePieces, int faceValue)
    {
        ArrayList<Piece> piecesThatCanMakePerfectMove = new ArrayList<>();
        for(int i = 0; i < moveAblePieces.size(); i++)
        {
            Piece aPiece = moveAblePieces.get(i);
            if(anotherFaceValuesCanBeUsedAfterMove(aPiece, faceValue))
                piecesThatCanMakePerfectMove.add(aPiece);
        }

        return piecesThatCanMakePerfectMove;
    }

    public ArrayList<Piece> getPiecesThatCanKill() {
        return piecesThatCanKill;
    }

    public void setPiecesThatCanKill(ArrayList<Piece> piecesThatCanKill) {
        this.piecesThatCanKill = piecesThatCanKill;
    }

    public ArrayList<Piece> getPiecesThatCanOnlyMove() {
        return piecesThatCanOnlyMove;
    }

    public void setPiecesThatCanOnlyMove(ArrayList<Piece> piecesThatCanOnlyMove) {
        this.piecesThatCanOnlyMove = piecesThatCanOnlyMove;
    }

    /*public void setGameCanvas1(GameCanvas1 gameCanvas1) {
        this.gameCanvas1 = gameCanvas1;
    }*/
}
