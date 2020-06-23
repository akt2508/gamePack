package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
;

public class tambola extends AppCompatActivity {

    boolean corners=true,top=true,lower=true,house=true;
    ArrayList<Integer> tambolaNums = new ArrayList<>();
    MediaPlayer mediaPlayer;
    MediaPlayer player;
    MediaPlayer amazing;
    int keep_running = 1;
    String playerId;
    DatabaseReference reference;
    int tambolaNumber;
    TextView status;
    int host;
    int[] colors = new int[]{Color.WHITE,Color.GREEN,Color.MAGENTA,Color.DKGRAY,Color.BLACK,Color.BLUE,Color.CYAN,Color.RED,Color.YELLOW};
    String roomId;
    ArrayList<View> numView = new ArrayList<>();
    TableRow r1;TableRow r2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambola);
        Intent intent = getIntent();

        roomId = intent.getStringExtra("id");

        host = intent.getIntExtra("host",3);

        playerId = intent.getStringExtra("playerId");

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mediaPlayer = MediaPlayer.create(this, R.raw.tick);

        player = MediaPlayer.create(this,R.raw.yeaaah);

        amazing = MediaPlayer.create(this,R.raw.amazing);

        status = findViewById(R.id.textView7);

        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);

        generateNums();

        CountDownTimer timer = new CountDownTimer(5000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished)
            {

                int secondsLeft = (int) millisUntilFinished/1000;

                status.setText("Tambola starting in "+secondsLeft+" seconds");

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish()
            {

                TextView t = findViewById(R.id.textView8);
                t.setAlpha(0f);
                t.setEnabled(false);

                reference = FirebaseDatabase.getInstance().getReference().child(roomId);
                updateNum();
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                        if(dataSnapshot.getChildrenCount() > 1)
                        {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {

                                if(snapshot.getValue() != null && snapshot.getKey() != null)
                                {
                                    if(snapshot.getKey().equals("start"))
                                    {
                                        int ran = new Random().nextInt(colors.length);
                                        status.setTextColor(colors[ran]);
                                        tambolaNumber = (Integer) snapshot.getValue();
                                        if(tambolaNumber == 120)
                                        {
                                            keep_running =0 ;
                                            status.setText("Someone Won the game");
                                            CountDownTimer timer1 = new CountDownTimer(2000,2000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {

                                                }

                                                @Override
                                                public void onFinish()
                                                {
                                                    Intent in = new Intent(getApplicationContext(),winners.class);
                                                    in.putExtra("id",roomId);
                                                    startActivity(in);
                                                    finish();
                                                }
                                            }.start();

                                        }
                                        status.setText(snapshot.getValue().toString());
                                        player.start();
                                    }
                                    else
                                    {

                                        LinearLayout linearLayout = findViewById(R.id.status);
                                        TextView t = new TextView(getApplicationContext());
                                        t.setText(snapshot.getKey()+" secured "+snapshot.getValue());
                                        linearLayout.addView(t);

                                    }

                                }

                            }
                        }
                        else
                        {
                            if(dataSnapshot.getValue() != null && dataSnapshot.getKey() != null)
                            {
                                if(dataSnapshot.getKey().equals("start"))
                                {
                                    Random random = new Random();
                                    int ran = random.nextInt(colors.length);
                                    status.setTextColor(colors[ran]);

                                    status.setText(dataSnapshot.getValue().toString());
                                    tambolaNumber = Integer.parseInt(dataSnapshot.getValue().toString());
                                    if(tambolaNumber == 120)
                                    {
                                        keep_running =0 ;
                                        status.setText("Someone Won the game");
                                        CountDownTimer timer1 = new CountDownTimer(2000,2000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish()
                                            {
                                                Intent in = new Intent(getApplicationContext(),winners.class);
                                                in.putExtra("id",roomId);
                                                startActivity(in);
                                                finish();
                                            }
                                        }.start();

                                    }
                                    player.start();
                                }
                                else
                                {

                                    LinearLayout linearLayout = findViewById(R.id.status);
                                    TextView t = new TextView(getApplicationContext());
                                    t.setText(dataSnapshot.getKey()+" secured "+dataSnapshot.getValue());
                                    linearLayout.addView(t);

                                }

                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }.start();
    }

    private void updateNum()
    {

        if(keep_running == 1)
       {
           if(host == 1)
           {

               TimerTask task = new TimerTask() {
                   @Override
                   public void run() {
                       DatabaseReference numbase = reference.child("start");

                       int num = new Random().nextInt(101);
                       numbase.setValue(num);
                   }
               };
               Timer timer = new Timer();
               timer.scheduleAtFixedRate(task,0,8*1000);

           }
           else
           {
               Toast.makeText(this, "Host is updating the tambola number on the server.", Toast.LENGTH_SHORT).show();
           }
       }
       else
       {
           Log.i("end","stopped");
       }
    }


    public void generateNums()
    {
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg)
            {
                if(msg.what == 1)
                {
                    addToRow(r1,msg.arg1,msg.arg2);
                }
                else if(msg.what == 2)
                {
                    addToRow(r2,msg.arg1,msg.arg2);
                }
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                for(int i = 11; i < 101 ; i += 10)
                {

                    for( int j =0 ; j < 2 ; j++ )
                    {
                        int c = (int) (Math.random() * (i - (i -11))) + (i-11);

                        int check = 1;

                        for(int z=0; z< tambolaNums.size(); z++)
                        {

                            if(c == tambolaNums.get(z))
                            {

                                check = 0;

                            }

                        }
                        if(check == 1)
                        {

                            tambolaNums.add(c);

                        }
                        else
                        {

                            j--;
                            Log.i("number repeated","in Generated numbers");

                        }

                    }
                }
Log.i("Tambola numebrs",""+tambolaNums.toString());
                for(int i = 0 ; i< tambolaNums.size(); i++)
                {

                    if(i <= tambolaNums.size()-2)
                    {

                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = tambolaNums.get(i);
                        msg.arg2 = i;
                        handler.sendMessage(msg);

                        i += 1;

                        Message msg2 = new Message();
                        msg2.what = 2;
                        msg2.arg1 = tambolaNums.get(i);
                        msg2.arg2 = i;
                        handler.sendMessage(msg2);

                    }

                }
            }
        };
        handler.post(runnable);
    }

    @SuppressLint("SetTextI18n")
    public void addToRow(TableRow row, int num, int index)
    {

        TextView t = new TextView(this);

        t.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT,1f));

        t.setTextColor(Color.WHITE);

        t.setBackgroundResource(R.drawable.ticket);

        t.setGravity(Gravity.CENTER);

        t.setText(""+num);

        t.setClickable(true);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                
                TextView ab = (TextView) v;

                if(Integer.parseInt(ab.getText().toString()) == tambolaNumber)
                {

                    updateTambolaList(tambolaNumber);

                    v.animate().alpha(0.2f).setDuration(2000);

                    v.setEnabled(false);

                    player.start();

                    checkScore();

                }
                else
                {

                    Toast.makeText(tambola.this, "Cheating mat karo", Toast.LENGTH_SHORT).show();

                }
            }
        });

        t.setFocusable(true);

        t.setId(index);

        numView.add(t);

        row.addView(t);

    }

    public void checkScore()
    {

       if(corners)
       {
           if(tambolaNums.get(0) == 120 && tambolaNums.get(16) == 120 && tambolaNums.get(1) == 120 && tambolaNums.get(17) == 120)
           {
               amazing.start();
               final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(roomId);
              
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                       {

                           if(corners)
                           {

                              if(dataSnapshot.getChildrenCount() > 1)
                              {
                                  for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                  {

                                      if(snapshot.getKey() != null && snapshot.getKey().equals(playerId) && snapshot.getValue() != null)
                                      {
                                          if(snapshot.getValue().toString().equals("start"))
                                          {
                                              DatabaseReference player = reference.child(playerId);
                                              player.setValue("Corners");
                                              
                                          }
                                          else
                                          {
                                              DatabaseReference player = reference.child(playerId);
                                              player.setValue(snapshot.getValue()+",Corners");
                                              
                                          }
                                          corners = false;
                                      }
                                  }
                              }
                              else
                              {
                                  Toast.makeText(tambola.this, "Only single player.", Toast.LENGTH_SHORT).show();
                              }

                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
       }
       if(top)
       {

           for(int i=0; i<17; i=i+2)
           {

               if(tambolaNums.get(i) != 120)
               {
                   break;
               }
               if(i == 16)
               {
                   amazing.start();
                   final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(roomId);
                  
                       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                           {

                               if(top)
                               {

                                   if(dataSnapshot.getChildrenCount() > 1)
                                   {
                                       for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                       {

                                           if(snapshot.getKey() != null && snapshot.getKey().equals(playerId) && snapshot.getValue() != null)
                                           {
                                              if(snapshot.getValue().toString().equals("start"))
                                              {
                                                  DatabaseReference player = reference.child(playerId);
                                                  player.setValue("Top");
                                              }
                                              else
                                              {
                                                  DatabaseReference player = reference.child(playerId);
                                                  player.setValue(snapshot.getValue().toString()+",Top");
                                              }
                                              top = false;
                                           }
                                       }
                                   }
                                   else
                                   {
                                       Toast.makeText(tambola.this, "Only one player in the game.", Toast.LENGTH_SHORT).show();
                                   }

                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }

               

           }

       }
       if(lower)
       {
           for(int i=1; i<18; i=i+2)
           {

               if(tambolaNums.get(i) != 120)
               {
                   break;
               }
               if(i == 17)
               {
                   amazing.start();
                   final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(roomId);
                   reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                           {

                               if(lower)
                               {

                                   if(dataSnapshot.getChildrenCount() > 1)
                                   {
                                       for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                       {

                                           if(snapshot.getKey() != null && snapshot.getKey().equals(playerId) && snapshot.getValue() != null)
                                           {
                                               if(snapshot.getValue().toString().equals("start"))
                                               {
                                                   DatabaseReference player = reference.child(playerId);
                                                   player.setValue("Lower");
                                                   lower = false;
                                               }
                                               else
                                               {
                                                   DatabaseReference player = reference.child(playerId);
                                                   player.setValue(snapshot.getValue()+",Lower");
                                                   lower = false;
                                               }
                                           }
                                       }
                                   }
                                   else
                                   {
                                       Toast.makeText(tambola.this, "Single player in the game.", Toast.LENGTH_SHORT).show();
                                   }

                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }
           }
       }
       if(house)
       {
           for(int i =0; i < 18; i++)
           {

               if(tambolaNums.get(i) != 120)
               {
                   break;
               }
               if(i == 17)
               {
                   amazing.start();
                   final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(roomId);
                   
                       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                           {

                               if(house)
                               {

                                   if(dataSnapshot.getChildrenCount() > 1)
                                   {
                                       for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                       {

                                           if(snapshot.getKey() != null && snapshot.getKey().equals("start"))
                                           {

                                               DatabaseReference databaseReference = reference.child(snapshot.getKey());

                                               databaseReference.setValue(120);

                                           }

                                           if(snapshot.getKey() != null && snapshot.getKey().equals(playerId) && snapshot.getValue() != null)
                                           {
                                              if(snapshot.getValue().toString().equals("start"))
                                              {
                                                  DatabaseReference player = reference.child(playerId);
                                                  player.setValue("House");
                                                  house = false;
                                              }
                                              else
                                              {
                                                  DatabaseReference player = reference.child(playerId);
                                                  player.setValue(snapshot.getValue()+",House");
                                                  house = false;
                                              }
                                           }
                                       }
                                   }
                                   else
                                   {
                                       Toast.makeText(tambola.this, "Single player in the game.", Toast.LENGTH_SHORT).show();
                                   }

                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }
           }
       }

    }

    public  void updateTambolaList(int number)
    {

        int index = tambolaNums.indexOf(number);

        tambolaNums.set(index,120);

    }

    public void Home(View v)
    {
        Toast.makeText(this, "You cannot leave active game.", Toast.LENGTH_SHORT).show();
    }

    public void add(View v)
    {
        Toast.makeText(this, "You cannot leave active game.", Toast.LENGTH_SHORT).show();
    }

    public void availableRooms(View v)
    {

        Toast.makeText(this, "You cannot leave active game.", Toast.LENGTH_SHORT).show();
    }

    public void settings(View v)
    {
        Toast.makeText(this, "You cannot leave active game.", Toast.LENGTH_SHORT).show();
    }

    public void statusVisible(View v)
    {

        ScrollView scroll = findViewById(R.id.scroll);

        if(scroll.getAlpha() == 0f)
        {

            scroll.animate().alpha(1f).setDuration(1500);
            v.animate().alpha(0f).setDuration(1000);

        }
        else if(scroll.getAlpha() == 1f)
        {

            scroll.animate().alpha(0f).setDuration(1500);
            v.animate().alpha(1f).setDuration(1000);

        }


    }
}
