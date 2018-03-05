package com.csci4210.engine;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;

class Button
{
    int x;
    int y;
    int width;
    int height;
    String label;
    int id;
    LinearGradient normalGradient;
    LinearGradient pushedGradient;

    private static final int normalColors[] = {
            Color.rgb(100, 100,100),
            Color.rgb(32, 32, 32),
    };
    private static final int pushedColors[] = {
            Color.rgb(32, 32, 32),
            Color.rgb(64, 64, 64),
    };

    Button(int x, int y, int width, int height, String label, int id)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.id = id;

        int top = y - height / 2;

        normalGradient = new LinearGradient(0, top + height / 8, 0, top + height, normalColors[0], normalColors[1], Shader.TileMode.MIRROR);
        pushedGradient = new LinearGradient(0, top + height / 8, 0, top + height, pushedColors[0], pushedColors[1], Shader.TileMode.MIRROR);
    }

    public Rect getRect()
    {
        return new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        //return new Rect(100, 100, 500, 500);
    }
}
