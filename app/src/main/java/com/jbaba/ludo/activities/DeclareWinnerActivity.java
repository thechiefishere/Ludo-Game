package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jbaba.ludo.R;
import com.jbaba.ludo.concreteclasses.MyConsts;

public class DeclareWinnerActivity extends AppCompatActivity {

    private TextView winnerTextView;
    private Button restartBtn;

    public static final String WINNER = "WhoWon";
    public static final String HOSTORCLIENT = "Status";
    private String who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_declare_winner);

        initComponent();
    }

    public void initComponent()
    {
        Intent intent = getIntent();
        String toPass = intent.getStringExtra(WINNER);
        if(toPass.equals("Player"))
            toPass = "You";

        winnerTextView = (TextView) findViewById(R.id.winner_id);
        winnerTextView.setText(toPass.toUpperCase());
        restartBtn = (Button) findViewById(R.id.restart_button);
    }

    public void restartBtnClicked(View view)
    {
        Intent intent = getIntent();
        who = intent.getStringExtra(HOSTORCLIENT);

        intent = new Intent(MyConsts.context, HouseChoiceMultiplayerActivity.class);
        intent.putExtra(HouseChoiceMultiplayerActivity.HOSTORCLIENT, who);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(WifiP2pActivity.getSendReceive() != null)
        {
            Intent intent = new Intent(MyConsts.context, HouseChoiceMultiplayerActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(MyConsts.context, HouseChoiceComputerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(WifiP2pActivity.getSendReceive() != null)
        {
            try {
                //WifiP2pActivity.getSendReceive().getSocket().close();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}