package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;

public class Player extends Bomber implements VaryingSprites
{
    Direction walkDir;

    Player(int x, int y)
    {
        super(x, y);
    }

    public void update()
    {
        walk(walkDir);
    }

    @Override
    public void faceDirection(Direction direction)
    {
        if (this.direction == direction)
            return;
        this.direction = direction;
        switch (direction)
        {
            case UP:
                sprite.spriteSheet = GameResources.heroSpriteSheetUp;
                break;
            case DOWN:
                sprite.spriteSheet = GameResources.heroSpriteSheetDown;
                break;
            case LEFT:
                sprite.spriteSheet = GameResources.heroSpriteSheetLeft;
                break;
            case RIGHT:
                sprite.spriteSheet = GameResources.heroSpriteSheetRight;
                break;
        }
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
