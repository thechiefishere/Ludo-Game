package com.jbaba.ludo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.jbaba.ludo.canvases.GameCanvasClient;
import com.jbaba.ludo.canvases.GameCanvasServer;

public class GameClientActivity extends AppCompatActivity {

    private GameCanvasClient gameCanvasClient;
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
            gameCanvasClient = new GameCanvasClient(this, size, playerHouse);
        else
            gameCanvasClient = (GameCanvasClient) savedInstanceState.getSerializable("Canvas");

        if(gameCanvasClient.getParent() != null)
            ((ViewGroup)gameCanvasClient.getParent()).removeView(gameCanvasClient);

        setContentView(gameCanvasClient);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameCanvasClient.pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameCanvasClient.resumeGame();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("Canvas", gameCanvasClient);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gameCanvasClient.getThread().interrupt();
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
