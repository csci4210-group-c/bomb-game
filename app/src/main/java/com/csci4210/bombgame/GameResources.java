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
    static Bitmap tileSet;
    static Bitmap battleBackground;
    static Bitmap blastSequence;
    static Bitmap lossMessage;
    static Bitmap winMessage;

    static int placeBomb;
    static int detonateBomb;
    static int timeWarning;

    static final int bomberWalkAnimSeq[][] =
    {
        {ANIMCMD_FRAME, 0, 8},
        {ANIMCMD_FRAME, 1, 8},
        {ANIMCMD_FRAME, 0, 8},
        {ANIMCMD_FRAME, 2, 8},
        {ANIMCMD_GOTO,  0},
    };

    static final int bomberStillAnimSeq[][] =
    {
        {ANIMCMD_FRAME, 0, 0},
        {ANIMCMD_END},
    };

    static final int bombFlashAnimSeq[][] =
    {
        {ANIMCMD_FRAME, 0, 30},
        {ANIMCMD_FRAME, 1, 30},
        {ANIMCMD_GOTO, 0},
    };

    static final int bombBlastAnimSeq[][] =
    {
        // explode
        {ANIMCMD_FRAME, 1, 30},
        {ANIMCMD_FRAME, 2, 10},
        {ANIMCMD_FRAME, 3, 10},
        {ANIMCMD_FRAME, 4, 10},
        {ANIMCMD_FRAME, 5, 10},
        {ANIMCMD_FRAME, 6, 10},
        {ANIMCMD_END, 0},
    };

    static final byte TILE_GRASS = 1;
    static final byte TILE_BRICK = 2;
    static final byte TILE_STONE = 3;
    static final byte TILE_FIRE = 4;

    static final byte level1TileMap[][] = {
            {TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS},
            {TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_STONE, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS},
            {TILE_STONE, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE},
            {TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_STONE},
            {TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_BRICK, TILE_BRICK},
            {TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK},
            {TILE_GRASS, TILE_STONE, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_STONE, TILE_STONE, TILE_GRASS},
            {TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS},
            {TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS},
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
        tileSet                = BitmapFactory.decodeResource(rsrc, R.drawable.alltiles, opts);
        battleBackground       = BitmapFactory.decodeResource(rsrc, R.drawable.outfloor, opts);
        blastSequence          = BitmapFactory.decodeResource(rsrc, R.drawable.blastsprite, opts);
        lossMessage            = BitmapFactory.decodeResource(rsrc, R.drawable.lose, opts);
        winMessage             = BitmapFactory.decodeResource(rsrc, R.drawable.win, opts);

        placeBomb = R.raw.drop_bomb;
        detonateBomb = R.raw.explosion;
        timeWarning = R.raw.timer;
    }
}
