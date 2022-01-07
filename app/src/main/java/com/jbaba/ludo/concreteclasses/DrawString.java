package com.jbaba.ludo.concreteclasses;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;
import java.util.StringTokenizer;

public class DrawString implements Serializable
{
    private String text;
    private int x;
    private int y;
    private int drawWidth;

    public DrawString(int x, int y, int drawWidth, String text)
    {
        this.x = x;
        this.y = y;
        this.drawWidth = drawWidth;
        this.text = text;
    }

    public void paintStringWithMargin(Canvas canvas, Paint paint)
    {
        StringTokenizer stk = new StringTokenizer(text);
        String toDraw = "";

        while(stk.hasMoreTokens())
        {
            String nextString = stk.nextToken();
            int lenght = (int) paint.measureText(toDraw + nextString);
            if(lenght <= drawWidth)
                toDraw += nextString + " ";
            else
            {
                lenght = (int) paint.measureText(toDraw);
                int halfOfDiff = 0;
                if(drawWidth > lenght)
                    halfOfDiff = (drawWidth - lenght) / 2;
                canvas.drawText(toDraw, x + halfOfDiff, y, paint);
                toDraw = "";
                toDraw += nextString + " ";
                y += paint.getTextSize();
            }
        }
        if(toDraw != null)
        {
            int lenght = (int) paint.measureText(toDraw);
            int halfOfDiff = (drawWidth - lenght) / 2;
            canvas.drawText(toDraw, x + halfOfDiff, y, paint);
        }
    }

    public void paintStringWithoutMargin(Canvas canvas, Paint paint)
    {
        StringTokenizer stk = new StringTokenizer(text);
        String toDraw = "";

        while(stk.hasMoreTokens())
        {
            String nextString = stk.nextToken();
            int lenght = (int) paint.measureText(toDraw + nextString);
            if(lenght <= drawWidth)
                toDraw += nextString + " ";
            else
            {
                lenght = (int) paint.measureText(toDraw);
                canvas.drawText(toDraw, x , y, paint);
                toDraw = "";
                toDraw += nextString + " ";
                y += paint.getTextSize();
            }
        }
        if(toDraw != null)
            canvas.drawText(toDraw, x, y, paint);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDrawWidth() {
        return drawWidth;
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
    }
}
