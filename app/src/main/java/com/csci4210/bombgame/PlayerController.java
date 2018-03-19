package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;

public class PlayerController
{
    Bomber bomber;
    Direction walkDir;

    PlayerController(Bomber bomber)
    {
        this.bomber = bomber;
    }

    public void update()
    {
        bomber.walk(walkDir);
    }

    // respond to input

    public void onTouchDown(int x, int y)
    {
        walkDir = null;

        x -= GameEngine.screenWidth / 2;
        y -= GameEngine.screenHeight / 2;

        if (Math.abs(x) > 16 || Math.abs(y) > 16)
        {
            if (x > y)  // up or right
            {
                if (x > -y)
                    walkDir = Direction.RIGHT;
                else
                    walkDir = Direction.UP;
            }
            else // down or left
            {
                if (-x > y)
                    walkDir = Direction.LEFT;
                else
                    walkDir = Direction.DOWN;
            }
        }
    }

    public void onTouchUp(int x, int y)
    {
        walkDir = null;
    }
}
