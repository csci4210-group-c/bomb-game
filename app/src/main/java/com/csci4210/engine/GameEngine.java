/**
 * Created by cameron on 2/13/18.
 */

package com.csci4210.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameEngine
{
    private static int displayScale = 1;
    public static int xoffset = 0;
    public static int yoffset = 0;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    private static final int MAX_SPRITES = 16;
    private static final int MAX_BUTTONS = 12;
    private static final int TICKS_PER_SECOND = 60;

    public static int screenWidth;
    public static int screenHeight;

    private static Bitmap tileSet;
    private static byte tileMap[][];
    private static Sprite sprites[] = new Sprite[MAX_SPRITES];
    private static Button buttons[] = new Button[MAX_BUTTONS];
    private static Bitmap backdrop;
    private static GameView gameView;
    private static State currState;
    private static Button pushedButton;

    static class GameView extends View
    {
        private Paint paint;
        private long lastTime;

        public GameView(Context context)
        {
            super(context);
            paint = new Paint();
            paint.setColor(Color.rgb(255, 255, 255));
            paint.setTextSize(20);
            paint.setTextAlign(Paint.Align.CENTER);
            this.setBackgroundColor(Color.BLACK);
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            Rect srcRect = new Rect();
            Rect destRect = new Rect();

            // draw backdrop
            if (backdrop != null)
            {
                srcRect.set(0, 0, backdrop.getWidth(), backdrop.getHeight());
                destRect.set(0, 0, GameEngine.screenWidth, GameEngine.screenHeight);
                canvas.drawBitmap(backdrop, srcRect, destRect, paint);
            }

            // draw tilemap
            if (tileMap != null && tileSet != null)
            {
                int tileWidth = TILE_WIDTH* displayScale;
                int tileHeight = TILE_HEIGHT* displayScale;
                int tileX, tileY;
                int destX, destY;
                int srcWidth = tileSet.getWidth();

                for (tileY = 0; tileY < tileMap.length; tileY++)
                {
                    destY = yoffset * displayScale + tileY * tileHeight;
                    for (tileX = 0; tileX < tileMap[tileY].length; tileX++)
                    {
                        byte tileId = tileMap[tileY][tileX];

                        destX = xoffset * displayScale + tileX * tileWidth;
                        destRect.set(destX, destY, destX + tileWidth, destY + tileHeight);
                        srcRect.set(0, tileId * srcWidth, srcWidth, tileId * srcWidth + srcWidth);
                        canvas.drawBitmap(tileSet, srcRect, destRect, paint);
                    }
                }
            }

            // draw sprites
            for (Sprite sprite : GameEngine.sprites)
            {
                if (sprite.active)
                {
                    destRect = sprite.getDestRect();
                    destRect.left = (destRect.left + xoffset) * displayScale;
                    destRect.right = (destRect.right + xoffset) * displayScale;
                    destRect.top = (destRect.top + yoffset) * displayScale;
                    destRect.bottom = (destRect.bottom + yoffset) * displayScale;

                    canvas.drawBitmap(
                            sprite.spriteSheet,
                            sprite.getFrameSrcRect(),
                            destRect,
                            paint);
                }
            }

            // draw buttons
            for (Button button : GameEngine.buttons)
            {
                if (button != null)
                {
                    boolean pushed = (button == pushedButton);

                    paint.setShader(pushed ? button.pushedGradient : button.normalGradient);
                    canvas.drawRect(button.getRect(), paint);
                    paint.setShader(null);
                    paint.setColor(Color.WHITE);
                    canvas.drawText(button.label, button.x, button.y + (pushed ? 2 * displayScale : 0), paint);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            int x = (int)event.getX();
            int y = (int)event.getY();

            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN)
            {
                for (Button button : GameEngine.buttons)
                {
                    if (button != null && button.getRect().contains(x, y))
                    {
                        GameEngine.pushedButton = button;
                        return true;
                    }
                }
                currState.onTouchDown(x, y);
            }
            else if (action == MotionEvent.ACTION_UP)
            {
                if (pushedButton != null && pushedButton.getRect().contains(x, y))
                    currState.onButton(pushedButton.id);
                else
                    currState.onTouchUp(x, y);
                pushedButton = null;
            }

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
        //getting view's default dimensions on runtime
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)gameView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        int screenDimMax = Math.max(screenWidth, screenHeight);
        if (screenDimMax > 1000)
            displayScale = 2;

        activity.setContentView(gameView);
    }

    public static void setTileSet(Bitmap _tileSet)
    {
        tileSet = _tileSet;
    }

    public static void setTileMap(byte[][] map)
    {
        tileMap = map;
    }

    // gets the tile ID at the specified coordinate
    public static int getTileAtCoord(int x, int y)
    {
        if (y >= 0 && y < GameEngine.TILE_HEIGHT * tileMap.length
         && x >= 0 && x < GameEngine.TILE_WIDTH * tileMap[0].length)
            return tileMap[y / GameEngine.TILE_HEIGHT][x / GameEngine.TILE_WIDTH];
        else
            return -1;  // out of bounds
    }

    // sets the tile ID at the specified grid cell
    public static void setTile(int x, int y, byte tile)
    {
        tileMap[y][x] = tile;
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

    public static void setCenterCoord(int x, int y)
    {
        xoffset = screenWidth / displayScale / 2 - x;
        yoffset = screenHeight / displayScale / 2 - y;
    }
}