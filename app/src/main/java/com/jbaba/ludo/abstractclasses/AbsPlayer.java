package com.jbaba.ludo.abstractclasses;

import android.util.Log;

import com.jbaba.ludo.concreteclasses.Board;
import com.jbaba.ludo.concreteclasses.Dice;
import com.jbaba.ludo.concreteclasses.House;
import com.jbaba.ludo.concreteclasses.MyConsts;
import com.jbaba.ludo.concreteclasses.Piece;
import com.jbaba.ludo.concreteclasses.RoadSegment;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbsPlayer implements Serializable
{
    private House[] playerHouse;
    private Dice dice;
    private ArrayList<Integer> playerDieValue;
    private Board board;

    private ArrayList<Piece> killedPieces;
    private ArrayList<Piece> movedPieces;
    private ArrayList<Integer> movedValue;
    private ArrayList<Character> moveEffect;

    private boolean onlyParlourPieceMoveable;
    private boolean skipMove;
    private int computerPlayerDieValueCount;
    private String playerName;
    private static int computerCountForSix;
    private static int computerCountForDoubleSix;

    public AbsPlayer()
    {
        super();
        playerDieValue = new ArrayList<>();
        setComputerCountForSix(0);
        setComputerCountForDoubleSix(0);
        initPlayer();
    }

    public void initPlayer()
    {
        killedPieces = new ArrayList<>();
        movedPieces = new ArrayList<>();
        movedValue = new ArrayList<>();
        moveEffect = new ArrayList<>();
        setOnlyParlourPieceMoveable(false);
        setSkipMove(false);
    }

    public boolean rollDice(int a, int b)
    {
        dice.setRollStart(true);
        dice.rollDice();
        for(int i = 0; i < 2; i++)
            playerDieValue.add(dice.getDice()[i].getDieFaceValue());
        setVirtual(a, b);
        manipulatingComputerValue();

        while (dice.isActive())
        {
            try {
                Thread.sleep(MyConsts.sleepTime);
            } catch (InterruptedException iex) {

            }
        }

        return true;
    }

    public void manipulatingComputerValue()
    {
        if(this.getPlayerName().equals("Computer"))
        {
            if(getPlayerDieValue().get(0) != 6 && getPlayerDieValue().get(1) != 6)
                setComputerCountForSix(getComputerCountForSix() + 1);
            else
                setComputerCountForSix(0);

            if(getComputerCountForSix() == 2)
            {
                getPlayerDieValue().set(0, 6);
                dice.getDice()[0].setDieFaceValue(6);
                setComputerCountForSix(0);
            }
        }
    }

    public void setVirtual(int a, int b)
    {
        if(getPlayerName().equals("Opponent"))
        {
            getPlayerDieValue().set(getPlayerDieValue().size() - 2, a);
            getPlayerDieValue().set(getPlayerDieValue().size() - 1, b);
            dice.getDice()[0].setDieFaceValue(a);
            dice.getDice()[1].setDieFaceValue(b);
        }
    }

    public boolean thereIsPieceToMove(int faceValue)
    {
        boolean piecePresent = false;

        for(int i = 0; i < getPlayerHouse().length; i++)
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(aPiece.isVisible())
                {
                    if(!aPiece.isInParlour())
                        piecePresent = true;
                    if(aPiece.isOnRoad())
                        return true;
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
                        setOnlyParlourPieceMoveable(true);
                        return true;
                    }
                }
            }

        if(faceValue == 6 && piecePresent)
            return true;

        return false;
    }

    public boolean thereIsPieceToMove(Piece piece, int faceValue)
    {
        boolean piecePresent = false;

        for(int i = 0; i < getPlayerHouse().length; i++)
        {
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(aPiece.isVisible())
                {
                    piecePresent = true;
                    if(!piece.equals(aPiece))
                    {
                        int pieceRoomDoorNum = getPlayerHouse()[i].getHouseRoomDoor().getSegmentNum();
                        if(aPiece.isOnRoad())
                            return true;
                        if(isPieceInParlourMoveable(aPiece, faceValue, pieceRoomDoorNum))
                            return true;
                    }
                }
            }
        }
        if(faceValue == 6 && piecePresent)
            return true;

        return false;
    }

    public boolean isPieceInParlourMoveable(Piece piece, int faceValue, int pieceRoomDoorNum)
    {
        if(piece.isInParlour())
        {
            int pieceSegNum = piece.getRoadSegmentNum();
            int pieceWinningNum = (pieceRoomDoorNum - 1) - pieceSegNum;
            if(faceValue <= pieceWinningNum)
                return true;
        }
        return false;
    }

    public void kill(Piece piece)
    {
        int segmentNum = piece.getRoadSegmentNum();

        if (segmentNum != -2)
        {
            RoadSegment aRoadSegment = board.getRoad().getRoadSeg()[segmentNum];
            if(aRoadSegment.getNumOfElementInSegment() > 1)
            {
                Piece aPiece = board.getAnotherPieceInSegment(aRoadSegment, piece);
                boolean itsPlayerPiece = false;

                for(int i = 0; i < this.getPlayerHouse().length; i++)
                {
                    for(int j = 0; j < this.getPlayerHouse()[i].getHousePieces().length; j++)
                    {
                        Piece thisPiece = this.getPlayerHouse()[i].getHousePieces()[j];
                        if(thisPiece.isVisible())
                        {
                            if(aPiece.equals(thisPiece))
                            {
                                itsPlayerPiece = true;
                                break;
                            }
                        }
                    }
                }

                if(!itsPlayerPiece)
                {
                    aPiece.setX(aPiece.getOriginX());
                    aPiece.setY(aPiece.getOriginY());
                    aPiece.setOnRoad(false);
                    aPiece.setPieceSegPriority(0);
                    aPiece.setRoadSegmentNum(-1);
                    getKilledPieces().add(aPiece);

                    piece.setOnRoad(false);
                    piece.setVisible(false);
                    piece.setPieceSegPriority(0);
                    piece.setRoadSegmentNum(-piece.getRoadSegmentNum());

                    aRoadSegment.setNumOfElementInSegment(aRoadSegment.getNumOfElementInSegment() - 2);
                }
            }
        }
    }

    public Piece topMostPieceInSegment(Piece piece)
    {
        int roadSegNum = piece.getRoadSegmentNum();
        if(roadSegNum < 0)
            return piece;
        RoadSegment aRoadSegment = getBoard().getRoad().getRoadSeg()[roadSegNum];
        int numOfPiecesInSegment = aRoadSegment.getNumOfElementInSegment();
        if(numOfPiecesInSegment > 1)
        {
            if(piece.getPieceSegPriority() == numOfPiecesInSegment)
                return piece;
            else
            {
                for(int i = 0; i < getPlayerHouse().length; i++)
                {
                    House aHouse = getPlayerHouse()[i];
                    for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
                    {
                        Piece aPiece = aHouse.getHousePieces()[j];
                        int segNum = aPiece.getRoadSegmentNum();
                        if(aPiece.isVisible() && segNum == roadSegNum)
                            if(aPiece.getPieceSegPriority() == numOfPiecesInSegment)
                                return aPiece;
                    }
                }
            }
        }

        return piece;
    }

    public boolean playerWon()
    {
        int total = 0;
        for(int i = 0; i < getPlayerHouse().length; i++)
            for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
            {
                Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                if(!aPiece.isVisible())
                    total++;
            }

        if(total == 1)
            return true;

        return false;
    }

    public boolean killPossible(Piece piece)
    {
        boolean killPossible = false;
        int segmentNum = piece.getRoadSegmentNum();
        if (segmentNum != -2)
        {
            RoadSegment aRoadSegment = board.getRoad().getRoadSeg()[segmentNum];
            if(aRoadSegment.getNumOfElementInSegment() > 1)
            {
                Piece aPiece = board.getAnotherPieceInSegment(aRoadSegment, piece);
                boolean itsPlayerPiece = false;

                L :for(int i = 0; i < getPlayerHouse().length; i++)
                {
                    for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
                    {
                        Piece thisPiece = getPlayerHouse()[i].getHousePieces()[j];
                        if(thisPiece.isVisible())
                        {
                            if(aPiece.equals(thisPiece))
                            {
                                itsPlayerPiece = true;
                                break L;
                            }
                        }
                    }
                }

                if(!itsPlayerPiece)
                    killPossible = true;
            }
        }

        return killPossible;
    }

    public boolean anotherFaceValuesCanBeUsedAfterKill(Piece piece, int faceValue)
    {
        if(getPlayerDieValue().size() > 1)
        {
            ArrayList<Integer> otherValues = getOtherValues(faceValue);
            for(int t = 0; t < otherValues.size(); t++)
            {
                int nextValue = otherValues.get(t);

                for(int i = 0; i < getPlayerHouse().length; i++)
                    for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
                    {
                        Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                        if(aPiece.isVisible())
                            if(!aPiece.equals(piece))
                                if(thereIsPieceToMove(piece, nextValue))
                                    return true;
                    }
            }
        }
        else if(getPlayerDieValue().size() == 1)
            return true;
        if(getComputerPlayerDieValueCount() == 1)
            return true;

        return false;
    }

    public boolean anotherFaceValuesCanBeUsedAfterMove(Piece piece, int faceValue)
    {
        if(getPlayerDieValue().size() > 1)
        {
            ArrayList<Integer> otherValues = getOtherValues(faceValue);
            for(int t = 0; t < otherValues.size(); t++)
            {
                int nextValue = otherValues.get(t);

                for(int i = 0; i < getPlayerHouse().length; i++)
                    for(int j = 0; j < getPlayerHouse()[i].getHousePieces().length; j++)
                    {
                        Piece aPiece = getPlayerHouse()[i].getHousePieces()[j];
                        if(aPiece.isVisible())
                            if(!aPiece.equals(piece))
                                if(thereIsPieceToMove(piece, nextValue))
                                    return true;
                    }
            }
        }
        else if(getPlayerDieValue().size() == 1)
            return true;
        if(samePieceCanMakeBothMove(piece, faceValue))
            return true;

        return false;
    }

    public ArrayList<Integer> getOtherValues(int faceValue)
    {
        ArrayList<Integer> otherValues = new ArrayList<>();
        int faceIndex = 0;
        for(int i = 0; i < getPlayerDieValue().size(); i++)
            if(getPlayerDieValue().get(i) == faceValue)
            {
                faceIndex = i;
                break;
            }

        for(int i = 0; i < getPlayerDieValue().size(); i++)
        {
            if(i == faceIndex)
                continue;
            otherValues.add(getPlayerDieValue().get(i));
        }

        return otherValues;
    }

    public boolean samePieceCanMakeBothMove(Piece piece, int faceValue)
    {
        int nextValue = getOtherValues(faceValue).get(0);
        if(faceValue == 6 && !piece.isInParlour() && !piece.isOnRoad())
        {
            boolean changeSegNum = false;
            if(piece.getRoadSegmentNum() == -1)
            {
                int pieceHouseRoomDoorNum = getBoard().getPieceHouse(piece).getHouseRoomDoor().getSegmentNum();
                piece.setRoadSegmentNum(pieceHouseRoomDoorNum);
                changeSegNum = true;
            }
            if (!killPossible(piece))
            {
                if (changeSegNum)
                    piece.setRoadSegmentNum(-1);
                return true;
            }
        }
        else
        {
            if (!killPossible(piece))
            {
                int totalFaceValue = faceValue + nextValue;
                if(this.getPlayerName().equals("Player"))
                    totalFaceValue -= faceValue;
                int pieceHouseRoomDoorNum = getBoard().getPieceHouse(piece).getHouseRoomDoor().getSegmentNum();
                int pieceSegNum = piece.getRoadSegmentNum();

                if(pieceSegNum + totalFaceValue <= pieceHouseRoomDoorNum - 1)
                    return true;
                if(pieceSegNum + totalFaceValue > pieceHouseRoomDoorNum)
                    return true;

            }
        }

        return false;
    }

    public boolean rolledDulce()
    {
        if(getPlayerDieValue().get(getPlayerDieValue().size() - 1) == 6 && getPlayerDieValue().get(getPlayerDieValue().size() - 2) == 6)
            return true;

        return false;
    }

    public boolean rolledThreeDulce()
    {
        int count = 0;

        if(getPlayerDieValue().size() == 6)
            for(int i = 0; i < getPlayerDieValue().size(); i++)
                if(getPlayerDieValue().get(i) == 6)
                    count++;

        if(count == 6)
            return true;

        return false;
    }

    public void setPlayerHouse(House[] playerHouse) {
        this.playerHouse = playerHouse;
    }

    public House[] getPlayerHouse() {
        return playerHouse;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public void setPlayerDieValue(ArrayList<Integer> playerDieValue) {
        this.playerDieValue = playerDieValue;
    }

    public ArrayList<Integer> getPlayerDieValue() {
        return playerDieValue;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Piece> getKilledPieces() {
        return killedPieces;
    }

    public void setKilledPieces(ArrayList<Piece> killedPieces) {
        this.killedPieces = killedPieces;
    }

    public ArrayList<Piece> getMovedPieces() {
        return movedPieces;
    }

    public void setMovedPieces(ArrayList<Piece> movedPieces) {
        this.movedPieces = movedPieces;
    }

    public ArrayList<Integer> getMovedValue() {
        return movedValue;
    }

    public void setMovedValue(ArrayList<Integer> movedValue) {
        this.movedValue = movedValue;
    }

    public ArrayList<Character> getMoveEffect() {
        return moveEffect;
    }

    public void setMoveEffect(ArrayList<Character> moveEffect) {
        this.moveEffect = moveEffect;
    }

    public boolean isOnlyParlourPieceMoveable() {
        return onlyParlourPieceMoveable;
    }

    public void setOnlyParlourPieceMoveable(boolean onlyParlourPieceMoveable) {
        this.onlyParlourPieceMoveable = onlyParlourPieceMoveable;
    }

    public boolean isSkipMove() {
        return skipMove;
    }

    public void setSkipMove(boolean skipMove) {
        this.skipMove = skipMove;
    }

    public int getComputerPlayerDieValueCount() {
        return computerPlayerDieValueCount;
    }

    public void setComputerPlayerDieValueCount(int computerPlayerDieValueCount) {
        this.computerPlayerDieValueCount = computerPlayerDieValueCount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static int getComputerCountForSix() {
        return computerCountForSix;
    }

    public static void setComputerCountForSix(int computerCountForSix) {
        AbsPlayer.computerCountForSix = computerCountForSix;
    }

    public static int getComputerCountForDoubleSix() {
        return computerCountForDoubleSix;
    }

    public static void setComputerCountForDoubleSix(int computerCountForDoubleSix) {
        AbsPlayer.computerCountForDoubleSix = computerCountForDoubleSix;
    }
}
