package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jbaba.ludo.R;
import com.jbaba.ludo.concreteclasses.MyConsts;

import java.io.IOException;

public class HouseChoiceMultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String HOSTORCLIENT = "Status";

    private TextView statusTextView;
    private TextView redText;
    private TextView yellowText;
    private TextView greenText;
    private TextView blueText;
    private ImageButton davidoBtn;
    private ImageButton olamideBtn;
    private ImageButton wixkidBtn;
    private ImageButton tiwaBtn;
    private Button startBtn;

    private boolean status;
    private String toPass;
    private int position;
    private String buttonName;
    private String who;
    private boolean sendPossible;

    private boolean davidoBtnEnabled;
    private boolean olamideBtnEnabled;
    private boolean wizkidBtnEnabled;
    private boolean tiwaButtonEnabled;

    private Thread incomingThread;
    private volatile boolean threadRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_house_choice_multiplayer);

        initComponent();
        incomingUpdate();
    }

    public void initComponent()
    {
        statusTextView = (TextView) findViewById(R.id.status);
        redText = (TextView) findViewById(R.id.red_text);
        yellowText = (TextView) findViewById(R.id.yellow_text);
        greenText = (TextView) findViewById(R.id.green_text);
        blueText = (TextView) findViewById(R.id.blue_text);
        davidoBtn = (ImageButton) findViewById(R.id.red_button);
        davidoBtn.setOnClickListener(this);
        olamideBtn = (ImageButton) findViewById(R.id.yellow_button);
        olamideBtn.setOnClickListener(this);
        wixkidBtn = (ImageButton) findViewById(R.id.green_button);
        wixkidBtn.setOnClickListener(this);
        tiwaBtn = (ImageButton) findViewById(R.id.blue_button);
        tiwaBtn.setOnClickListener(this);
        startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);

        Intent intent = getIntent();
        who = intent.getStringExtra(HOSTORCLIENT);
        if(who.equals("Host"))
            status = true;
        if(status)
            statusTextView.setText("Choose a color");
        toPass = "";

        davidoBtnEnabled = true;
        olamideBtnEnabled = true;
        wizkidBtnEnabled = true;
        tiwaButtonEnabled = true;

        threadRunning = true;
    }

    public void incomingUpdate()
    {
        incomingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isThreadRunning())
                {
                    Log.d("Debugging", "Am up thread while loop");
                    try {
                        String position = WifiP2pActivity.getSendReceive().getBufferedReader().readLine();
                        if(position.equals("DONE"))
                            break;

                        if(position != null)
                        {
                            buttonName = "";
                            int pos = Integer.parseInt(position);
                            status = true;

                            if(pos == 0)
                                buttonName = "davidoBtn";
                            else if(pos == 1)
                                buttonName = "olamideBtn";
                            else if(pos == 2)
                                buttonName = "wixkidBtn";
                            else if(pos == 3)
                                buttonName = "tiwaBtn";

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    statusTextView.setText("Choose a color");
                                    if(buttonName.equals("davidoBtn"))
                                    {
                                        davidoBtnEnabled = false;
                                        redText.setTextColor(Color.RED);
                                        redText.setTypeface(null, Typeface.BOLD);
                                    }
                                    else if(buttonName.equals("olamideBtn"))
                                    {
                                        olamideBtnEnabled = false;
                                        yellowText.setTextColor(Color.YELLOW);
                                        yellowText.setTypeface(null, Typeface.BOLD);
                                    }
                                    else if(buttonName.equals("wixkidBtn"))
                                    {
                                        wizkidBtnEnabled = false;
                                        greenText.setTextColor(Color.GREEN);
                                        greenText.setTypeface(null, Typeface.BOLD);
                                    }
                                    else if(buttonName.equals("tiwaBtn"))
                                    {
                                        tiwaButtonEnabled = false;
                                        blueText.setTextColor(Color.BLUE);
                                        blueText.setTypeface(null, Typeface.BOLD);
                                    }
                                }
                            });
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.d("Debugging", "Am down thread while loop");
                }
                Log.d("HouseChoiceMultipla", "Thread interrupted");
            }
        });
        incomingThread.start();
    }

    @Override
    public void onClick(View view)
    {
        if(status)
        {
            sendPossible = false;
            if(view.getId() == R.id.red_button)
            {
                if(davidoBtnEnabled)
                {
                    position = 0;
                    toPass += position;
                    davidoBtnEnabled = false;
                    redText.setTextColor(Color.RED);
                    redText.setTypeface(null, Typeface.BOLD);
                    sendPossible = true;
                }
                else
                    Toast.makeText(getApplicationContext(), "Button has been selected", Toast.LENGTH_SHORT).show();
            }
            else if(view.getId() == R.id.yellow_button)
            {
                if(olamideBtnEnabled)
                {
                    position = 1;
                    toPass += position;
                    olamideBtnEnabled = false;
                    yellowText.setTextColor(Color.YELLOW);
                    yellowText.setTypeface(null, Typeface.BOLD);
                    sendPossible = true;
                }
                else
                    Toast.makeText(getApplicationContext(), "Button has been selected", Toast.LENGTH_SHORT).show();
            }
            else if(view.getId() == R.id.green_button)
            {
                if(wizkidBtnEnabled)
                {
                    position = 2;
                    toPass += position;
                    wizkidBtnEnabled = false;
                    greenText.setTextColor(Color.GREEN);
                    greenText.setTypeface(null, Typeface.BOLD);
                    sendPossible = true;
                }
                else
                    Toast.makeText(getApplicationContext(), "Button has been selected", Toast.LENGTH_SHORT).show();
            }
            else if(view.getId() == R.id.blue_button)
            {
                if(tiwaButtonEnabled)
                {
                    position = 3;
                    toPass += position;
                    tiwaButtonEnabled = false;
                    blueText.setTextColor(Color.BLUE);
                    blueText.setTypeface(null, Typeface.BOLD);
                    sendPossible = true;
                }
                else
                    Toast.makeText(getApplicationContext(), "Button has been selected", Toast.LENGTH_SHORT).show();
            }

            if(toPass.length() == 2)
            {
                statusTextView.setText("Start Game");
                startBtn.setEnabled(true);
            }

            if(sendPossible)
            {
                status = false;
                if (toPass.length() < 2)
                    statusTextView.setText("Wait");

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WifiP2pActivity.getSendReceive().getPrintWriter().println(position);
                        WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                    }
                });
                thread.start();
            }
        }

        if(view.getId() == R.id.start_btn)
        {
            Log.d("HouseChoiceMulti", "I enterred startButtonclicked");
            setThreadRunning(false);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    WifiP2pActivity.getSendReceive().getPrintWriter().println("DONE");
                    WifiP2pActivity.getSendReceive().getPrintWriter().flush();
                }
            });
            thread.start();
            sleepMainThread(100);

            if(toPass.length() == 2)
            {
                if(who.equals("Host"))
                {
                    Intent intent = new Intent(MyConsts.context, GameServerActivity.class);
                    intent.putExtra(GameServerActivity.CHOSENCOLOR, toPass);
                    MyConsts.context.startActivity(intent);
                }
                else if(who.equals("Client"))
                {
                    Intent intent = new Intent(MyConsts.context, GameClientActivity.class);
                    intent.putExtra(GameClientActivity.CHOSENCOLOR, toPass);
                    MyConsts.context.startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setThreadRunning(false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WifiP2pActivity.getSendReceive().getPrintWriter().println("DONE");
                WifiP2pActivity.getSendReceive().getPrintWriter().flush();
            }
        });
        thread.start();
        sleepMainThread(100);

        try {
            WifiP2pActivity.getSendReceive().getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WifiP2pActivity.setSendReceive(null);
        WifiP2pActivity.disconnect();
        if(WifiP2pActivity.wifiOnByActivity)
            WifiP2pActivity.wifiManager.setWifiEnabled(false);
        Intent intent = new Intent(this, GameTypeActivity.class);
        startActivity(intent);
    }

    public void sleepMainThread(int sleepTime)
    {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }
}