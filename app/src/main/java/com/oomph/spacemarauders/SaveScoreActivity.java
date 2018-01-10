package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

public class SaveScoreActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private MediaPlayer bravePilotsSound;
    //Get the intent that stated tis activity and extract the string
    private Intent intent;
    private TextView textView;
    private int[] highScore = new int[4];
    private String[] highScoreNames = new String[4];
    private int score, pos = -1;
    private boolean isHighScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        //initializing the media players for the game sounds
        bravePilotsSound = MediaPlayer.create(this, R.raw.bravepilots);
        //loop game soundtrack
        bravePilotsSound.setLooping(true);
        bravePilotsSound.start();
        intent = getIntent();
        score = intent.getIntExtra(GameView.EXTRA_MESSAGE,0);
        highScore[0] = sharedPreferences.getInt("score1", 40);
        highScore[1] = sharedPreferences.getInt("score2", 30);
        highScore[2] = sharedPreferences.getInt("score3", 20);
        highScore[3] = sharedPreferences.getInt("score4", 10);
        highScoreNames[0] = sharedPreferences.getString("scoreName1", "DBV");
        highScoreNames[1] = sharedPreferences.getString("scoreName2", "MAV");
        highScoreNames[2] = sharedPreferences.getString("scoreName3", "HMV");
        highScoreNames[3] = sharedPreferences.getString("scoreName4", "BLV");
        isHighScore = false;
        //Assigning the position to the highscore
        for (int j = 0; j < highScore.length; j++) {
            if (highScore[j] < score) {
                pos = j;
                isHighScore = true;
                break;
            }
        }
        //Shifting the scores to the highscore integer array
        if(isHighScore) { //3, 2, 1, 0 : 0, 1, 2, 3
            for (int j = highScore.length - 1; j >= 0; j--) {//4
                if (j > 0 && j > pos) {
                    highScore[j] = highScore[j - 1];//3,2,1,0 : 2,1,0, -1
                    highScoreNames[j] = highScoreNames[j - 1];
                }

            }
        }
        //Assigning the high score to the highscore integer array
        highScore[pos] = score;
        if(!isHighScore){
            startActivity(new Intent(this, HighScoreActivity.class));
        }else {
            setContentView(R.layout.activity_save_score);
            //Capture the layout's TextView ans set the string as it text
            textView = (TextView) findViewById((R.id.scoreView));
            textView.setText(String.valueOf(highScore[pos]));
        }
    }
    public void saveMessage(View view){
        bravePilotsSound.stop();
        EditText editText = (EditText) findViewById(R.id.editInitials);
        String initials = editText.getText().toString().toUpperCase();
        switch(initials.length()){
            case 0:
                initials = "???";
                break;
            case 1:
                initials += "??";
                break;
            case 2:
                initials += "?";
                break;
        }
        //storing the scores through shared Preferences
        //storing the scores through shared Preferences
        SharedPreferences.Editor e = sharedPreferences.edit();
        for (int k = 0; k < highScore.length; k++) {
            e.putString("scoreName" + (k + 1), highScoreNames[k]);
            e.putInt("score" + (k + 1), highScore[k]);
        }
        e.putString("scoreName" + (pos+1), initials);
        e.putInt("score" + (pos+1), highScore[pos]);
        e.apply();

        startActivity(new Intent(this, HighScoreActivity.class));
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