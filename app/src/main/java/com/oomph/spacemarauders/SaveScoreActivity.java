package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.view.View;

public class SaveScoreActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private MediaPlayer bravePilotsSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        //initializing the media players for the game sounds
        bravePilotsSound = MediaPlayer.create(this, R.raw.bravepilots);
        //loop game soundtrack
        bravePilotsSound.setLooping(true);
        bravePilotsSound.start();
        setContentView(R.layout.activity_save_score);
    }
    public void saveMessage(View view){
        bravePilotsSound.stop();
        EditText editText = (EditText) findViewById(R.id.editInitials);
        String initials = editText.getText().toString().toUpperCase();

        //Get the intent that stated tis activity and extract the string
        Intent intent = getIntent();
        int pos = intent.getIntExtra(GameView.EXTRA_MESSAGE,0);

        //sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);
        //storing the scores through shared Preferences
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString("scoreName" + (pos+1), initials);
        e.apply();

        //return to Main home screen
        startActivity(new Intent(this, HighScoreActivity.class));
    }
}