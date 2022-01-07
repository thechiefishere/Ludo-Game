package com.jbaba.ludo.concreteclasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jbaba.ludo.R;
import com.jbaba.ludo.abstractclasses.AbsSpriteImg;

public class PlayPauseButton extends AbsSpriteImg
{
    transient Bitmap[] playPause = new Bitmap[2];
    private boolean playing;

    public PlayPauseButton()
    {
        super();
        initSprite();
    }

    @Override
    public void initSprite()
    {
        setX(MyConsts.playPauseButtonX);
        setY(MyConsts.playPauseButtonY);
        setVisible(true);
        setActive(false);
        setPlaying(true);

        Bitmap pause = BitmapFactory.decodeResource(MyConsts.context.getResources(), R.drawable.pause);
        pause = Bitmap.createScaledBitmap(pause, MyConsts.reverseButtonWidth, MyConsts.reverseButtonHeight, false);
        Bitmap play = BitmapFactory.decodeResource(MyConsts.context.getResources(), R.drawable.play);
        play = Bitmap.createScaledBitmap(play, MyConsts.reverseButtonWidth, MyConsts.reverseButtonHeight, false);

        playPause[0] = pause;
        playPause[1] = play;
        setBitmap(playPause[0]);
    }

    @Override
    public void updateSprite()
    {

    }

    @Override
    public void drawSprite(Canvas canvas, Paint paint)
    {
        if(isPlaying())
            canvas.drawBitmap(playPause[0], getX(), getY(), paint);
        else
            canvas.drawBitmap(playPause[1], getX(), getY(), paint);
    }

    public Bitmap[] getPlayPause() {
        return playPause;
    }

    public void setPlayPause(Bitmap[] playPause) {
        this.playPause = playPause;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
