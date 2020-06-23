package com.example.hambola;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class splashScreen extends AppCompatActivity {

    MediaPlayer player;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        player = MediaPlayer.create(this,R.raw.yes);
        player.start();
    }


    public void mainAct(View v)
    {
        v.setEnabled(false);

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        Button btn = findViewById(R.id.button6);
        btn.animate().alpha(0f).setDuration(2000);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                player.pause(); startActivity(intent); finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable,2500);

    }
}
