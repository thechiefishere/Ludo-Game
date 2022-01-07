package com.jbaba.ludo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.jbaba.ludo.canvases.GameCanvasServer;

public class GameServerActivity extends AppCompatActivity {

    private GameCanvasServer gameCanvasServer;
    public static final String CHOSENCOLOR = "colorChoice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Intent intent = getIntent();
        String playerHouse = intent.getStringExtra(CHOSENCOLOR);

        if(savedInstanceState == null)
            gameCanvasServer = new GameCanvasServer(this, size, playerHouse);
        else
            gameCanvasServer = (GameCanvasServer) savedInstanceState.getSerializable("Canvas");

        if(gameCanvasServer.getParent() != null)
            ((ViewGroup)gameCanvasServer.getParent()).removeView(gameCanvasServer);

        setContentView(gameCanvasServer);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameCanvasServer.pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameCanvasServer.resumeGame();
    }

    /*public void controlFPS()
    {
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        long timeToSleep = 15 - timeThisFrame;
        if(timeThisFrame > 0)
        {
            fps = (int) 1000 / timeThisFrame;
        }
        if(timeThisFrame > 0)
        {
            try {
                thread.sleep(timeToSleep);
            } catch (InterruptedException ex) {

            }
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("Canvas", gameCanvasServer);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gameCanvasServer.getThread().interrupt();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try {
            //WifiP2pActivity.getSendReceive().getSocket().close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
