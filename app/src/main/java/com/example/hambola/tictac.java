package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class tictac extends AppCompatActivity
{

    LinearLayout linearLayout;

    int[][] board = new int[3][3];

    ArrayList<TextView> textViews = new ArrayList<>();

    public Handler handler;

    ProgressBar user,ai;
    Button restart;

    String[] movesGuess = new String[10];

//0 for user 1 for AI 2 for space.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictac);
    }

    @SuppressLint({"SetTextI18n", "HandlerLeak"})
    public void userTapped(View v)
    {
        TextView t = (TextView) v;
        t.setText("O");
        t.setEnabled(false);
        String[] b = t.getTag().toString().split(",");
        int f = Integer.parseInt(b[0]);
        int s = Integer.parseInt(b[1]);
        board[f][s] = 0;

        if(valueOfBoard(board,0) == 0 && EmptySpace(board))
        {
            user.setEnabled(false);
            user.setAlpha(0f);
            ai.setAlpha(1f);
            ai.setEnabled(true);

            TextView textView = findViewById(R.id.alert);
            textView.setText("Its Ai Man's turn");

            textViews();

            handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if(msg.what == 0)
                    {

                        TextView t = (TextView) msg.obj;

                        t.setText("X");

                        t.setEnabled(false);

                        user.setEnabled(true);

                        user.setAlpha(1f);

                        ai.setAlpha(0f);

                        ai.setEnabled(false);

                        TextView textView1 = findViewById(R.id.alert);

                        textView1.setText("Its your turn");

                    }
                    if(msg.what == 1)
                    {

                        TextView t = (TextView) msg.obj;

                        t.setText("X");

                        final TextView textView1 = findViewById(R.id.alert);
                        if(msg.arg1 == -10) {
                            textView1.setText("Game ended, you won!");
                        }
                        else if(msg.arg1 == 10)
                        {
                            textView1.setText("hehehehe AI jeet gaya");
                        }
                        else
                        {
                            textView1.setText("DRAW");
                        }
                       CountDownTimer timer = new CountDownTimer(3000,1000) {
                           @Override
                           public void onTick(long millisUntilFinished)
                           {

                           }

                           @Override
                           public void onFinish()
                           {
                               ai.setAlpha(0f);

                               ai.setEnabled(false);

                               restart.animate().alpha(1f).setDuration(1000);

                               restart.setEnabled(true);

                           }
                       }.start();

                    }
                }
            };

            Runnable run = new Runnable() {
                @Override
                public void run()
                {

                    findBestMove();

                }
            };


            handler.post(run);
        }
        else
        {
            user.setEnabled(false);
            user.setAlpha(0f);
            restart.animate().alpha(1f).setDuration(1000);
            restart.setEnabled(true);
            TextView tac = findViewById(R.id.alert);
            int value  = valueOfBoard(board,0);
            if(value == -10) {
                tac.setText("Game ended, you won!");
            }
            else if(value == 10)
            {
                tac.setText("hehehehe AI jeet gaya");
            }
            else
            {
                tac.setText("DRAW");
            }
        }

    }

    public void restart(final View v)
    {

        getTicTakInitial();

        for(TextView t : textViews)
        {

            t.setText("");
            t.setEnabled(true);

        }

        v.animate().setDuration(1000).translationXBy(700).translationYBy(-800).rotationBy(3000);

        CountDownTimer timer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish()
            {
                user.setAlpha(1f);
                user.setEnabled(true);
                v.setAlpha(0f);
                v.setEnabled(false);
                v.animate().setDuration(1000).translationXBy(-700).translationYBy(800).rotationBy(-3000);
                linearLayout.setEnabled(true);
                linearLayout.animate().setDuration(500).alpha(1f);
                TextView t = findViewById(R.id.alert);
                t.setText("Its your turn!");
            }
        }.start();

    }

    public void findBestMove()
    {

        Log.i("into","findBestMove");

        int index = 0;

        int bestIndex = 1000, bestScore = -1000;

        int[][] newBoard = new int[3][3];
        for (int i = 0; i < 3; i++)
        {

            for (int j = 0; j < 3; j++)
            {

                newBoard[i][j] = board[i][j];

            }

        }


        if(valueOfBoard(board,0) == 0)
        {

            Log.i("into","starting minimax start");
            for(int a =0; a<3;a++)
            {

                for(int b=0; b<3;b++)
                {

                    if(newBoard[a][b] == 2)
                    {

                        movesGuess[index] = a+","+b;


                        newBoard[a][b] = 1;

                        int thisPlaceScore = miniMax(newBoard, 0, false);

                        Log.i("score",""+thisPlaceScore);

                        if (thisPlaceScore > bestScore)
                        {

                            bestScore = thisPlaceScore;

                            bestIndex = index;

                        }
                        index++;

                        newBoard[a][b] = 2;

                    }

                }

            }
        }
        else
        {
            TextView textView = findViewById(R.id.alert);
            textView.setText("Game Ended");
        }

        Message m = new Message();

        String yes = movesGuess[bestIndex];

        String[] moves = yes.split(",");

        int f = Integer.parseInt(moves[0]);

        int s = Integer.parseInt(moves[1]);

        board[f][s] = 1;

        int value = valueOfBoard(board,0);

        if(value == 0)
        {
            m.what = 0;
        }
        else
        {
            m.what = 1;
            m.arg1 = value;
        }

        TextView textView = null;

        for (int i = 0; i < textViews.size(); i++)
        {

            if(textViews.get(i).getTag().equals(yes))
            {

                textView = textViews.get(i);

            }
        }

        if(textView != null)
        {

            m.obj = textView;

            handler.sendMessage(m);

        }
        else
        {

            Log.i("null","textView");

        }

    }

    public int miniMax(int[][] mboard, int depth, boolean isAi)
    {
        int value = valueOfBoard(mboard,depth);

        if(value == 0)
        {

            if (!EmptySpace(mboard)) {

                return valueOfBoard(mboard, depth);

            } else {
                if (isAi) {

                    int score = -1000;

                    for (int i = 0; i < 3; i++)
                    {

                        for (int j = 0; j < 3; j++)
                        {

                            if (mboard[i][j] == 2)
                            {
                                mboard[i][j] = 1;

                                int nextScore = miniMax(mboard, depth + 1, false);

                                Log.i("nextScore",""+nextScore);

                                score = Math.max(nextScore, score);

                                mboard[i][j] = 2;

                            }

                        }

                    }
                    return score;

                } else {

                    int score = 1000;

                    for (int i = 0; i < 3; i++) {

                        for (int j = 0; j < 3; j++) {

                            if (mboard[i][j] == 2) {

                                mboard[i][j] = 0;

                                int nextScore = miniMax(mboard, depth + 1, true);

                                Log.i("nextScore",""+nextScore);

                                score = Math.min(nextScore, score);

                                mboard[i][j] = 2;

                            }

                        }

                    }
                    return score;
                }
            }
        }
        else
        {
            return value;
        }
    }

    public int valueOfBoard(int[][] board, int depth)
    {
        int value = 0;


        for(int i =0 ; i<3;i++)
        {

            if(board[0][i]==board[1][i] && board[1][i] == board[2][i])
            {

                if(board[0][i] == 1)
                {
                    value = 10 - depth;
                }
                else if(board[0][i] == 0)
                {
                    value = -10 + depth;
                }

            }

        }
        for(int i =0 ; i<3;i++)
        {

            if(board[i][0]==board[i][1] && board[i][1] == board[i][2])
            {

                if(board[i][0] == 1)
                {
                    value = 10 - depth;
                }
                else if(board[i][0] == 0)
                {
                    value = -10 + depth;
                }

            }

        }
        if(board[0][0] == board[1][1] && board[1][1] == board[2][2])
        {

            if(board[0][0] == 1)
            {
                value = 10 - depth;
            }
            else if(board[0][0] == 0)
            {
                value = -10 + depth;
            }

        }
        else if(board[2][0] == board[1][1] && board[1][1] == board[0][2])
        {

            if(board[1][1] == 1)
            {
                value = 10 - depth;
            }
            else if(board[1][1] == 0)
            {
                value = -10 + depth;
            }

        }
        return value;
    }

    public boolean EmptySpace(int[][] myBoard)
    {
        for(int i =0; i <3; i++)
        {
            for(int j =0; j< 3; j++)
            {

                if(myBoard[i][j] == 2)
                {
                    return true;
                }

            }
        }
        return false;
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("imageDir",MODE_PRIVATE);
        File out = new File(file,"dpBitmap"+".jpg");
        try {
            FileInputStream inputStream = new FileInputStream(out.getAbsoluteFile());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ImageView imageView = findViewById(R.id.imageView9);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        restart = findViewById(R.id.restart);
        restart.setAlpha(0f);
        restart.setEnabled(false);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setEnabled(false);
        linearLayout.setAlpha(0f);
        user = findViewById(R.id.progressBar4);
        ai = findViewById(R.id.progressBar3);
        user.setAlpha(0f);
        ai.setAlpha(0f);
        getTicTakInitial();
    }

    public void Start(final View v)
    {
        v.animate().setDuration(2000).translationXBy(700).translationYBy(800).rotationBy(4000);
        CountDownTimer timer = new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish()
            {
                user.setAlpha(1f);
                user.setEnabled(true);
                v.setVisibility(View.GONE);
                v.setEnabled(false);
                linearLayout.setEnabled(true);
                linearLayout.animate().setDuration(2000).alpha(1f);
                TextView t = findViewById(R.id.alert);
                t.setText("Its your turn!");
            }
        }.start();

    }
    public void getTicTakInitial()
    {

        for(int i = 0; i < 3;i++)
        {

            for(int j =0; j<3;j++)
            {

                board[i][j] = 2;

            }

        }

    }

    public void textViews()
    {
        TextView t = findViewById(R.id.tictac11);
        textViews.add(t);

        TextView t1 = findViewById(R.id.tictac12);
        textViews.add(t1);

        TextView t2 = findViewById(R.id.tictac13);
        textViews.add(t2);

        TextView t3 = findViewById(R.id.tictac21);
        textViews.add(t3);

        TextView t4 = findViewById(R.id.tictac22);
        textViews.add(t4);

        TextView t5 = findViewById(R.id.tictac23);
        textViews.add(t5);

        TextView t6 = findViewById(R.id.tictac31);
        textViews.add(t6);

        TextView t7 = findViewById(R.id.tictac32);
        textViews.add(t7);

        TextView t8 = findViewById(R.id.tictac33);
        textViews.add(t8);
    }

    public void Home(View v)
    {
        Intent intent = new Intent(getApplicationContext(),drawer.class);
        startActivity(intent);
    }
    public void add(View v)
    {

        Toast.makeText(this, "hehehehehe", Toast.LENGTH_SHORT).show();

    }
    public void availableRooms(View v)
    {

        Toast.makeText(this, "This feature will be made available soon.", Toast.LENGTH_SHORT).show();

    }
    public void settings(View v)
    {

        Toast.makeText(this, "hehehehehe", Toast.LENGTH_SHORT).show();
    }
}
