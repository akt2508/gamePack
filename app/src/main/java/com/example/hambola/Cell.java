package com.example.hambola;

public class Cell
{

    private int state;

    private double value;

    private Cell previousCell;

    private int x,y;

    Cell()
    {

    }

    Cell(double v, Cell c, int xVal, int yVal,int stat)
    {

    this.value = v;
    this.previousCell = c;
    this.x = xVal;
    this.y = yVal;
    this.state = stat;

    }

   void setState(int statea)
    {
        this.state = statea;
    }
    int getState()
    {

        return state;

    }

    void setPreviousCell(Cell cell)
    {

        this.previousCell = cell;

    }
    Cell(int xVal, int yVal)
    {

        this.x = xVal;
        this.y = yVal;

    }

    double getValue()
    {
        return value;
    }

    Cell getPreviousCell()
    {
        return previousCell;
    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

     void setValue(double val)
    {
        this.value = val;
    }

     void setX(int xx)
    {
        this.x = xx;
    }

     void setY(int yy)
    {
        this.y = yy;
    }

}
