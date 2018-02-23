package com.csci4210.bombgame;

/**
 * Created by cameron on 2/13/18.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.csci4210.engine.Sprite.*;

class GameResources
{
    static Bitmap bomberSpriteSheetUp;
    static Bitmap bomberSpriteSheetDown;
    static Bitmap bomberSpriteSheetLeft;
    static Bitmap bomberSpriteSheetRight;
    static Bitmap titleBackground;

    static final int bomberWalkAnimSeq[][] =
    {
        {ANIMCMD_FRAME, 0, 8},
        {ANIMCMD_FRAME, 1, 8},
        {ANIMCMD_FRAME, 0, 8},
        {ANIMCMD_FRAME, 2, 8},
        {ANIMCMD_GOTO,  0},
    };

    static void load(Context context)
    {
        Resources rsrc = context.getResources();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        titleBackground = BitmapFactory.decodeResource(rsrc, R.drawable.dark_background, opts);
        bomberSpriteSheetUp    = BitmapFactory.decodeResource(rsrc, R.drawable.bomber_up,    opts);
        bomberSpriteSheetDown  = BitmapFactory.decodeResource(rsrc, R.drawable.bomber_down,  opts);
        bomberSpriteSheetLeft  = BitmapFactory.decodeResource(rsrc, R.drawable.bomber_left,  opts);
        bomberSpriteSheetRight = BitmapFactory.decodeResource(rsrc, R.drawable.bomber_right, opts);
    }
}
