package com.jbaba.ludo.concreteclasses;

import android.graphics.RectF;

import com.jbaba.ludo.abstractclasses.AbsPlayer;
import com.jbaba.ludo.activities.WifiP2pActivity;

public class Player extends AbsPlayer
{
    private Piece threadPiece;
    private int threadValue;

    public Player()
    {
        super();
        setPlayerName("Player");
    }

    public boolean makeCount(int x, int y, int faceValue, boolean called)
    {
        Piece piece = null;
        boolean successful = false;

        L : for(int i = 0; i < getPlayerHouse().length; i++)
        {
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                piece = getPlayerHouse()[i].getHousePieces()[j];

                RectF pieceRect = new RectF();
                pieceRect.left = piece.getX();
                pieceRect.top = piece.getY();
                pieceRect.right = pieceRect.left + 2 * MyConsts.pieceRadius;
                pieceRect.bottom = pieceRect.top + 2 * MyConsts.pieceRadius;

                if(pieceRect.contains(x, y) && piece.isVisible())
                {
                    piece = topMostPieceInSegment(piece);
                    threadPiece = piece;
                    threadValue = faceValue;
                    if(getBoard().movePiece(piece, faceValue))
                        successful = true;
                    break L;
                }
            }
        }
        if(successful)
        {
            if(called)
            {
                Thread thread = new Thread(new Runnable() {
                    int pieceNum = threadPiece.getPieceNum();
                    int value = threadValue;
                    @Override
                    public void run() {
                        try {
                            WifiP2pActivity.getSendReceive().getPrintWriter().println("C " + pieceNum + " " + value);
                            WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

            getMovedPieces().add(piece);
            getMovedValue().add(faceValue);
            if(killPossible(piece))
            {
                if(anotherFaceValuesCanBeUsedAfterKill(piece, faceValue))
                {
                    getMoveEffect().add('k');
                    kill(piece);
                }
                else
                    getMoveEffect().add('n');
            }
            else
            {
                getMoveEffect().add('n');
                if(!anotherFaceValuesCanBeUsedAfterMove(piece, faceValue))
                    setSkipMove(true);
            }

            return true;
        }

        return false;
    }

    public void makeCount(Piece piece, int faceValue)
    {
        getBoard().movePiece(piece, faceValue);

        getMovedPieces().add(piece);
        getMovedValue().add(faceValue);
        if(killPossible(piece))
        {
            if(anotherFaceValuesCanBeUsedAfterKill(piece, faceValue))
            {
                getMoveEffect().add('k');
                kill(piece);
            }
            else
                getMoveEffect().add('n');
        }
        else
        {
            getMoveEffect().add('n');
            if(!anotherFaceValuesCanBeUsedAfterMove(piece, faceValue))
                setSkipMove(true);
        }
    }

    public Piece getPieceFromPieceNum(int pieceNum)
    {
        Piece aPiece = null;

        for(int i = 0; i < getPlayerHouse().length; i++)
        {
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(aPiece.getPieceNum() == pieceNum)
                    return aPiece;
            }
        }

        return aPiece;
    }

    public void reverse()
    {
        if(getMovedValue().size() >= 1)
        {
            int lastIndex = getMovedValue().size() - 1;
            Piece playerPiece = getMovedPieces().get(lastIndex);
            int faceValue = getMovedValue().get(lastIndex);
            int effect = getMoveEffect().get(lastIndex);

            House aHouse = getBoard().getPieceHouse(playerPiece);
            int houseRoomDoor = aHouse.getHouseRoomDoor().getSegmentNum();
            int position = playerPiece.getRoadSegmentNum();
            RoadSegment aRoadSegment = null;
            if(position != -2)
            {
                if(position > 0)
                {
                    aRoadSegment = getBoard().getRoad().getRoadSeg()[position];
                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                }
                else
                {
                    position *= -1;
                    aRoadSegment = getBoard().getRoad().getRoadSeg()[position];
                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 1);
                }
            }

            int newPos = position - faceValue;
            if(checkIfNewPositionIsInAParlour(playerPiece, newPos, faceValue))
                newPos -= getBoard().JUMPVALUE;
            if(newPos < 0)
                newPos = 72 + newPos;

            if(effect == 'k')
            {
                int index = getKilledPieces().size() - 1;
                Piece killedPiece = getKilledPieces().get(index);
                killedPiece.setOnRoad(true);
                killedPiece.setX(aRoadSegment.getX());
                killedPiece.setY(aRoadSegment.getY());
                killedPiece.setRoadSegmentNum(aRoadSegment.getSegmentNum());
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 2);
                killedPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment());

                playerPiece.setVisible(true);
                playerPiece.setOnRoad(true);
                playerPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment() + 1);

                getKilledPieces().remove(getKilledPieces().size() - 1);
            }

            if(position == houseRoomDoor)
            {
                playerPiece.setX(playerPiece.getOriginX());
                playerPiece.setY(playerPiece.getOriginY());
                playerPiece.setOnRoad(false);
                playerPiece.setRoadSegmentNum(-1);
                playerPiece.setPieceSegPriority(0);
            }
            else if(position == - 2)
            {
                newPos = (houseRoomDoor - 1) - faceValue;
                playerPiece.setVisible(true);
                aRoadSegment = getBoard().getRoad().getRoadSeg()[newPos];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 1);
                playerPiece.setX(aRoadSegment.getX());
                playerPiece.setY(aRoadSegment.getY());
                playerPiece.setRoadSegmentNum(newPos);
                playerPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment());
                if(faceValue == 6)
                {
                    playerPiece.setOnRoad(true);
                    playerPiece.setInParlour(false);
                }
                else
                {
                    playerPiece.setOnRoad(false);
                    playerPiece.setInParlour(true);
                }
            }
            else
            {
                aRoadSegment = getBoard().getRoad().getRoadSeg()[newPos];
                aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() + 1);
                playerPiece.setX(aRoadSegment.getX());
                playerPiece.setY(aRoadSegment.getY());
                playerPiece.setRoadSegmentNum(newPos);
                playerPiece.setPieceSegPriority(aRoadSegment.getNumOfElementInSegment());
            }

            getPlayerDieValue().add(getMovedValue().get(lastIndex));
            getMovedValue().remove(lastIndex);
            getMoveEffect().remove(lastIndex);
            getMovedPieces().remove(lastIndex);
        }
    }

    public boolean checkIfNewPositionIsInAParlour(Piece piece, int newPos, int faceValue)
    {
        for(int i = 0; i < getBoard().getHouses().getHouses().length; i++)
        {
            House aHouse = getBoard().getHouses().getHouses()[i];
            int houseRoomDoorNum = aHouse.getHouseRoomDoor().getSegmentNum();
            int houseParlourDoorNum = aHouse.getHousePalourDoor().getSegmentNum();

            if(newPos >= houseParlourDoorNum && newPos < houseRoomDoorNum - 1)
                return true;
            if(newPos == houseParlourDoorNum - 1 && faceValue == 6)
                return true;
        }

        return false;
    }
}
