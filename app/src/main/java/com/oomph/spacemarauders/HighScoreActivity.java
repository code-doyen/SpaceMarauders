package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity {

    TextView textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12;

    SharedPreferences sharedPreferences;
    static MediaPlayer bravePilotsSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing the media players for the game sounds
        bravePilotsSound = MediaPlayer.create(this, R.raw.bravepilots);
        //loop game soundtrack
        bravePilotsSound.setLooping(true);
        bravePilotsSound.start();

        setContentView(R.layout.activity_high_score);

        //initializing the textViews
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView11 = (TextView) findViewById(R.id.textView11);
        textView12 = (TextView) findViewById(R.id.textView12);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView5.setText(sharedPreferences.getString("scoreName1","DBV"));
        textView6.setText(sharedPreferences.getString("scoreName2","MAV"));
        textView7.setText(sharedPreferences.getString("scoreName3","HMV"));
        textView8.setText(sharedPreferences.getString("scoreName4","BLV"));
        textView9.setText(String.valueOf(sharedPreferences.getInt("score1",40)));
        textView10.setText(String.valueOf(sharedPreferences.getInt("score2",30)));
        textView11.setText(String.valueOf(sharedPreferences.getInt("score3",20)));
        textView12.setText(String.valueOf(sharedPreferences.getInt("score4",10)));

    }
    @Override
    public void onBackPressed() {
        bravePilotsSound.stop();
       // startActivity(new Intent(this, MainActivity.class));
        startActivity(new Intent(this, MainActivity.class)
                .addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        finish();
    }
    //pausing the game music when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        bravePilotsSound.pause();
    }

    //running the game music when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        bravePilotsSound.start();
    }

}
