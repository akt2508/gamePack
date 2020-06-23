package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class join extends AppCompatActivity {


    public void joinGame(final View v)
    {
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setEnabled(true);
        progressBar.setAlpha(1f);
        v.setEnabled(false);

        EditText editText = findViewById(R.id.editText);
        final String id = editText.getText().toString().toLowerCase().trim();
        
        if(!id.equals(""))
        {
            
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(id);
            
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) 
                {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        if(snapshot.getKey() != null)
                        {
                            if(snapshot.getKey().equals("running_status"))
                            {
                                if((long)snapshot.getValue() == 0)
                                {

                                    EditText editText1  = findViewById(R.id.editText1);
                                    String name = editText1.getText().toString().trim();

                                    reference.child(name);

                                    DatabaseReference player = reference.child(name);

                                    player.setValue("start");

                                    Toast.makeText(getApplicationContext(), "Game Joined", Toast.
                                            LENGTH_LONG).show();

                                    progressBar.setAlpha(0f);
                                    progressBar.setEnabled(false);

                                    Intent in = new Intent(getApplicationContext(),members.class);
                                    in.putExtra("id",id);
                                    in.putExtra("host",0);
                                    in.putExtra("playerId",player.getKey());
                                    startActivity(in);
                                    finish();

                                }
                                else
                                {
                                    progressBar.setAlpha(0f);
                                    progressBar.setEnabled(false);
                                    v.setEnabled(true);
                                    Toast.makeText(join.this, "Game has already started", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            
        }
       
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        TextView textView = findViewById(R.id.textView3);

        textView.setSelected(true);

        ProgressBar pBar = findViewById(R.id.progressBar);

        pBar.setEnabled(false);

        pBar.setAlpha(0f);

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
