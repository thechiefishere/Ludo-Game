package com.jbaba.ludo.abstractclasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jbaba.ludo.concreteclasses.MyConsts;

import java.util.ArrayList;

public abstract class AbsGameCanvas extends AbsCanvas
{
    private ArrayList<AbsSprite> spriteAry;
    private int sleepTime;

    public AbsGameCanvas(Context context, Point point)
    {
        super(context, point);

        spriteAry = new ArrayList<>();
        setSleepTime(MyConsts.sleepTime);
    }

    @Override
    public void run()
    {
        try {
            while(isThreadRunning())
            {
                while(isShowing())
                {
                    updateComponent();
                    drawComponent();
                    Thread.sleep(getSleepTime());
                }
                //drawComponent();
            }
        } catch (InterruptedException iex) {

        }
    }

    public void updateComponent()
    {
        for(AbsSprite sprite : getSpriteAry())
            if(sprite.isActive())
                sprite.updateSprite();
    }

    public void drawComponent()
    {
        if(getSurfaceHolder().getSurface().isValid())
        {
            setCanvas(getSurfaceHolder().lockCanvas());
            getCanvas().drawColor(Color.MAGENTA);
            synchronized (getSpriteAry())
            {
                for(AbsSprite sprite : getSpriteAry())
                    if(sprite.isVisible())
                        sprite.drawSprite(getCanvas(), getPaint());
            }

            drawBoardDisplays(getCanvas(), getPaint());

            getSurfaceHolder().unlockCanvasAndPost(getCanvas());
        }
    }

    public abstract void drawBoardDisplays(Canvas canvas, Paint paint);

    public synchronized ArrayList<AbsSprite> getSpriteAry()
    {
        return spriteAry;
    }

    public void setSpriteAry(ArrayList<AbsSprite> spriteAry) {
        this.spriteAry = spriteAry;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
