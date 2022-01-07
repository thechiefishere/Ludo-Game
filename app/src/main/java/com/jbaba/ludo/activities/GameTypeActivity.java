package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jbaba.ludo.R;
import com.jbaba.ludo.concreteclasses.MyConsts;

public class GameTypeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button computerButton;
    private Button friendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game_type);

        initComponent();
    }

    public void initComponent()
    {
        computerButton = (Button) findViewById(R.id.computer_click);
        computerButton.setOnClickListener(this);
        friendButton = (Button) findViewById(R.id.friend_click);
        friendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.computer_click)
        {
            Intent intent = new Intent(this, HouseChoiceComputerActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.friend_click)
        {
            Intent intent = new Intent(this, WifiP2pActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
    }
}