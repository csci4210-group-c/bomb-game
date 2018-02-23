package com.csci4210.engine;

import android.graphics.Rect;

class Button
{
    int x;
    int y;
    int width;
    int height;
    String label;
    int id;

    public Button(int x, int y, int width, int height, String label, int id)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.id = id;
    }

    public Rect getRect()
    {
        return new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        //return new Rect(100, 100, 500, 500);
    }
}
