/**
 * Created by cameron on 2/13/18.
 */

package com.csci4210.engine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.csci4210.bombgame.MainActivity;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.IOException;



public class GameEngine
{
    private static int displayScale = 1;
    public static int xoffset = 0;
    public static int yoffset = 0;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    private static final int MAX_SPRITES = 16;
    private static final int MAX_BUTTONS = 12;
    private static final int MAX_TEXT_LABELS = 5;
    public static final int TICKS_PER_SECOND = 60;

    public static int screenWidth;
    public static int screenHeight;

    private static Bitmap tileSet;
    private static byte tileMaps[][][] = new byte[2][][];
    private static Sprite sprites[] = new Sprite[MAX_SPRITES];
    private static Button buttons[] = new Button[MAX_BUTTONS];
    private static TextLabel textLabels[] = new TextLabel[MAX_TEXT_LABELS];
    private static Bitmap backdrop;
    private static GameView gameView;
    private static State currState;
    private static State nextState;
    private static Button pushedButton;
    private static MediaPlayer mediaPlayers[] = new MediaPlayer[4];
    private static Activity context;

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

            for (int i = 0; i < tileMaps.length; i++) {
                // draw tilemap
                byte tileMap[][] = tileMaps[i];
                if (tileMap != null && tileSet != null) {
                    int tileWidth = TILE_WIDTH * displayScale;
                    int tileHeight = TILE_HEIGHT * displayScale;
                    int tileX, tileY;
                    int destX, destY;
                    int srcWidth = tileSet.getWidth();

                    for (tileY = 0; tileY < tileMap.length; tileY++) {
                        destY = yoffset * displayScale + tileY * tileHeight;
                        for (tileX = 0; tileX < tileMap[tileY].length; tileX++) {
                            byte tileId = tileMap[tileY][tileX];

                            if (tileId != 0) {  // tile ID 0 is transparent
                                tileId -= 1;  // get 0 based index of tile in tileset
                                destX = xoffset * displayScale + tileX * tileWidth;
                                destRect.set(destX, destY, destX + tileWidth, destY + tileHeight);
                                srcRect.set(0, tileId * srcWidth, srcWidth, tileId * srcWidth + srcWidth);
                                canvas.drawBitmap(tileSet, srcRect, destRect, paint);
                            }
                        }
                    }
                }
            }
            // draw sprites
            for (Sprite sprite : GameEngine.sprites)
            {
                if (sprite != null)
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

            // draw text labels
            for (TextLabel label : GameEngine.textLabels)
            {
                if (label != null)
                {
                    canvas.drawText(label.text, label.x, label.y, paint);
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
        // initialize Android view
        gameView = new GameView(activity);
        //getting view's default dimensions on runtime
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)gameView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        int screenDimMax = Math.max(screenWidth, screenHeight);
        displayScale = Math.max(screenDimMax / 500, 1);
        activity.setContentView(gameView);
        context = activity;
    }

    public static void setTileSet(Bitmap _tileSet)
    {
        tileSet = _tileSet;
    }

    public static void setTileMap(byte[][] map)
    {
        if (map == null)  // disable tile map
        {
            tileMaps[0] = null;
            tileMaps[1] = null;
            return;
        }

        int columns = map[0].length;
        int rows = map.length;

        // allocate two tilemaps
        tileMaps[0] = new byte[rows][columns];
        tileMaps[1] = new byte[rows][columns];

        // initialize bottom tilemap
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                tileMaps[0][r][c] = map[r][c];
    }

    // gets the tile ID at the specified coordinate
    public static int getTileAtCoord(int mapNum, int x, int y)
    {
        if (y >= 0 && y < GameEngine.TILE_HEIGHT * tileMaps[0].length
         && x >= 0 && x < GameEngine.TILE_WIDTH * tileMaps[0][0].length)
            return tileMaps[mapNum][y / GameEngine.TILE_HEIGHT][x / GameEngine.TILE_WIDTH];
        else
            return -1;  // out of bounds
    }

    // sets the tile ID at the specified grid cell
    public static void setTile(int mapNum, int x, int y, byte tile)
    {
        byte tileMap[][] = tileMaps[mapNum];

        if (y >= 0 && y < tileMap.length && x >= 0 && x < tileMap[0].length)
            tileMap[y][x] = tile;
    }

    // gets the tile ID at the specified grid cell
    public static byte getTile(int mapNum, int x, int y)
    {
        byte tileMap[][] = tileMaps[mapNum];

        if (y >= 0 && y < tileMap.length && x >= 0 && x < tileMap[0].length)
            return tileMap[y][x];
        else
            return -1;  // out of bounds
    }

    public static void setState(State state)
    {
        nextState = state;
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

                // handle state change
                if (nextState != currState)
                {
                    if (nextState == null)  // Exit if state was set to null
                    {
                        context.finish();
                        return;
                    }

                    // End current state
                    if (currState != null)
                        currState.exit();

                    // Clean up resources
                    destroyAllButtons();
                    destroyAllSprites();
                    destroyAllTextLabels();
                    setTileMap(null);
                    setBackdrop(null);

                    // Start new state
                    currState = nextState;
                    currState.enter();
                }

                currState.update();
                for (Sprite sprite : GameEngine.sprites)
                {
                    if (sprite != null)
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
        for (int i = 0; i < sprites.length; i++)
        {
            if (sprites[i] == null)
            {
                sprites[i] = new Sprite(spriteSheet, animSeq, x, y, width, height);
                return sprites[i];
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

    public static void destroySprite(Sprite sprite)
    {
        for (int i = 0; i < sprites.length; i++)
        {
            if (sprites[i] == sprite) {
                sprites[i] = null;
                return;
            }
        }
    }

    public static void destroyAllSprites()
    {
        for (int i = 0; i < sprites.length; i++)
            sprites[i] = null;
    }

    public static void setCenterCoord(int x, int y)
    {
        xoffset = screenWidth / displayScale / 2 - x;
        yoffset = screenHeight / displayScale / 2 - y;
    }

    public static void playSound(int sound)
    {
        // release finished players
        for (int i = 0; i < mediaPlayers.length; i++)
        {
            if (mediaPlayers[i] != null && !mediaPlayers[i].isPlaying()) {
                mediaPlayers[i].release();
                mediaPlayers[i] = null;
            }
        }

        // create new media player
        for (int i = 0; i < mediaPlayers.length; i++)
        {
            if (mediaPlayers[i] == null)
            {
                mediaPlayers[i] = MediaPlayer.create(context, sound);
                mediaPlayers[i].start();
                break;
            }
        }
    }

    public static TextLabel createTextLabel(int x, int y, String text)
    {
        for (int i = 0; i < textLabels.length; i++)
        {
            if (textLabels[i] == null)
            {
                textLabels[i] = new TextLabel(x, y, text);
                return textLabels[i];
            }
        }
        return null;
    }

    static void destroyAllTextLabels()
    {
        for (int i = 0; i < textLabels.length; i++)
            textLabels[i] = null;
    }
}
