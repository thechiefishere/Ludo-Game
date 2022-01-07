package com.jbaba.ludo.canvases;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.jbaba.ludo.LudoDatabaseHelper;
import com.jbaba.ludo.activities.DeclareWinnerActivity;
import com.jbaba.ludo.abstractclasses.AbsGameCanvas;
import com.jbaba.ludo.abstractclasses.AbsPlayer;
import com.jbaba.ludo.concreteclasses.Board;
import com.jbaba.ludo.concreteclasses.Computer;
import com.jbaba.ludo.concreteclasses.Die;
import com.jbaba.ludo.concreteclasses.DrawString;
import com.jbaba.ludo.concreteclasses.House;
import com.jbaba.ludo.concreteclasses.MyConsts;
import com.jbaba.ludo.concreteclasses.PlayPauseButton;
import com.jbaba.ludo.concreteclasses.Player;
import com.jbaba.ludo.concreteclasses.ReverseButton;

import java.util.Collections;

public class GameCanvas1 extends AbsGameCanvas
{
    private Board board;
    private Player player;
    private Computer computer;
    private AbsPlayer currPlayer;
    private ReverseButton reverseButton;
    private Die[] countingDice;
    private Die clickedDie;
    private PlayPauseButton playPauseButton;

    private boolean rollDiePossible;
    private boolean firstStep;
    private boolean secondStep;
    private boolean thirdStep;

    private int DieValueToCount;
    private String playerHouse;

    private int playerScore;
    private int computerScore;
    private transient Cursor cursor;
    private transient SQLiteDatabase db;

    public GameCanvas1(Context context, Point point, String playerHouse)
    {
        super(context, point);
        this.playerHouse = playerHouse;
        initComponent();
        startGame();
    }

    public void initComponent()
    {
        initBoard();
        initPlayer();
        initComputer();
        initReverseButton();
        initCountingDice();
        initDatabase();
        //initPlayPauseButton();

        currPlayer = player;
        setRollDiePossible(true);
        initSteps();
    }

    private void initSteps()
    {
        setFirstStep(true);
        setSecondStep(false);
        setThirdStep(false);
    }

    private void initBoard()
    {
        board = new Board();
        getSpriteAry().add(board);
    }

    private void initPlayer()
    {
        player = new Player();
        player.setDice(board.getDice());
        player.initPlayer();

        House[] houses = new House[2];
        for(int i = 0; i < getPlayerHouse().length(); i++)
        {
            int house = Integer.parseInt(Character.toString(playerHouse.charAt(i)));
            houses[i] = board.getHouses().getHouses()[house];
        }
        player.setPlayerHouse(houses);

        player.setBoard(board);
    }

    private void initComputer()
    {
        computer = new Computer();
        computer.setDice(board.getDice());
        computer.initPlayer();

        House[] houses = new House[2];
        int inc = 0;
        for(int i = 0; i < 4; i++)
        {
            String s = Integer.toString(i);
            if(playerHouse.indexOf(s.charAt(0)) > -1)
                continue;

            houses[inc] = board.getHouses().getHouses()[i];
            inc++;
        }
        computer.setPlayerHouse(houses);
        computer.setBoard(board);
        //computer.setGameCanvas1(this);
    }

    private void initReverseButton()
    {
        reverseButton = new ReverseButton();
        getSpriteAry().add(reverseButton);
    }

    private void initCountingDice()
    {
        countingDice = new Die[6];

        int x = MyConsts.boardEndingX + 5;
        int y = MyConsts.boardStartingY + MyConsts.houseLength + MyConsts.roadSegmentLength / 2;
        int imageWidth = MyConsts.diceLength;
        int count = 0;

        for(int i = 0; i < countingDice.length; i++)
        {
            if(i % 2 == 0)
                y = MyConsts.boardStartingY + MyConsts.houseLength + MyConsts.roadSegmentLength / 2;
            Die die = new Die();
            die.setVisible(false);
            die.setDieName("die" + i);
            die.setX(x);
            die.setY(y);
            countingDice[i] = die;
            getSpriteAry().add(die);

            count++;
            y += imageWidth + 5;

            if(count == 2)
            {
                x += imageWidth + 5;
                count = 0;
            }
        }
    }

    public void initPlayPauseButton()
    {
        playPauseButton = new PlayPauseButton();
        getSpriteAry().add(playPauseButton);
    }

    public void initDatabase()
    {
        SQLiteOpenHelper ludoDatabaseHelper = new LudoDatabaseHelper(MyConsts.context);
        try {
            db = ludoDatabaseHelper.getWritableDatabase();
            cursor = db.query("SCORE", new String[] {"PLAYER_SCORE", "COMPUTER_SCORE"}, "_id = ?", new String[] {Integer.toString(1)},
                    null, null, null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(cursor.moveToFirst())
        {
            playerScore = cursor.getInt(0);
            computerScore = cursor.getInt(1);
        }
    }

    private void switchPlayer()
    {
        clearer();
        setFirstStep(true);
        setSecondStep(false);
        setThirdStep(false);
        setRollDiePossible(true);
        removeDice();

        boolean isCurrent = false;
        if(currPlayer.equals(player))
        {
            if(!currPlayer.playerWon())
            {
                currPlayer = computer;
                reverseButton.setVisible(false);
                isCurrent = true;
            }
            else
                declareGameOver();
        }
        else if(currPlayer.equals(computer))
        {
            if(!currPlayer.playerWon())
            {
                currPlayer = player;
                reverseButton.setVisible(true);
            }
            else
                declareGameOver();
        }

        if(currPlayer.equals(computer) && isCurrent)
        {
            try {
                Thread.sleep(300);
                computerPlay();
            } catch (InterruptedException iex) {

            }
        }
    }

    public void computerPlay()
    {
        while(isRollDiePossible() && isShowing())
            currPlayerClickToRollDice(board.getDiceHouse().getX() + MyConsts.diceLength, board.getDiceHouse().getY() + MyConsts.diceLength);
        Collections.sort(computer.getPlayerDieValue(), Collections.<Integer>reverseOrder());

        try{
            Thread.sleep(500);
            computer.makeCount();
            Thread.sleep(300);
            switchPlayer();
        } catch (InterruptedException iex) {

        }
    }

    @Override
    public void drawBoardDisplays(Canvas canvas, Paint paint)
    {
        drawScores(canvas, paint);
        drawTurn(canvas, paint);
        if(getHighlighter().isVisible())
            getHighlighter().drawSprite(getCanvas(), getPaint());
    }

    public void drawScores(Canvas canvas, Paint paint)
    {
        paint.setTextSize(MyConsts.screenHeight / 20);
        paint.setColor(Color.WHITE);
        DrawString drawString = new DrawString(0, (int)paint.getTextSize(), MyConsts.boardStartingX - MyConsts.boardStartingX / 5, "SCORES");
        drawString.paintStringWithoutMargin(canvas,paint);

        paint.setTextSize(MyConsts.screenHeight / 25);
        paint.setColor(Color.BLACK);
        drawString = new DrawString(0, (int)paint.getTextSize() + (int)MyConsts.screenHeight / 20,
                MyConsts.boardStartingX - MyConsts.boardStartingX / 5, "PLAYER:");
        drawString.paintStringWithoutMargin(canvas,paint);

        paint.setTextSize(MyConsts.screenHeight / 25);
        paint.setColor(Color.WHITE);
        drawString = new DrawString(0, (int)paint.getTextSize() + 2 * (int)MyConsts.screenHeight / 20,
                MyConsts.boardStartingX - MyConsts.boardStartingX / 5, "" + playerScore);
        drawString.paintStringWithoutMargin(canvas,paint);

        paint.setTextSize(MyConsts.screenHeight / 25);
        paint.setColor(Color.BLACK);
        drawString = new DrawString(0, (int)paint.getTextSize() + 3 * (int)MyConsts.screenHeight / 20,
                MyConsts.boardStartingX - MyConsts.boardStartingX / 5, "COMPUTER:");
        drawString.paintStringWithoutMargin(canvas,paint);

        paint.setTextSize(MyConsts.screenHeight / 25);
        paint.setColor(Color.WHITE);
        drawString = new DrawString(0, (int)paint.getTextSize() + 4 * (int)MyConsts.screenHeight / 20,
                MyConsts.boardStartingX - MyConsts.boardStartingX / 5, "" + computerScore);
        drawString.paintStringWithoutMargin(canvas,paint);
    }

    public void declareGameOver()
    {
        ContentValues newScore = new ContentValues();
        if(currPlayer.equals(player))
        {
            newScore.put("PLAYER_SCORE", playerScore + 1);
            db.update("SCORE", newScore, "_id = ?", new String[] {Integer.toString(1)});
        }
        else
        {
            newScore.put("COMPUTER_SCORE", computerScore + 1);
            db.update("SCORE", newScore, "_id = ?", new String[] {Integer.toString(1)});
        }

        cursor.close();
        db.close();

        Intent intent = new Intent(MyConsts.context, DeclareWinnerActivity.class);
        intent.putExtra(DeclareWinnerActivity.WINNER, currPlayer.getPlayerName());
        MyConsts.context.startActivity(intent);
    }

    public void clearer()
    {
        currPlayer.getPlayerDieValue().clear();
        currPlayer.getMovedValue().clear();
        currPlayer.getMoveEffect().clear();
        currPlayer.getMovedPieces().clear();
        currPlayer.getKilledPieces().clear();
        currPlayer.setOnlyParlourPieceMoveable(false);
        currPlayer.setSkipMove(false);
        currPlayer.setComputerPlayerDieValueCount(0);
    }

    public void addRolledDice()
    {
        for(int i = 0; i < currPlayer.getPlayerDieValue().size(); i++)
        {
            for(int j = 0; j < countingDice.length; j++)
            {
                Die aDie = countingDice[i];
                if(!aDie.isVisible())
                {
                    aDie.setDieFaceValue(currPlayer.getPlayerDieValue().get(i));
                    aDie.setVisible(true);
                    break;
                }
            }
        }
    }

    public void drawTurn(Canvas canvas, Paint paint)
    {
        paint.setColor(Color.WHITE);
        paint.setTextSize(MyConsts.textSize);
        if(currPlayer.equals(player))
            canvas.drawText("Your Turn", MyConsts.boardEndingX + 5, MyConsts.boardStartingY + MyConsts.textSize, paint);
    }

    public boolean clickedReverse(int x, int y)
    {
        RectF reverseButtonRect = new RectF();
        reverseButtonRect.left = reverseButton.getX();
        reverseButtonRect.top = reverseButton.getY();
        reverseButtonRect.right = reverseButtonRect.left + MyConsts.reverseButtonWidth;
        reverseButtonRect.bottom = reverseButtonRect.top + MyConsts.reverseButtonHeight;

        if(reverseButtonRect.contains(x, y))
        {
            setHighlighterValues((int)reverseButtonRect.left, (int)reverseButtonRect.top, (int)reverseButtonRect.right, (int)reverseButtonRect.bottom);
            player.reverse();
            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
            {
                if(currPlayer.equals(player) && isShowing())
                {
                    if(clickedReverse((int)motionEvent.getX(), (int)motionEvent.getY()))
                    {
                        removeDice();
                        addRolledDice();
                    }
                    if(isFirstStep())
                    {
                        currPlayerClickToRollDice((int) motionEvent.getX(), (int) motionEvent.getY());
                        if(isRollDiePossible())
                        {
                            setFirstStep(true);
                            setSecondStep(false);
                        }
                        else
                        {
                            for(int element : currPlayer.getPlayerDieValue())
                            {
                                if(currPlayer.thereIsPieceToMove(element))
                                {
                                    setFirstStep(false);
                                    setSecondStep(true);
                                    break;
                                }
                            }
                            if(!isSecondStep())
                            {
                                while (board.getDice().isActive())
                                {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException iex) {

                                    }
                                }
                                try {
                                    Thread.sleep(1000);
                                    switchPlayer();
                                } catch (InterruptedException iex) {

                                }
                            }
                        }
                    }
                    else if(isSecondStep())
                    {
                        clickDice((int)motionEvent.getX(), (int)motionEvent.getY());
                        if(!currPlayer.thereIsPieceToMove(getDieValueToCount()))
                        {
                            setSecondStep(true);
                            setThirdStep(false);
                        }
                    }
                    else if(isThirdStep())
                    {
                        clickDice((int)motionEvent.getX(), (int)motionEvent.getY());
                        if(player.makeCount((int)motionEvent.getX(), (int)motionEvent.getY(), getDieValueToCount(), false))
                        {
                            removeDiceAndDieValue(getClickedDie());
                            if(currPlayer.getPlayerDieValue().size() > 0)
                            {
                                setThirdStep(false);
                                setSecondStep(true);
                            }
                            else
                            {
                                try {
                                    Thread.sleep(1000);
                                    switchPlayer();
                                } catch (InterruptedException iex) {

                                }
                            }

                            if(currPlayer.isOnlyParlourPieceMoveable())
                            {
                                Log.d("Debugging", "Only parlour piece is movebale");
                                boolean pieceStillMoveable = false;
                                if(currPlayer.getPlayerDieValue().size() != 0)
                                {
                                    for(int i = 0; i < currPlayer.getPlayerDieValue().size(); i++)
                                    {
                                        int value = currPlayer.getPlayerDieValue().get(i);
                                        if(currPlayer.thereIsPieceToMove(value))
                                            pieceStillMoveable = true;
                                    }
                                    if(!pieceStillMoveable)
                                    {
                                        Log.d("Debugging", "Am in player doesnt have piece to move again");
                                        try {
                                            Thread.sleep(1000);
                                            switchPlayer();
                                        } catch (InterruptedException iex) {

                                        }
                                    }
                                }
                            }
                            if(currPlayer.isSkipMove())
                            {
                                try {
                                    Thread.sleep(500);
                                    switchPlayer();
                                } catch (InterruptedException iex) {

                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void currPlayerClickToRollDice(int x, int y)
    {
        RectF diceHouseRect = new RectF();
        diceHouseRect.left = board.getDiceHouse().getX();
        diceHouseRect.top = board.getDiceHouse().getY();
        diceHouseRect.right = diceHouseRect.left + board.getDiceHouse().getWidth();
        diceHouseRect.bottom = diceHouseRect.top + board.getDiceHouse().getHeight();

        if(diceHouseRect.contains(x, y) && isRollDiePossible())
        {
            setHighlighterValues((int)diceHouseRect.left, (int)diceHouseRect.top, (int)diceHouseRect.right, (int)diceHouseRect.bottom);
            currPlayer.rollDice(0,0);
            setRollDiePossible(false);
            addRolledDice();

            if(currPlayer.rolledThreeDulce())
            {
                removeDice();
                currPlayer.getPlayerDieValue().clear();
                setRollDiePossible(true);
            }
            else if(currPlayer.rolledDulce())
                setRollDiePossible(true);
        }
    }

    private void clickDice(int x, int y)
    {
        for(int i = 0; i < countingDice.length; i++)
        {
            Die die = countingDice[i];

            RectF dieRect = new RectF();
            dieRect.left = die.getX();
            dieRect.top = die.getY();
            dieRect.right = dieRect.left + MyConsts.diceLength;
            dieRect.bottom = dieRect.top + MyConsts.diceLength;

            if(dieRect.contains(x, y) && die.isVisible())
            {
                setHighlighterValues((int)dieRect.left, (int)dieRect.top, (int)dieRect.right, (int)dieRect.bottom);
                setDieValueToCount(die.getDieFaceValue());
                getPaint().setColor(Color.BLACK);
                getCanvas().drawRect(dieRect, getPaint());
                setSecondStep(false);
                setThirdStep(true);
                setClickedDie(die);
                break;
            }
        }
    }

    public void removeDice()
    {
        for(int i = 0; i < countingDice.length; i++)
        {
            Die aDie = countingDice[i];
            if(aDie.isVisible())
                aDie.setVisible(false);
        }
    }

    public void removeDiceAndDieValue(Die die)
    {
        int faceValue = die.getDieFaceValue();
        for(int i = 0; i < currPlayer.getPlayerDieValue().size(); i++)
        {
            if(currPlayer.getPlayerDieValue().get(i) == faceValue)
            {
                currPlayer.getPlayerDieValue().remove(i);
                die.setVisible(false);
                break;
            }
        }
    }

    public boolean isRollDiePossible() {
        return rollDiePossible;
    }

    public void setRollDiePossible(boolean rollDiePossible) {
        this.rollDiePossible = rollDiePossible;
    }

    public boolean isFirstStep() {
        return firstStep;
    }

    public void setFirstStep(boolean firstStep) {
        this.firstStep = firstStep;
    }

    public boolean isSecondStep() {
        return secondStep;
    }

    public void setSecondStep(boolean secondStep) {
        this.secondStep = secondStep;
    }

    public boolean isThirdStep() {
        return thirdStep;
    }

    public void setThirdStep(boolean thirdStep) {
        this.thirdStep = thirdStep;
    }

    public int getDieValueToCount() {
        return DieValueToCount;
    }

    public void setDieValueToCount(int dieValueToCount) {
        DieValueToCount = dieValueToCount;
    }

    public String getPlayerHouse() {
        return playerHouse;
    }

    public void setPlayerHouse(String playerHouse) {
        this.playerHouse = playerHouse;
    }

    public AbsPlayer getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(AbsPlayer currPlayer) {
        this.currPlayer = currPlayer;
    }

    public Die getClickedDie() {
        return clickedDie;
    }

    public void setClickedDie(Die clickedDie) {
        this.clickedDie = clickedDie;
    }
}
