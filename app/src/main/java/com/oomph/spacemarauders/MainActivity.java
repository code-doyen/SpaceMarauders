package com.oomph.spacemarauders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private ImageButton buttonPlay;
    //high score button
    private ImageButton buttonScore;
    static MediaPlayer titleSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //initializing the highscore button
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);

        //adding a click listener
        buttonPlay.setOnClickListener(this);

        //setting the on click listener to high score button
        buttonScore.setOnClickListener(this);//initializing the media players for the game sounds

        //initializing the media players for the game sounds
        titleSound = MediaPlayer.create(this, R.raw.skyfiretitlescreen);
        //loop game soundtrack
        titleSound.setLooping(true);
        //starting the game music as the game starts
        titleSound.start();

    }

    @Override
    public void onClick(View v) {

        if (v == buttonPlay) {
            titleSound.stop();
            //the transition from MainActivity to GameActivity
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        }
        if (v == buttonScore) {
            titleSound.stop();
            //the transition from MainActivity to HighScore activity
            startActivity(new Intent(MainActivity.this, HighScoreActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        titleSound.stop();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        titleSound.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        titleSound.start();
    }

}
