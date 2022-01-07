package com.jbaba.ludo.canvases;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;

import com.jbaba.ludo.abstractclasses.AbsCanvas;
import com.jbaba.ludo.activities.GameTypeActivity;
import com.jbaba.ludo.concreteclasses.DrawString;
import com.jbaba.ludo.concreteclasses.MyConsts;

public class LauncherCanvas extends AbsCanvas
{
    public LauncherCanvas(Context context, Point point)
    {
        super(context, point);
        startGame();
    }

    @Override
    public void run()
    {
        try {
            while(isThreadRunning())
            {
                while(isShowing())
                {
                    drawComponent();
                    Thread.sleep(getMajorSleepTime());
                }
            }
        } catch (InterruptedException iex) {

        }
    }

    @Override
    public void drawComponent()
    {
        if(getSurfaceHolder().getSurface().isValid())
        {
            setCanvas(getSurfaceHolder().lockCanvas());
            drawColors();
            drawLetters();
            drawTapScreen();
            getSurfaceHolder().unlockCanvasAndPost(getCanvas());
        }
    }

    public void drawColors()
    {
        int quarterOfScreen = MyConsts.screenWidth / 4;
        getPaint().setColor(Color.RED);
        getCanvas().drawRect(0, 0, quarterOfScreen, MyConsts.boardHeight, getPaint());
        getPaint().setColor(Color.YELLOW);
        getCanvas().drawRect(quarterOfScreen, 0, 2 * quarterOfScreen, MyConsts.boardHeight, getPaint());
        getPaint().setColor(Color.GREEN);
        getCanvas().drawRect(2 * quarterOfScreen, 0, 3 * quarterOfScreen, MyConsts.boardHeight, getPaint());
        getPaint().setColor(Color.BLUE);
        getCanvas().drawRect(3 * quarterOfScreen, 0, 4 * quarterOfScreen, MyConsts.boardHeight, getPaint());
    }

    public void drawLetters()
    {
        getPaint().setTextSize(MyConsts.launcherTextSize);
        int quarterOfScreen = MyConsts.screenWidth / 4;
        int x = quarterOfScreen / 8;
        int y = 0;
        int colors[] = {Color.WHITE, Color.BLACK};
        String word = "LUDO";
        setDrawString(new DrawString(x, y, (int)MyConsts.launcherDrawWidth, ""));
        for(int i = 0; i < word.length(); i++)
        {
            if(i % 2 == 0)
                getPaint().setColor(colors[0]);
            else
                getPaint().setColor(colors[1]);
            String text = Character.toString(word.charAt(i));
            getDrawString().setText(text);
            getDrawString().paintStringWithoutMargin(getCanvas(), getPaint());
            getDrawString().setX(((i + 1) * quarterOfScreen) + x);
            getDrawString().setY(y);
        }
    }

    public void drawTapScreen()
    {
        getPaint().setTextSize(MyConsts.launcherTextSize / 10);
        getPaint().setColor(Color.BLACK);
        int x = MyConsts.screenWidth / 4;
        int y = (MyConsts.boardHeight / 4) * 3;
        String text = "TAP SCREEN";
        getDrawString().setX(x);
        getDrawString().setY(y);
        getDrawString().setDrawWidth(x * 2);
        getDrawString().setText(text);
        getDrawString().paintStringWithMargin(getCanvas(), getPaint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (MotionEvent.ACTION_MASK & motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                Intent intent = new Intent(MyConsts.context, GameTypeActivity.class);
                MyConsts.context.startActivity(intent);
            }
        }
        return false;
    }
}
