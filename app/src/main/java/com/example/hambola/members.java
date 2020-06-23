package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class members extends AppCompatActivity {

    String roomId;
    ArrayList<String> members = new ArrayList<>();
    String playerId;
    DatabaseReference room;
    int host;

    @Override
    protected void onStart()
    {
        super.onStart();

    }


    public void game(View v)
    {

        room.setValue(1);
        room.child("start");
        DatabaseReference reference = room.child("start");
        reference.setValue(1);

        Intent n = new Intent(this,tambola.class);
        n.putExtra("id",roomId);
        n.putExtra("host",host);
        n.putExtra("playerId",playerId);
        startActivity(n);
        finish();

    }

    public void players()
    {

        room = FirebaseDatabase.getInstance().getReference().child(roomId);

        room.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String key = snapshot.getKey();
                    if(key != null && !key.equals("running_status"))
                    {
                        int check = 1;
                        if(members.size() > 0)
                        {
                            for(String names : members)
                            {

                                if(names.equals(key))
                                {
                                    check = 0;
                                }

                            }
                        }
                        if(check == 1)
                        {
                            members.add(key);
                            addDisplay(key);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        room.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if(dataSnapshot.getChildrenCount() > 1)
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String key = snapshot.getKey();
                        if(key != null && !key.equals("running_status"))
                        {
                            Toast.makeText(members.this, key, Toast.LENGTH_SHORT).show();
                            if(key.equals("start"))
                            {

                                Intent n = new Intent(getApplicationContext(),tambola.class);
                                n.putExtra("id",roomId);
                                n.putExtra("host",host);
                                n.putExtra("playerId",playerId);
                                startActivity(n);
                                finish();

                            }
                            else
                            {
                                addDisplay(key);
                                members.add(key);
                            }
                        }
                        else
                        {
                            Log.i("lol","null key");
                        }
                    }
                }
                else {


                    Log.i("lol","one child only");


                    String key = dataSnapshot.getKey();
                    if(key != null && !key.equals("running_status"))
                    {
                        Toast.makeText(members.this, key, Toast.LENGTH_SHORT).show();
                        if(key.equals("start"))
                        {

                            Intent n = new Intent(getApplicationContext(),tambola.class);
                            n.putExtra("id",roomId);
                            n.putExtra("host",host);
                            n.putExtra("playerId",playerId);
                            startActivity(n);
                            finish();

                        }
                        else
                        {
                            addDisplay(key);
                            members.add(key);
                        }
                    }
                    else
                    {
                        Log.i("lol","null key");
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {
              if(dataSnapshot.getChildrenCount() > 1)
              {
                  for(DataSnapshot snapshot : dataSnapshot.getChildren())
                  {
                      removeDisplay(snapshot.getKey());
                      members.remove(snapshot.getKey());
                  }
              }
              else
              {

                  String key = dataSnapshot.getKey();
                  removeDisplay(key);
                  members.remove(key);

              }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(members.this, "Error when updating or removing details", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addDisplay(String name)
    {

        LinearLayout members  = findViewById(R.id.member);

        TextView t = new TextView(this);
        t.setText(name.toUpperCase());
        members.addView(t);
        Toast.makeText(this, name+" Joined", Toast.LENGTH_SHORT).show();

    }

    private void removeDisplay(String name)
    {
        LinearLayout members  = findViewById(R.id.member);

        for(int i =0; i < members.getChildCount();i++)
        {
            TextView t = (TextView) members.getChildAt(i);
            if(t.getText().toString().toLowerCase().equals(name.toLowerCase()))
            {

                members.removeViewAt(i);
                Toast.makeText(this, name+" left", Toast.LENGTH_SHORT).show();

            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        Button btn = findViewById(R.id.button5);

        Intent in = getIntent();

        playerId = in.getStringExtra("playerId");

        host = in.getIntExtra("host",3);

        if(host == 3)
        {

            Log.i("fuck","cannot get host status into members");

        }

        if(host == 1)
        {
            btn.setEnabled(true);
        }
        if(host == 0)
        {
            btn.setEnabled(false);
        }

        roomId = in.getStringExtra("id");

        players();

    }


    public void Home(View v)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
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
