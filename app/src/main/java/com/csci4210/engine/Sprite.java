package com.csci4210.engine;

/**
 * Created by cameron on 2/13/18.
 */

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Sprite
{
    public boolean active = false;

    public int x, y;
    public int width, height;

    public Bitmap spriteSheet;
    public int animSequence[][];
    public int animCmdIndex;
    public int animFrameNum;
    private long animTimer;

    // Anim Cmd IDs
    public static final int ANIMCMD_FRAME = 0;  // ANIMCMD_FRAME, frameNum, duration
    public static final int ANIMCMD_GOTO  = 1;  // ANIMCMD_GOTO, cmdIndex
    public static final int ANIMCMD_END   = 2;  // ANIMCMD_END

    Sprite()
    {
    }

    void setParams(Bitmap spriteSheet, int animSequence[][], int x, int y, int width, int height)
    {
        this.spriteSheet = spriteSheet;
        this.animSequence = animSequence;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Set up initial anim sequence

        int index = 0;
        int cmd = animSequence[index][0];

        while (cmd == ANIMCMD_GOTO)
        {
            index = animSequence[index][1];
            cmd = animSequence[index][0];
        }

        if (cmd == ANIMCMD_FRAME)
        {
            animFrameNum = animSequence[index][1];  // frame number
            animTimer    = animSequence[index][2];  // duration
        }

        animCmdIndex = index;
    }

    private void initFrame(int frameNum, int duration)
    {
        animFrameNum = frameNum;
        animTimer = duration;
    }

    // processes one tick of animation
    void updateAnim()
    {
        while (true)
        {
            switch (animSequence[animCmdIndex][0])
            {
                case ANIMCMD_FRAME:
                    if (animTimer > 0)
                    {
                        animTimer--;
                        return;
                    }
                    else
                    {
                        // Timer has expired. Go to the next cmd.
                        animCmdIndex++;
                        // Start next frame
                        if (animSequence[animCmdIndex][0] == ANIMCMD_FRAME)
                            initFrame(animSequence[animCmdIndex][1], animSequence[animCmdIndex][2]);
                    }
                    break;
                case ANIMCMD_GOTO:
                    animCmdIndex = animSequence[animCmdIndex][1];
                    // set up next frame
                    if (animSequence[animCmdIndex][0] == ANIMCMD_FRAME)
                        initFrame(animSequence[animCmdIndex][1], animSequence[animCmdIndex][2]);
                    break;
                case ANIMCMD_END:
                    return;  // do nothing
            }
        }
    }

    Rect getFrameSrcRect()
    {
        int frameX = 0;
        int frameY = animFrameNum * height;

        return new Rect(frameX, frameY, frameX + width, frameY + height);
    }

    Rect getDestRect()
    {
        return new Rect(x, y, x + width, y + width);
    }
}
