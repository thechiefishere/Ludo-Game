package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;

import com.jbaba.ludo.canvases.GameCanvas1;

public class GameActivity extends AppCompatActivity {

    private GameCanvas1 gameCanvas1;
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
            gameCanvas1 = new GameCanvas1(this, size, playerHouse);
        else
            gameCanvas1 = (GameCanvas1) savedInstanceState.getSerializable("Canvas");

        if(gameCanvas1.getParent() != null)
            ((ViewGroup)gameCanvas1.getParent()).removeView(gameCanvas1);

        setContentView(gameCanvas1);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameCanvas1.pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameCanvas1.resumeGame();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("Canvas", gameCanvas1);
    }
}
