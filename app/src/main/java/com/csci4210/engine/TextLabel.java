package com.csci4210.engine;

public class TextLabel
{
    String text;
    int x, y;

    TextLabel(int x, int y, String text)
    {
        this.x = x;
        this.y = y;
        this.text = text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
}