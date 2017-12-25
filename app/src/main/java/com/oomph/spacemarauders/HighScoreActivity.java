package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4;

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
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView1.setText("1. "+sharedPreferences.getString("scoreName1","DBV")+" "+sharedPreferences.getInt("score1",0));
        textView2.setText("2. "+sharedPreferences.getString("scoreName2","MAV")+" "+sharedPreferences.getInt("score2",0));
        textView3.setText("3. "+sharedPreferences.getString("scoreName3","HMV")+" "+sharedPreferences.getInt("score3",0));
        textView4.setText("4. "+sharedPreferences.getString("scoreName4","BLV")+" "+sharedPreferences.getInt("score4",0));

    }
    @Override
    public void onBackPressed() {
        bravePilotsSound.stop();
        startActivity(new Intent(this, MainActivity.class));
    }

}
