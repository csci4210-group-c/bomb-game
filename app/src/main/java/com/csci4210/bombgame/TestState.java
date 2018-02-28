package com.csci4210.bombgame;

/**
 * Created by cameron on 2/13/18.
 */

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;

// Simple state for testing the animations
class TestState extends State
{
    enum Direction {UP, DOWN, LEFT, RIGHT};
    final int BUTTON_BOMB = 0;
    private Sprite bomberSprite;
    private Direction movementDir = Direction.RIGHT;

    public void enter()
    {
        bomberSprite = GameEngine.createSprite(
                GameResources.bomberSpriteSheetRight,
                GameResources.bomberWalkAnimSeq,
                100, 100,
                32, 32);
        GameEngine.createButton(GameEngine.screenWidth - 100, GameEngine.screenHeight - 100, 75, 75, "BOMB", BUTTON_BOMB);
    }

    public void exit()
    {
        GameEngine.destroySprite(bomberSprite);
    }

    public void update()
    {
        switch (movementDir)
        {
        case UP:
            if (--bomberSprite.y <= 100)
            {
                movementDir = Direction.RIGHT;
                bomberSprite.spriteSheet = GameResources.bomberSpriteSheetRight;
            }
            break;
        case DOWN:
            if (++bomberSprite.y >= 300)
            {
                movementDir = Direction.LEFT;
                bomberSprite.spriteSheet = GameResources.bomberSpriteSheetLeft;
            }
            break;
        case LEFT:
            if (--bomberSprite.x <= 100)
            {
                movementDir = Direction.UP;
                bomberSprite.spriteSheet = GameResources.bomberSpriteSheetUp;
            }
            break;
        case RIGHT:
            if (++bomberSprite.x >= 300)
            {
                movementDir = Direction.DOWN;
                bomberSprite.spriteSheet = GameResources.bomberSpriteSheetDown;
            }
            break;
        }
    }

    public void onBackPressed()
    {
        GameEngine.setState(MainActivity.title);
    }
}
