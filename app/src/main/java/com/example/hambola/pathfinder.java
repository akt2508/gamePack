package com.example.hambola;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
// 0 pe kuch nhi hai , -1 pe black hai,  1 pe user path hai.
public class pathfinder extends AppCompatActivity
{

    int status = 0;
    int aiWork = 0;
    int rows;
    int direction = 1;
    int columns;

    ArrayList<Cell> cells = new ArrayList<>();

    ArrayList<Cell> userPath = new ArrayList<>();

    ArrayList<Cell> aiPath = new ArrayList<>();

    public void aStar()
    {

        @SuppressLint("HandlerLeak") final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg)
            {

                if(msg.what == 0)
                {

                    ProgressBar progressBar = findViewById(R.id.ai_status);

                    if(progressBar.isEnabled())
                    {

                        progressBar.setEnabled(false);
                        progressBar.setAlpha(0f);

                    }

                    LinearLayout grid = findViewById(R.id.grid);

                    LinearLayout row = (LinearLayout) grid.getChildAt(msg.arg1);

                    TextView cell = (TextView) row.getChildAt(msg.arg2);

                    cell.setBackgroundResource(R.drawable.ai_cell);

                }
                else if(msg.what == 1)
                {

                    ProgressBar progressBar = findViewById(R.id.ai_status);

                    if(progressBar.isEnabled())
                    {

                        progressBar.setEnabled(false);
                        progressBar.setAlpha(0f);

                    }

                    Toast.makeText(pathfinder.this, "No path available", Toast.LENGTH_LONG).show();

                }
                else if(msg.what == 10)
                {

                    LinearLayout grid = findViewById(R.id.grid);

                    LinearLayout row = (LinearLayout) grid.getChildAt(msg.arg1);

                    TextView cell = (TextView) row.getChildAt(msg.arg2);

                    cell.setBackgroundResource(R.drawable.yellow_path);

                }
            }
        };

        final Runnable runnable =  new Runnable() {
            @Override
            public void run()
            {

                ArrayList<Cell> open = new ArrayList<>();

                Cell start = cells.get(0);

                Cell end = new Cell(rows-1,columns-1);

                double valueToEnd = calDistance(start,end);

                start.setValue(valueToEnd);

                open.add(start);

                while(open.size() > 0)
                {

                    Cell current = new Cell();
                    current.setValue(Double.MAX_VALUE);
                    //check for the lowest valued member
                    for(Cell cell : open)
                    {
                        if(cell.getValue() < current.getValue())
                        {

                            current = cell;
                            open.remove(cell);
                            break;

                        }
                    }

                    Log.i("current",current.getX()+"  "+current.getY()+"   value: "+current.getValue() );

                    if(current.getX() == end.getX() && current.getY() == end.getY())
                    {

                        Cell cell = current.getPreviousCell();

                        while(cell != null)
                        {

                            if(cell.getX() == start.getX() && cell.getY() == start.getY())
                            {
                                Log.i("this cell","is starting point");
                            }
                            else
                            {
                                handler.sendMessageDelayed(generateMessage(cell),1000);
                                aiPath.add(cell);
                            }
                            cell = cell.getPreviousCell();
                        }
                        return;
                    }

                    Cell bestNeighbour = new Cell();
                    bestNeighbour.setValue(Double.MAX_VALUE);

                    for(Cell cell : neighbours(current))
                    {

                        if(cell.getState() >= 0)
                        {

                            double dis = calDistance(cell,end);

                            if(dis < calDistance(current,end))
                            {

                                bestNeighbour = cell;

                                bestNeighbour.setValue(dis + current.getValue());
                                bestNeighbour.setX(cell.getX());
                                bestNeighbour.setY(cell.getY());

                                //Message msg = generateMessage(bestNeighbour);
                                //msg.what = 10;
                                //handler.sendMessageDelayed(msg,300);

                                int check = 1;
                                for(Cell box : open)
                                {
                                    if (bestNeighbour.getX() == box.getX() && bestNeighbour.getY() == box.getY())
                                    {

                                        check = 0;

                                        break;

                                    }
                                }
                                if(check == 1)
                                {
                                    open.add(bestNeighbour);
                                    Log.i("Adding","neighbour");
                                }

                            }

                        }
                        else
                        {
                            Log.i("neighbours","obstacle here");
                        }

                    }

                }
                Message ms = new Message();
                ms.what = 1;
                handler.sendMessage(ms);

            }
        };


        handler.postDelayed(runnable,0);

    }

    public Message generateMessage(Cell cell)
    {

        Message output = new Message();
        output.what = 0;
        output.arg1 = cell.getX();
        output.arg2 = cell.getY();

        return output;

    }

    public ArrayList<Cell> neighbours(Cell cell)
    {
        ArrayList<Cell> output = new ArrayList<>();

        int rowMinus = cell.getX()-1;
        int colMinus = cell.getY() -1;
        int rowAdd = cell.getX() + 1;
        int colAdd = cell.getY() + 1;

        if(rowMinus >= 0)
        {
            Cell into = new Cell(rowMinus,cell.getY());

            for(Cell cell1 : cells)
            {

                if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                {

                    into.setState(cell1.getState());

                }

            }

            into.setPreviousCell(cell);

            output.add(into);

        }

        if(colMinus>=0)
        {

            Cell into = new Cell(cell.getX(),colMinus);
            for(Cell cell1 : cells)
            {

                if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                {

                    into.setState(cell1.getState());

                }

            }
            into.setPreviousCell(cell);
            output.add(into);

        }

        if(rowAdd <= rows-1)
        {

            Cell into = new Cell(rowAdd,cell.getY());
            for(Cell cell1 : cells)
            {

                if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                {

                    into.setState(cell1.getState());

                }

            }
            into.setPreviousCell(cell);
            output.add(into);

        }

        if(colAdd <= columns-1)
        {

            Cell into = new Cell(cell.getX(),colAdd);
            for(Cell cell1 : cells)
            {

                if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                {

                    into.setState(cell1.getState());

                }

            }
            into.setPreviousCell(cell);

            output.add(into);

        }

        if(direction == 1)
        {
            if(rowMinus >= 0 && colMinus >= 0)
            {

                Cell into = new Cell(rowMinus,colMinus);
                for(Cell cell1 : cells)
                {

                    if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                    {

                        into.setState(cell1.getState());

                    }

                }
                into.setPreviousCell(cell);

                output.add(into);

            }

            if(rowAdd < rows && colAdd < columns)
            {
                Cell into = new Cell(rowAdd,colAdd);
                for(Cell cell1 : cells)
                {

                    if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                    {

                        into.setState(cell1.getState());

                    }

                }
                into.setPreviousCell(cell);

                output.add(into);
            }

            if(colAdd < columns && rowMinus >= 0)
            {
                Cell into = new Cell(rowMinus,colAdd);
                for(Cell cell1 : cells)
                {

                    if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                    {

                        into.setState(cell1.getState());

                    }

                }
                into.setPreviousCell(cell);

                output.add(into);
            }

            if(colMinus >= 0 && rowAdd < rows)
            {
                Cell into = new Cell(rowAdd,colMinus);
                for(Cell cell1 : cells)
                {

                    if ( cell1.getX() == into.getX() && cell1.getY() == into.getY())
                    {

                        into.setState(cell1.getState());

                    }

                }
                into.setPreviousCell(cell);

                output.add(into);
            }

        }

        return output;

    }

    public double calDistance(Cell c1, Cell c2)
    {

        return Math.sqrt(Math.pow(c2.getX() - c1.getX(),2) + Math.pow(c2.getY() - c1.getY(),2));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathfinder);

        Intent intent = getIntent();
        rows = intent.getIntExtra("rows",10);
        columns = intent.getIntExtra("columns",10);
        direction = intent.getIntExtra("direction",1);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart()
    {
        super.onStart();
        final LinearLayout rowLL = findViewById(R.id.grid);

        rowLL.setAlpha(0.2f);
        rowLL.setEnabled(false);

        rowLL.setClickable(false);
        rowLL.setFocusable(false);


        for(int i =0; i< rows ; i++)
        {
            LinearLayout aa = new LinearLayout(this);

            aa.setClickable(false);
            aa.setFocusable(false);

            aa.setOrientation(LinearLayout.HORIZONTAL);
            aa.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1));

            for(int j=0; j<columns;j++)
            {

                TextView t = new TextView(this);
                t.setClickable(false);
                t.setFocusable(false);
                t.setTag(i+","+j);

                Cell cell = new Cell(0,null,i,j,0);

                cells.add(cell);

                t.setAlpha(0.5f);
                t.setBackgroundResource(R.drawable.cell_path);
                t.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1));

                if(i==j)
                {
                    if(i == 0)
                    {
                        t.setBackgroundResource(R.drawable.red_start);
                        t.setEnabled(false);
                    }
                }
                if(i== rows-1 && j == columns-1)
                {

                    t.setBackgroundResource(R.drawable.green_end);
                    t.setEnabled(false);

                }

                aa.addView(t);

            }
            rowLL.addView(aa);

        }

        rowLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int touch = event.getActionMasked();

                if(touch == MotionEvent.ACTION_MOVE)
                {

                    LinearLayout root = (LinearLayout)v;

                    for(int harshal=0; harshal<root.getChildCount();harshal++)
                    {

                        LinearLayout child_root = (LinearLayout) root.getChildAt(harshal);

                        for(int dam =0;dam < child_root.getChildCount();dam++)
                        {

                            TextView view = (TextView) child_root.getChildAt(dam);

                            Rect rect = new Rect();
                            view.getGlobalVisibleRect(rect);
                            int[] location = new int[2];
                            rowLL.getLocationOnScreen(location);

                            rect.set(rect.left-location[0],
                                    rect.top -location[1],
                                    rect.right-location[0],
                                    rect.bottom-location[1]);

                            if(rect.contains((int)event.getX(),(int)event.getY()))
                            {
                               for(Cell cell : cells)
                                {

                                    if(cell.getX() == harshal && cell.getY() == dam)
                                    {
                                        cell.setState(-1);
                                    }

                                }
                                view.setAlpha(0.8f);
                                view.setEnabled(false);

                                return true;

                            }

                        }

                    }

                }

                return true;
            }
        });

    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void user(View v)
    {

        if(status == 0)
        {

            LinearLayout linearLayout = findViewById(R.id.grid);
            linearLayout.animate().alpha(1f);
            linearLayout.setEnabled(true);

            Button t = (Button)v;
            t.setText("Start drawing shortest path");
            ((Button) v).setTextColor(Color.YELLOW);
            status++;

        }
        else if(status ==1)
        {
            for(Cell cell : cells)
            {

                Log.i(cell.getX()+"  "+cell.getY(),""+cell.getState());

            }

            Button t = (Button)v;
            t.setText("Compare your path with AI");
            t.setTextColor(Color.GREEN);
            status++;

            final LinearLayout rowLL = findViewById(R.id.grid);
            rowLL.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int touch = event.getActionMasked();

                    if(touch == MotionEvent.ACTION_MOVE)
                    {


                        LinearLayout root = (LinearLayout)v;

                        for(int harshal=0; harshal<root.getChildCount();harshal++)
                        {

                            LinearLayout child_root = (LinearLayout) root.getChildAt(harshal);

                            for(int dam =0;dam < child_root.getChildCount();dam++)
                            {

                                TextView view = (TextView) child_root.getChildAt(dam);

                                Rect rect = new Rect();
                                view.getGlobalVisibleRect(rect);
                                int[] location = new int[2];
                                rowLL.getLocationOnScreen(location);

                                rect.set(rect.left-location[0],
                                        rect.top -location[1],
                                        rect.right-location[0],
                                        rect.bottom-location[1]);

                                if(rect.contains((int)event.getX(),(int)event.getY()))
                                {

                                   if(view.isEnabled())
                                    {
                                        view.setBackgroundResource(R.drawable.yellow_path);
                                        view.setEnabled(false);
                                        for(Cell cell : cells)
                                        {

                                            if(cell.getX() == harshal && cell.getY() == dam)
                                            {
                                                cell.setState(1);
                                                userPath.add(cell);
                                            }

                                        }
                                    }

                                    return true;

                                }

                            }

                        }

                    }

                    return true;
                }
            });

        }
        else if(status == 2)
        {

            if(aiWork == 0)
            {
                Toast.makeText(this, "First make the ai work on finding the shortest path", Toast.LENGTH_LONG).show();
            }
            else
            {
                Button ai = findViewById(R.id.button10);
                ai.setEnabled(false);
                v.setEnabled(false);

                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg)
                    {

                        if(msg.what == 13)
                        {
                            Log.i("abc","bcd");
                            ConstraintLayout body = findViewById(R.id.body);

                            Snackbar snackbar = Snackbar.make(body,"kya bawasir bana diye ho",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Start over", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            reset();
                                        }
                                    });

                            snackbar.show();
                        }
                        else if(msg.what == 12)
                        {
                            float percent = (float)msg.obj;

                            ConstraintLayout body = findViewById(R.id.body);

                            Snackbar snackbar = Snackbar.make(body,"Your path is "+percent+"% close to the best path.",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Start over", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            reset();
                                        }
                                    });

                            snackbar.show();
                        }

                    }
                };


                Runnable runnable = new Runnable() {
                    @Override
                    public void run()
                    {

                        int total = aiPath.size();

                        float captured = 0;

                        for(Cell cel : userPath)
                        {

                            for(Cell cell : aiPath)
                            {

                                if(cel.getX() == cell.getX() && cel.getY() == cell.getY())
                                {

                                    captured++;

                                }

                            }

                        }

                        if(captured == 0)
                        {
                            Message msg = new Message();
                            msg.what = 13;
                            handler.sendMessage(msg);
                        }
                        else
                        {
                            Message msg = new Message();
                            msg.what = 12;
                            msg.obj = (captured/total)*100;
                            handler.sendMessage(msg);
                        }

                    }
                };
                handler.post(runnable);

            }

        }

    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void reset()
    {

        aiWork = 0;
        status = 0;
        userPath.clear();
        cells.clear();
        aiPath.clear();

        Button btn = findViewById(R.id.button11);
        btn.setEnabled(true);
        btn.setTextColor(Color.RED);
        btn.setText("Set obstacles");

        Button bn = findViewById(R.id.button10);
        bn.setEnabled(true);

        final LinearLayout grid = findViewById(R.id.grid);

        grid.setAlpha(0.2f);

        grid.setEnabled(false);

        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int touch = event.getActionMasked();

                if(touch == MotionEvent.ACTION_MOVE)
                {

                    LinearLayout root = (LinearLayout)v;

                    for(int harshal=0; harshal<root.getChildCount();harshal++)
                    {

                        LinearLayout child_root = (LinearLayout) root.getChildAt(harshal);

                        for(int dam =0;dam < child_root.getChildCount();dam++)
                        {

                            TextView view = (TextView) child_root.getChildAt(dam);

                            Rect rect = new Rect();
                            view.getGlobalVisibleRect(rect);
                            int[] location = new int[2];
                            grid.getLocationOnScreen(location);

                            rect.set(rect.left-location[0],
                                    rect.top -location[1],
                                    rect.right-location[0],
                                    rect.bottom-location[1]);

                            if(rect.contains((int)event.getX(),(int)event.getY()))
                            {
                                for(Cell cell : cells)
                                {

                                    if(cell.getX() == harshal && cell.getY() == dam)
                                    {
                                        cell.setState(-1);
                                    }

                                }
                                view.setAlpha(0.8f);
                                view.setEnabled(false);

                                return true;

                            }

                        }

                    }

                }

                return true;
            }
        });

        for (int i =0 ; i < grid.getChildCount(); i++)
        {

            LinearLayout row = (LinearLayout) grid.getChildAt(i);

            for(int j=0; j < row.getChildCount(); j++)
            {

                TextView cell = (TextView) row.getChildAt(j);

                Cell cell1 = new Cell(0,null,i,j,0);

                cells.add(cell1);

                cell.setBackgroundResource(R.drawable.cell_path);

                cell.setAlpha(0.5f);

                if(i==j)
                {
                    if(i==0)
                    {
                        cell.setBackgroundResource(R.drawable.red_start);
                        cell.setEnabled(false);
                    }
                }
                if(i == rows-1 && j == columns-1)
                {
                    cell.setBackgroundResource(R.drawable.green_end);
                    cell.setEnabled(false);
                }

            }

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void aiClicked(View v)
    {

        if(aiWork == 0 && userPath.size() > 0)
        {

            ProgressBar aiStatus = findViewById(R.id.ai_status);

            aiStatus.setAlpha(1f);
            aiStatus.setEnabled(true);

            LinearLayout linearLayout = findViewById(R.id.grid);
            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            aiWork++;
            aStar();

        }
        else {

            Snackbar.make(findViewById(R.id.body),"Please draw your path", Snackbar.LENGTH_SHORT).show();

        }

    }
}

