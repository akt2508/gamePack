package com.example.hambola;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MaterialButton button = findViewById(R.id.button);
        MaterialButton button1 = findViewById(R.id.button2);
        button.setEnabled(true);
        button1.setEnabled(true);
    }

    public void join(View v)
    {
        v.setEnabled(false);
        Intent in = new Intent(getApplicationContext(), join.class);
        startActivity(in);
    }

    public void create(View v)
    {
        v.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                String roomId = "";

                Character[] character = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};


                for(int i = 0 ; i < 4; i++)
                {
                    Random random = new Random();
                    char c = character[random.nextInt(26)];
                    roomId += c;
                }

                Log.i("Room id",""+ roomId);
                Intent in = new Intent(getApplicationContext(), createGame.class);
                in.putExtra("room", roomId);
                startActivity(in);
            }
        },500);

    }

    public void Home(View v)
    {

    }
    public void add(View v)
    {

        Intent intent = new Intent(getApplicationContext(),createGame.class);
        startActivity(intent);

    }
    public void availableRooms(View v)
    {

        Toast.makeText(this, "This feature will be made available soon.", Toast.LENGTH_SHORT).show();

    }
    public void settings(View v)
    {

        Intent intent = new Intent(getApplicationContext(),settings.class);
        startActivity(intent);

    }
}
