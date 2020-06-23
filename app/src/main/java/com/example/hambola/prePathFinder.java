package com.example.hambola;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class prePathFinder extends AppCompatActivity {

    int direction = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_path_finder);
    }

    public void cont(View v)
    {

        EditText editText = findViewById(R.id.editText2);
        EditText editText1 = findViewById(R.id.editText3);

       if(!editText.getText().toString().trim().equals("") && !editText1.getText().toString().trim().equals("") && direction != -1)
       {
           int rows = Integer.parseInt(editText.getText().toString().trim());
           int columns = Integer.parseInt(editText1.getText().toString().trim());

           if(rows > 60 || columns>60)
           {
               Toast.makeText(this, "High value of rows and columns might make the app slow, please enter values less than 60", Toast.LENGTH_LONG).show();
           }
           else
           {

               Intent intent = new Intent(getApplicationContext(),pathfinder.class);

               intent.putExtra("rows",rows);
               intent.putExtra("columns",columns);
               intent.putExtra("direction",direction);

               startActivity(intent);
               finish();

           }
       }
    }

    @SuppressLint("SetTextI18n")
    public void four(View v)
    {

        direction = 0;
        TextView status  =  findViewById(R.id.status);
        status.setText("Each cell can move to four neighbours around it.");
        status.setSelected(true);

    }
    @SuppressLint("SetTextI18n")
    public void eight(View v)
    {

        direction = 1;
        TextView status  =  findViewById(R.id.status);
        status.setText("Each cell can move to eight neighbours around it.");
        status.setSelected(true);

    }

    public void Open(View v)
    {

        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/A*_search_algorithm"));

        startActivity(browser);

    }
}
