package com.jbaba.ludo.canvases;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;

import com.jbaba.ludo.abstractclasses.AbsGameCanvas;
import com.jbaba.ludo.abstractclasses.AbsPlayer;
import com.jbaba.ludo.activities.DeclareWinnerActivity;
import com.jbaba.ludo.activities.WifiP2pActivity;
import com.jbaba.ludo.concreteclasses.Board;
import com.jbaba.ludo.concreteclasses.Die;
import com.jbaba.ludo.concreteclasses.House;
import com.jbaba.ludo.concreteclasses.MyConsts;
import com.jbaba.ludo.concreteclasses.Piece;
import com.jbaba.ludo.concreteclasses.PlayPauseButton;
import com.jbaba.ludo.concreteclasses.Player;
import com.jbaba.ludo.concreteclasses.ReverseButton;

import java.util.StringTokenizer;

public class GameCanvasClient extends AbsGameCanvas
{
    private Board board;
    private Player playerClient;
    private Player playerServer;
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
    private String todo;

    private int[] rolledDieValue;

    private transient Thread thread;
    private volatile boolean incomingThreadRunning;

    public GameCanvasClient(Context context, Point point, String playerHouse)
    {
        super(context, point);
        this.playerHouse = playerHouse;
        initComponent();
        startGame();
        checkClientPlayerTurn();
    }

    public void initComponent()
    {
        initBoard();
        initPlayer();
        initComputer();
        initReverseButton();
        initCountingDice();

        currPlayer = playerServer;
        setRollDiePossible(true);
        setIncomingThreadRunning(true);
        initSteps();
        rolledDieValue = new int[2];
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
        playerClient = new Player();
        playerClient.setDice(board.getDice());
        playerClient.initPlayer();

        House[] houses = new House[2];
        for(int i = 0; i < getPlayerHouse().length(); i++)
        {
            int house = Integer.parseInt(Character.toString(playerHouse.charAt(i)));
            houses[i] = board.getHouses().getHouses()[house];
        }
        playerClient.setPlayerHouse(houses);
        playerClient.setBoard(board);
    }

    private void initComputer()
    {
        playerServer = new Player();
        playerServer.setDice(board.getDice());
        playerServer.initPlayer();

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
        playerServer.setPlayerHouse(houses);
        playerServer.setBoard(board);
        playerServer.setPlayerName("Opponent");
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

    public void checkClientPlayerTurn()
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isIncomingThreadRunning())
                {
                    //Looper.prepare();
                    try
                    {
                        todo = WifiP2pActivity.getSendReceive().getBufferedReader().readLine();
                        String use = todo;
                        if(use == null)
                        {

                        }
                        else if(todo.charAt(0) == 'C')
                        {
                            StringTokenizer stk = new StringTokenizer(todo);
                            String c = stk.nextToken();
                            int pieceNum = Integer.parseInt(stk.nextToken());
                            int faceValue = Integer.parseInt(stk.nextToken());
                            Piece aPiece = ((Player)currPlayer).getPieceFromPieceNum(pieceNum);
                            ((Player)currPlayer).makeCount(aPiece, faceValue);
                        }
                        else if(todo.charAt(0) == 'R')
                        {
                            StringTokenizer stk = new StringTokenizer(todo);
                            String c = stk.nextToken();
                            int a = Integer.parseInt(stk.nextToken());
                            int b = Integer.parseInt(stk.nextToken());
                            Log.d("GameCanvasClient", "Rolled value is " + a + " " + b);
                            setRolledValue(a, b);
                            currPlayerClickToRollDice(board.getDiceHouse().getX(), board.getDiceHouse().getY(), false);
                        }
                        else if(todo.charAt(0) == 'D')
                        {
                            StringTokenizer stk = new StringTokenizer(todo);
                            String c = stk.nextToken();
                            String dieName = stk.nextToken();
                            for(int i = 0; i < getCountingDice().length; i++)
                            {
                                if(getCountingDice()[i].getDieName().equals(dieName))
                                {
                                    setClickedDie(getCountingDice()[i]);
                                    break;
                                }
                            }
                            removeDiceAndDieValue(getClickedDie(), false);
                        }
                        else if(todo.charAt(0) == 'G')
                        {
                            declareGameOver();
                            setIncomingThreadRunning(false);
                            break;
                        }
                        else if(todo.charAt(0) == 'U')
                        {
                            clickedReverse(reverseButton.getX(), reverseButton.getY(), false);
                        }
                        else if(use.equals("Turn"))
                        {
                            switchPlayer();
                        }
                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Log.d("Debugging", "Am out of while");
            }
        });
        thread.start();
    }

    public void setRolledValue(int a, int b)
    {
        rolledDieValue[0] = a;
        rolledDieValue[1] = b;
    }

    public int[] getRolledDieValue() {
        return rolledDieValue;
    }

    private void switchPlayer()
    {
        clearer();
        setFirstStep(true);
        setSecondStep(false);
        setThirdStep(false);
        setRollDiePossible(true);
        removeDice();

        if(currPlayer.equals(playerClient))
        {
            if(!currPlayer.playerWon())
            {
                currPlayer = playerServer;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WifiP2pActivity.getSendReceive().getPrintWriter().println("Turn");
                            WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                        } catch (Exception ex) {

                        }
                    }
                });
                thread.start();
                reverseButton.setVisible(false);
            }
            else
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WifiP2pActivity.getSendReceive().getPrintWriter().println("Go");
                            WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                        } catch (Exception ex) {

                        }
                    }
                });
                thread.start();
                declareGameOver();
            }
        }
        else if(currPlayer.equals(playerServer))
        {
            if(!currPlayer.playerWon())
            {
                currPlayer = playerClient;
                reverseButton.setVisible(true);
            }
            else
                declareGameOver();
        }
    }

    @Override
    public void drawBoardDisplays(Canvas canvas, Paint paint)
    {
        drawTurn(canvas, paint);
        if(getHighlighter().isVisible())
            getHighlighter().drawSprite(getCanvas(), getPaint());
    }

    public void declareGameOver()
    {
        Intent intent = new Intent(MyConsts.context, DeclareWinnerActivity.class);
        intent.putExtra(DeclareWinnerActivity.WINNER, currPlayer.getPlayerName());
        intent.putExtra(DeclareWinnerActivity.HOSTORCLIENT, "Client");
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
        if(currPlayer.equals(playerClient))
            canvas.drawText("Your Turn", MyConsts.boardEndingX + 5, MyConsts.boardStartingY + MyConsts.textSize, paint);
    }

    public boolean clickedReverse(int x, int y, boolean called)
    {
        RectF reverseButtonRect = new RectF();
        reverseButtonRect.left = reverseButton.getX();
        reverseButtonRect.top = reverseButton.getY();
        reverseButtonRect.right = reverseButtonRect.left + MyConsts.reverseButtonWidth;
        reverseButtonRect.bottom = reverseButtonRect.top + MyConsts.reverseButtonHeight;

        if(reverseButtonRect.contains(x, y))
        {
            setHighlighterValues((int)reverseButtonRect.left, (int)reverseButtonRect.top, (int)reverseButtonRect.right, (int)reverseButtonRect.bottom);
            ((Player)currPlayer).reverse();
            if(called)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WifiP2pActivity.getSendReceive().getPrintWriter().println("U");
                            WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
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
                if(currPlayer.equals(playerClient) && isShowing())
                {
                    if(clickedReverse((int)motionEvent.getX(), (int)motionEvent.getY(), true))
                    {
                        removeDice();
                        addRolledDice();
                    }
                    if(isFirstStep())
                    {
                        currPlayerClickToRollDice((int) motionEvent.getX(), (int) motionEvent.getY(), true);
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
                        if(playerClient.makeCount((int)motionEvent.getX(), (int)motionEvent.getY(), getDieValueToCount(), true))
                        {
                            try {
                                Thread.sleep(100);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            removeDiceAndDieValue(getClickedDie(), true);
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

    public void currPlayerClickToRollDice(int x, int y, boolean called)
    {
        RectF diceHouseRect = new RectF();
        diceHouseRect.left = board.getDiceHouse().getX();
        diceHouseRect.top = board.getDiceHouse().getY();
        diceHouseRect.right = diceHouseRect.left + board.getDiceHouse().getWidth();
        diceHouseRect.bottom = diceHouseRect.top + board.getDiceHouse().getHeight();

        if(diceHouseRect.contains(x, y) && isRollDiePossible())
        {
            if(called)
            {
                setHighlighterValues((int)diceHouseRect.left, (int)diceHouseRect.top, (int)diceHouseRect.right, (int)diceHouseRect.bottom);
                currPlayer.rollDice(0, 0);
            }
            else
            {
                currPlayer.rollDice(getRolledDieValue()[0], getRolledDieValue()[1]);
            }
            setRollDiePossible(false);
            addRolledDice();

            if(called)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int a = currPlayer.getPlayerDieValue().get(currPlayer.getPlayerDieValue().size() - 2);
                        int b = currPlayer.getPlayerDieValue().get(currPlayer.getPlayerDieValue().size() - 1);
                        try {
                            WifiP2pActivity.getSendReceive().getPrintWriter().println("R " + a + " " + b);
                            WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                        } catch (Exception ex) {

                        }
                    }
                });
                thread.start();
            }

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

    public void removeDiceAndDieValue(Die die, boolean called)
    {
        final int faceValue = die.getDieFaceValue();
        final String dieName = die.getDieName();

        for(int i = 0; i < currPlayer.getPlayerDieValue().size(); i++)
        {
            if(currPlayer.getPlayerDieValue().get(i) == faceValue)
            {
                currPlayer.getPlayerDieValue().remove(i);
                die.setVisible(false);
                if(called)
                {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WifiP2pActivity.getSendReceive().getPrintWriter().println("D " + dieName);
                                WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
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

    public Die[] getCountingDice()
    {
        return countingDice;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isIncomingThreadRunning() {
        return incomingThreadRunning;
    }

    public void setIncomingThreadRunning(boolean incomingThreadRunning) {
        this.incomingThreadRunning = incomingThreadRunning;
    }
}
