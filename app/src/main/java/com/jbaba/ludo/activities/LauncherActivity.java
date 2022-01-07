package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;

import com.jbaba.ludo.OnClearFromRecentService;
import com.jbaba.ludo.canvases.LauncherCanvas;

public class LauncherActivity extends AppCompatActivity {

    private LauncherCanvas launcherCanvas;
    private Display display;
    private Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        launcherCanvas = new LauncherCanvas(this, size);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        setContentView(launcherCanvas);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        launcherCanvas.pauseGame();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        launcherCanvas.resumeGame();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("Canvas", launcherCanvas);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}