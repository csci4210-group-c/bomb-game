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
import android.os.SystemClock;
import android.view.View;

public class GameEngine
{
    /*
    public static final int CELL_WIDTH = 32;
    public static final int CELL_HEIGHT = 32;
    */
    private static final int MAX_SPRITES = 16;
    private static final int TICKS_PER_SECOND = 60;

    private static Sprite sprites[] = new Sprite[MAX_SPRITES];
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
            this.setBackgroundColor(Color.DKGRAY);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
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
        }
    }

    public static void init(Activity activity)
    {
        // initialize sprites
        for (int i = 0; i < sprites.length; i++)
            sprites[i] = new Sprite();

        // initialize Android view
        gameView = new GameView(activity);
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

    public static void destroySprite(Sprite toDestroy)
    {
        toDestroy.active = false;
    }
}
