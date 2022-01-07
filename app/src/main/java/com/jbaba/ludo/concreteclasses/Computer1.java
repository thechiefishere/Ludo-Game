package com.jbaba.ludo.concreteclasses;

import com.jbaba.ludo.abstractclasses.AbsPlayer;

import java.util.ArrayList;

public class Computer1 extends AbsPlayer
{
    ArrayList<Piece> piecesThatCanKill;
    ArrayList<Piece> piecesThatCanOnlyMove;

    public Computer1()
    {
        super();
        setPlayerName("Computer");
    }

    public void makeCount()
    {
        //Log.d("Debugging", "Am in makeCount");
        boolean successful = false;
        int numOfTimesToIterate = getPlayerDieValue().size();
        setComputerPlayerDieValueCount(getPlayerDieValue().size());

        while(numOfTimesToIterate >= 0)
        {
            ArrayList<Integer> enteredInAt = new ArrayList<>();

            for(int i = 0; i < getPlayerDieValue().size(); i++)
            {
                int value = getPlayerDieValue().get(i);
                //Log.d("Debugging", "value is " + value);
                if(thereIsPieceToMove(value))
                {
                    ArrayList<Piece> moveAblePieces = piecesThatCanMakeTheMove(value);
                    ArrayList<Piece> piecesThatCanKill = piecesThatCanMakeAKillWithTheMove(moveAblePieces, value);
                    ArrayList<Piece> piecesThatCanMakePerfectMove = piecesThatCanDoAPerfectMove(moveAblePieces, value);
                    Piece aPiece = null;
                    //Log.d("Debugging", "Num of pieces that can move with the value are " + moveAblePieces.size());
                    //Log.d("Debugging", "Num of pieces that can KILL with the value are " + piecesThatCanKill.size());
                    //Log.d("Debugging", "Num of pieces that can ONLY MOVE with the value are " + piecesThatCanMakePerfectMove.size());

                    if(piecesThatCanKill.size() > 0)
                    {
                        //Log.d("Debugging", "Am in piece that can kill with value " + value);
                        int rand = (int) (Math.random() * piecesThatCanKill.size());
                        aPiece = piecesThatCanKill.get(rand);
                        aPiece = topMostPieceInSegment(aPiece);
                        getBoard().movePiece(aPiece, value);
                        //Log.d("Debugging", "Am killing with piece " + aPiece);
                        kill(aPiece);
                        setComputerPlayerDieValueCount(getComputerPlayerDieValueCount() - 1);

                        enteredInAt.add(value);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException iex) {

                        }
                    }
                    else
                    {
                        if(piecesThatCanMakePerfectMove.size() > 0)
                        {
                            //Log.d("Debugging", "Am in piece that can move with value " + value);
                            int rand = (int) (Math.random() * piecesThatCanMakePerfectMove.size());
                            aPiece = piecesThatCanMakePerfectMove.get(rand);
                            aPiece = topMostPieceInSegment(aPiece);
                            getBoard().movePiece(aPiece, value);
                            setComputerPlayerDieValueCount(getComputerPlayerDieValueCount() - 1);

                            enteredInAt.add(value);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException iex) {

                            }
                        }
                    }

                    moveAblePieces.clear();
                    piecesThatCanKill.clear();
                    piecesThatCanMakePerfectMove.clear();
                }
            }

            for(int i = 0; i < enteredInAt.size(); i++)
                getPlayerDieValue().remove(enteredInAt.get(i));
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
                    {
                        //setOnlyParlourPieceMoveable(true);
                        moveAblePieces.add(aPiece);
                    }
                }
            }

        return moveAblePieces;
    }

    public ArrayList<Piece> piecesThatCanMakeAKillWithTheMove(ArrayList<Piece> moveAblePieces, int faceValue)
    {
        //Log.d("Debugging", "Am in pieces that can kill");
        //Log.d("Debugging", "Computer playerPieceValue is " + getComputerPlayerDieValueCount());
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
            {
                //Log.d("Debugging", "I enterred killPossible");
                if(anotherFaceValuesCanBeUsedAfterKill(aPiece, faceValue))
                {
                    //Log.d("Debugging", "I enterred anotherFaceValueCanBeUsedAfterKill");
                    piecesThatCanKill.add(aPiece);
                }
            }

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
}
