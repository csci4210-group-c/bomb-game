package com.csci4210.bombgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

// Test code - remove later

class TestView extends View
{
    int x = 0;
    int y = 0;
    private Paint paint;

    public TestView(Context context)
    {
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        paint.setColor(Color.GREEN);
        canvas.drawRect(x, y, x + 100, y + 100, paint);
        x++;
        y++;
        this.invalidate();  // tell OS to draw again
    }
}

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TestView view = new TestView(this);
        view.setBackgroundColor(Color.BLACK);
        this.setContentView(view);
    }
}
