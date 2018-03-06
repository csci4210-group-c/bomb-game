package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;

enum Direction {UP, DOWN, LEFT, RIGHT};

class Bomber
{
    Sprite sprite;
    int x;
    int y;
    Direction direction;
    boolean isMoving = false;

    public void faceDirection(Direction direction)
    {
        if (this.direction == direction)
            return;
        this.direction = direction;
        switch (direction)
        {
            case UP:
                sprite.spriteSheet = GameResources.bomberSpriteSheetUp;
                break;
            case DOWN:
                sprite.spriteSheet = GameResources.bomberSpriteSheetDown;
                break;
            case LEFT:
                sprite.spriteSheet = GameResources.bomberSpriteSheetLeft;
                break;
            case RIGHT:
                sprite.spriteSheet = GameResources.bomberSpriteSheetRight;
                break;
        }
    }

    public Bomber(int x, int y)
    {
        this.x = x;
        this.y = y;
        sprite = GameEngine.createSprite(null, GameResources.bomberWalkAnimSeq,
                x, y, 32, 32);
        faceDirection(Direction.DOWN);
    }

    public void walk(Direction direction)
    {
        if (direction == null)  // not moving
        {
            isMoving = false;
            sprite.animSequence = GameResources.bomberStillAnimSeq;
            sprite.animCmdIndex = 0;
            return;
        }

        faceDirection(direction);
        switch (direction)
        {
            case UP:
                y--;
                sprite.y = y;
                break;
            case DOWN:
                y++;
                sprite.y = y;
                break;
            case LEFT:
                x--;
                sprite.x = x;
                break;
            case RIGHT:
                x++;
                sprite.x = x;
                break;
        }
        sprite.animSequence = GameResources.bomberWalkAnimSeq;
        isMoving = true;
    }
}

public class BattleState extends State
{
    private Bomber player;
    Direction walkDir;

    public void enter()
    {
        GameEngine.setTileSet(GameResources.tileSet);
        GameEngine.setTileMap(GameResources.level1TileMap);
        player = new Bomber(50, 50);
    }

    public void onTouchDown(int x, int y)
    {
        walkDir = null;

        x -= player.x;
        y -= player.y;

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

    public void update()
    {
        player.walk(walkDir);
    }
}