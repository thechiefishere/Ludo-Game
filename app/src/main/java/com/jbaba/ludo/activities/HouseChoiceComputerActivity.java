package com.jbaba.ludo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jbaba.ludo.R;

public class HouseChoiceComputerActivity extends AppCompatActivity implements View.OnClickListener{

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
    private StringBuffer toPass;
    private int position;

    private boolean davidoBtnEnabled;
    private boolean olamideBtnEnabled;
    private boolean wizkidBtnEnabled;
    private boolean tiwaButtonEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_house_choice_computer);

        initComponent();
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

        toPass = new StringBuffer("");
        status = true;

        davidoBtnEnabled = true;
        olamideBtnEnabled = true;
        wizkidBtnEnabled = true;
        tiwaButtonEnabled = true;
    }

    @Override
    public void onClick(View view)
    {
        if(status)
        {
            if(view.getId() == R.id.red_button)
            {
                if(davidoBtnEnabled)
                {
                    position = 0;
                    toPass.append(position);
                    davidoBtnEnabled = false;
                    redText.setTextColor(Color.RED);
                    redText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    davidoBtnEnabled = true;
                    int i = toPass.lastIndexOf(Character.toString('0'));
                    toPass.deleteCharAt(i);
                    redText.setTextColor(Color.BLACK);
                    redText.setTypeface(null, Typeface.NORMAL);
                }
            }
            else if(view.getId() == R.id.yellow_button)
            {
                if(olamideBtnEnabled)
                {
                    position = 1;
                    toPass.append(position);
                    olamideBtnEnabled = false;
                    yellowText.setTextColor(Color.YELLOW);
                    yellowText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    olamideBtnEnabled = true;
                    int i = toPass.lastIndexOf(Character.toString('1'));
                    toPass.deleteCharAt(i);
                    yellowText.setTextColor(Color.BLACK);
                    yellowText.setTypeface(null, Typeface.NORMAL);
                }
            }
            else if(view.getId() == R.id.green_button)
            {
                if(wizkidBtnEnabled)
                {
                    position = 2;
                    toPass.append(position);
                    wizkidBtnEnabled = false;
                    greenText.setTextColor(Color.GREEN);
                    greenText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    wizkidBtnEnabled = true;
                    int i = toPass.lastIndexOf(Character.toString('2'));
                    toPass.deleteCharAt(i);
                    greenText.setTextColor(Color.BLACK);
                    greenText.setTypeface(null, Typeface.NORMAL);
                }
            }
            else if(view.getId() == R.id.blue_button)
            {
                if(tiwaButtonEnabled)
                {
                    position = 3;
                    toPass.append(position);
                    tiwaButtonEnabled = false;
                    blueText.setTextColor(Color.BLUE);
                    blueText.setTypeface(null, Typeface.BOLD);
                }
                else
                {
                    tiwaButtonEnabled = true;
                    int i = toPass.lastIndexOf(Character.toString('3'));
                    toPass.deleteCharAt(i);
                    blueText.setTextColor(Color.BLACK);
                    blueText.setTypeface(null, Typeface.NORMAL);
                }
            }

            if(toPass.length() == 2)
            {
                statusTextView.setText("Start Game");
                startBtn.setEnabled(true);
            }
            else if(toPass.length() < 2)
            {
                statusTextView.setText("Select color");
                startBtn.setEnabled(false);
            }
            else if(toPass.length() > 2)
            {
                Toast.makeText(getApplicationContext(), "You've choosen more than 2 colors", Toast.LENGTH_SHORT).show();
                statusTextView.setText("Unselect a color");
                startBtn.setEnabled(false);
            }
        }

        if(view.getId() == R.id.start_btn)
        {
            if(toPass.length() == 2)
            {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(GameActivity.CHOSENCOLOR, toPass.toString());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, GameTypeActivity.class);
        startActivity(intent);
    }
}