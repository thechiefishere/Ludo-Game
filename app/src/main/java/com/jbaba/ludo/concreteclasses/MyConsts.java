package com.jbaba.ludo.concreteclasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.jbaba.ludo.R;

import java.io.Serializable;

public class MyConsts implements Serializable
{
    public static Context context;
    public static int screenWidth;
    public static int screenHeight;
    private transient Paint paint = new Paint();

    public static final int redPieceColor = Color.argb(255, 255, 0, 0);
    public static final int yellowPieceColor = Color.argb(255, 255, 255, 0);
    public static final int greenPieceColor = Color.argb(255, 0, 255, 0);
    public static final int bluePieceColor = Color.argb(255, 0, 0, 255);

    public static final int lightRedPieceColor = Color.argb(190, 255, 0, 0);
    public static final int lightYellowPieceColor = Color.argb(190, 255, 255, 0);
    public static final int lightGreenPieceColor = Color.argb(190, 0, 255, 0);
    public static final int lightBluePieceColor = Color.argb(190, 0, 0, 255);

    public MyConsts(Context context, Point size)
    {
        this.context = context;
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        boardWidth = screenHeight;
        boardHeight = screenHeight;
        roadSegmentLength = boardHeight / 15;
        houseLength = 6 * (boardHeight / 15);
        diceHouseLength = roadSegmentLength * 3;
        diceLength = roadSegmentLength;
        roadSpace = 2;
        pieceRadius = (roadSegmentLength - (2 * roadSpace)) / 2;
        sleepTime = 150;
        boardStartingY = 0;
        boardStartingX = (screenWidth - boardWidth) / 2;
        boardEndingX = boardStartingX + boardWidth;
        die1X = boardStartingX + houseLength + roadSegmentLength / 4;
        die2X = boardEndingX - houseLength - roadSegmentLength - roadSegmentLength / 4;
        die1Y = boardStartingY + houseLength + roadSegmentLength;
        dieSpeed = diceLength;
        reverseButtonWidth = roadSegmentLength + (roadSegmentLength / 2);
        reverseButtonHeight = roadSegmentLength + (roadSegmentLength / 2);
        reverseButtonX = boardEndingX + 5;
        reverseButtonY = boardHeight - reverseButtonHeight;
        textSize = (roadSegmentLength / 2) + (roadSegmentLength / 3);
        playPauseButtonX = 0;
        playPauseButtonY = 0;
        playPauseButtonWidth = roadSegmentLength + (roadSegmentLength / 2);
        playPauseButtonHeight = roadSegmentLength + (roadSegmentLength / 2);
        quitButtonStartingY = screenHeight - (screenHeight / 4);
        quitButtonHeight = (screenHeight / 4) / 4;
        paint.setTextSize(quitButtonHeight);
        quitButtonWidth = paint.measureText("RESTART") + 20;
        quitButtonStartingX = (screenWidth - (screenWidth / 4)) - quitButtonWidth;
        replayButtonStartingY = quitButtonStartingY;
        replayButtonHeight = quitButtonHeight;
        replayButtonWidth = paint.measureText("RESTART") + 20;
        replayButtonStartingX = screenWidth / 4;
        houseChoiceDrawWidth = (screenWidth / 5) * 3;
        launcherTextSize = screenHeight / 2;
        launcherDrawWidth = (screenWidth / 4) / 2;
        gameTypeTextSize = boardHeight / 15;
        paint.setTextSize(gameTypeTextSize);
        vsComputerButtonStartingX = screenWidth / 2;
        vsComputerButtonStartingY = (boardHeight / 4) * 2;
        vsComputerButtonWidth = (int) paint.measureText("COMPUTER");
        vsComputerButtonHeight = gameTypeTextSize;
        multiPlayerButtonStartingX = screenWidth / 2;
        multiPlayerButtonStartingY = vsComputerButtonStartingY + (2 * gameTypeTextSize);
        multiPlayerButtonHeight = gameTypeTextSize;
        multiPlayerButtonWidth = (int) paint.measureText("FRIENDS");
        majorSleepTime = 10;
    }

    public static int boardWidth;
    public static int boardHeight;
    public static int roadSegmentLength;
    public static int houseLength;
    public static int diceHouseLength;
    public static int diceLength;
    public static int pieceRadius;
    public static int sleepTime;
    public static int boardStartingY;
    public static int boardStartingX;
    public static int boardEndingX;
    public static int roadSpace;
    public static int die1X;
    public static int die2X;
    public static int die1Y;
    public static int dieSpeed;
    public static int reverseButtonX;
    public static int reverseButtonY;
    public static int reverseButtonWidth;
    public static int reverseButtonHeight;
    public static int textSize;
    public static int playPauseButtonX;
    public static int playPauseButtonY;
    public static int playPauseButtonWidth;
    public static int playPauseButtonHeight;
    public static float quitButtonStartingX;
    public static float quitButtonStartingY;
    public static float quitButtonWidth;
    public static float quitButtonHeight;
    public static float replayButtonStartingX;
    public static float replayButtonStartingY;
    public static float replayButtonWidth;
    public static float replayButtonHeight;
    public static float houseChoiceDrawWidth;
    public static int launcherTextSize;
    public static float launcherDrawWidth;
    public static int gameTypeTextSize;
    public static int gameTypeDrawWidth;
    public static int multiPlayerButtonStartingX;
    public static int multiPlayerButtonStartingY;
    public static int multiPlayerButtonWidth;
    public static int multiPlayerButtonHeight;
    public static int vsComputerButtonStartingX;
    public static int vsComputerButtonStartingY;
    public static int vsComputerButtonWidth;
    public static int vsComputerButtonHeight;
    public static int majorSleepTime;
}
