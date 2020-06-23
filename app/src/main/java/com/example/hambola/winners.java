package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class winners extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);

    }

    @Override
    protected  void onStart()
    {
        super.onStart();
        String roomId = getIntent().getStringExtra("id");

        assert roomId != null;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(roomId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    TextView t = new TextView(getApplicationContext());
                    t.setText(snapshot.getKey()+" secured "+snapshot.getValue());

                    LinearLayout layout = findViewById(R.id.status);

                    layout.addView(t);

                    if(snapshot.getKey() != null)
                    {
                        DatabaseReference reference1 = reference.child(snapshot.getKey());
                        reference1.removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
