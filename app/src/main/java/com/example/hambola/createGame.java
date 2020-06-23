package com.example.hambola;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Clock;


public class createGame extends AppCompatActivity {

    String ID;


    public void CreateGame(View v)
    {
        v.setEnabled(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child(ID);
        DatabaseReference acv = reference.child(ID);
        EditText editText = findViewById(R.id.editText1);
        String name = editText.getText().toString();
        acv.child(name);
        DatabaseReference host = acv.child(name);
        host.setValue("start");
        acv.child("running_status");
        DatabaseReference run = acv.child("running_status");
        run.setValue(0);

        Toast.makeText(this, "Game Created", Toast.LENGTH_LONG).show();

        Intent in = new Intent(this,members.class);
        in.putExtra("id",ID);
        in.putExtra("host",1);
        in.putExtra("playerId",host.getKey());
        startActivity(in);
        finish();

    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ProgressBar pg = findViewById(R.id.progressBar2);
        pg.setAlpha(0f);pg.setEnabled(false);
        Intent in = getIntent();
        ID = in.getStringExtra("room");
        TextView RoomId = findViewById(R.id.textView2);
        RoomId.setText("Room ID: "+ID);
        TextView textView = findViewById(R.id.textView3);
        textView.setSelected(true);
    }

    public void Home(View v)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void add(View v)
    {
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
