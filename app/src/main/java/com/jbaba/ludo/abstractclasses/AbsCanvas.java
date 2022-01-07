package com.jbaba.ludo.abstractclasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jbaba.ludo.concreteclasses.DrawString;
import com.jbaba.ludo.concreteclasses.Highlighter;
import com.jbaba.ludo.concreteclasses.MyConsts;

import java.io.Serializable;

public abstract class AbsCanvas extends SurfaceView implements Runnable, Serializable
{
    private transient SurfaceHolder surfaceHolder;
    private transient Thread animThread;
    private transient Canvas canvas;
    private transient Paint paint;
    private Highlighter highlighter;

    private MyConsts myConsts;
    private boolean showing;
    private boolean threadRunning;
    private DrawString drawString;

    private int majorSleepTime;

    public AbsCanvas(Context context, Point point)
    {
        super(context);
        myConsts = new MyConsts(context, point);
        initComponent();
    }

    private void initComponent()
    {
        surfaceHolder = getHolder();
        animThread = null;
        paint = new Paint();
        highlighter = new Highlighter();

        setMajorSleepTime(MyConsts.majorSleepTime);
    }

    public void initAnimation()
    {
        if(animThread == null)
        {
            animThread = new Thread(this);
            animThread.start();
        }
    }

    public abstract void drawComponent();

    public void setHighlighterValues(int x, int y, int width, int height)
    {
        getHighlighter().setX(x);
        getHighlighter().setY(y);
        getHighlighter().setWidth(width);
        getHighlighter().setHeight(height);
        getHighlighter().setVisible(true);
    }

    public void startGame()
    {
        setThreadRunning(true);
        setShowing(true);
        initAnimation();
    }

    public void pauseGame()
    {
        setShowing(false);
    }

    public void resumeGame()
    {
        setShowing(true);
    }

    public void stopGame()
    {
        setShowing(false);
        setThreadRunning(false);
        animThread.interrupt();
        animThread = null;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public Thread getAnimThread() {
        return animThread;
    }

    public void setAnimThread(Thread animThread) {
        this.animThread = animThread;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public MyConsts getMyConsts() {
        return myConsts;
    }

    public void setMyConsts(MyConsts myConsts) {
        this.myConsts = myConsts;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }

    public DrawString getDrawString() {
        return drawString;
    }

    public void setDrawString(DrawString drawString) {
        this.drawString = drawString;
    }

    public int getMajorSleepTime() {
        return majorSleepTime;
    }

    public void setMajorSleepTime(int majorSleepTime) {
        this.majorSleepTime = majorSleepTime;
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
    }
}
