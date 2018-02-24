/**
 * Created by cameron on 2/13/18.
 */

package com.csci4210.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameEngine
{
    /*
    public static final int CELL_WIDTH = 32;
    public static final int CELL_HEIGHT = 32;
    */
    private static final int MAX_SPRITES = 16;
    private static final int MAX_BUTTONS = 12;
    private static final int TICKS_PER_SECOND = 60;

    public static int screenWidth;
    public static int screenHeight;

    private static Sprite sprites[] = new Sprite[MAX_SPRITES];
    private static Button buttons[] = new Button[MAX_BUTTONS];
    private static Bitmap backdrop;
    private static GameView gameView;
    private static State currState;

    static class GameView extends View
    {
        private Paint paint;
        private long lastTime;

        public GameView(Context context)
        {
            super(context);
            paint = new Paint();
            paint.setColor(Color.rgb(255, 255, 255));
            this.setBackgroundColor(Color.BLACK);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            if (backdrop != null)
            {
                Rect srcRect = new Rect(0, 0, backdrop.getWidth(), backdrop.getHeight());
                Rect destRect = new Rect(0, 0, GameEngine.screenWidth, GameEngine.screenHeight);
                canvas.drawBitmap(backdrop, srcRect, destRect, paint);
            }

            for (Sprite sprite : GameEngine.sprites)
            {
                if (sprite.active)
                {
                    canvas.drawBitmap(
                            sprite.spriteSheet,
                            sprite.getFrameSrcRect(),
                            sprite.getDestRect(),
                            paint);
                }
            }

            for (Button button : GameEngine.buttons)
            {
                if (button != null)
                    canvas.drawRect(button.getRect(), paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                currState.onTouch((int)event.getX(), (int)event.getY());
            return true;
        }

        public void onBackPressed()
        {
            currState.onBackPressed();
        }
    }

    public static void init(Activity activity)
    {
        // initialize sprites
        for (int i = 0; i < sprites.length; i++)
            sprites[i] = new Sprite();

        // initialize Android view
        gameView = new GameView(activity);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)gameView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        activity.setContentView(gameView);
    }

    public static void setState(State state)
    {
        if (currState != null)
            currState.exit();
        currState = state;
        currState.enter();
    }

    static class MainThread extends Thread
    {
        @Override
        public void run()
        {
            long before = SystemClock.elapsedRealtime();
            long now;
            long dt;

            while (true)
            {
                now = SystemClock.elapsedRealtime();
                dt = now - before;
                if (1000 / TICKS_PER_SECOND > dt)
                    SystemClock.sleep(1000 / TICKS_PER_SECOND - dt);
                before = SystemClock.elapsedRealtime();

                currState.update();
                for (Sprite sprite : GameEngine.sprites)
                {
                    if (sprite.active)
                        sprite.updateAnim();
                }
                GameEngine.gameView.postInvalidate();
            }
        }
    }

    public static void mainLoop()
    {
        MainThread thread = new MainThread();
        thread.start();
    }

    public static void setBackdrop(Bitmap bitmap)
    {
        backdrop = bitmap;
    }

    public static Sprite createSprite(Bitmap spriteSheet, int animSeq[][], int x, int y, int width, int height)
    {
        for (Sprite sprite : sprites)
        {
            if (!sprite.active)
            {
                sprite.setParams(spriteSheet, animSeq, x, y, width, height);
                sprite.active = true;
                return sprite;
            }
        }
        return null;
    }

    public static void createButton(int x, int y, int width, int height, String label, int id)
    {
        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i] == null)
            {
                buttons[i] = new Button(x, y, width, height, label, id);
                return;
            }
        }
    }

    public static void destroyAllButtons()
    {
        for (int i = 0; i < buttons.length; i++)
            buttons[i] = null;
    }

    public static void destroySprite(Sprite toDestroy)
    {
        toDestroy.active = false;
    }

    public static int buttonHitTest(int x, int y)
    {
        for (Button button : GameEngine.buttons)
        {
            if (button != null)
            {
                Rect rect = button.getRect();
                if (rect.contains(x, y))
                    return button.id;
            }
        }
        return -1;
    }
}