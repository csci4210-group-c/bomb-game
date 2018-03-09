package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;

enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction random()
    {
        switch ((int)(Math.random() * 4))
        {
            case 0: return UP;
            case 1: return DOWN;
            case 2: return LEFT;
            case 3: return RIGHT;
        }
        // should not happen
        return UP;
    }
}

class Bomber
{
    final int WIDTH = 16;
    final int HEIGHT = 16;
    Sprite sprite;
    int x;
    int y;
    Direction direction;

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

    public boolean walk(Direction direction)
    {
        int newx = x;
        int newy = y;

        if (direction == null)  // not moving
        {
            sprite.animSequence = GameResources.bomberStillAnimSeq;
            sprite.animCmdIndex = 0;
            return false;
        }

        faceDirection(direction);
        switch (direction)
        {
            case UP:
                newy--;
                break;
            case DOWN:
                newy++;
                break;
            case LEFT:
                newx--;
                break;
            case RIGHT:
                newx++;
                break;
        }

        sprite.animSequence = GameResources.bomberWalkAnimSeq;

        // Do collision check with grid

          // top left
        if (GameEngine.getTileAtCoord(newx - WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
         // top right
         && GameEngine.getTileAtCoord(newx + WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
         // bottom left
         && GameEngine.getTileAtCoord(newx - WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS
         // bottom right
         && GameEngine.getTileAtCoord(newx + WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS)
        {
            // was able to move
            x = newx;
            y = newy;
            sprite.x = x;
            sprite.y = y;
            return true;
        }
        else {
            // was not able to move
            return false;
        }
    }
}

public class BattleState extends State
{
    private Bomber player;
    private Bomber enemy;
    Direction walkDir;
    final int BUTTON_BOMB = 0;

    public void enter()
    {
        GameEngine.createButton(GameEngine.screenWidth - 100, GameEngine.screenHeight - 100,
                200, 200, "BOMB!", BUTTON_BOMB);
        GameEngine.setTileSet(GameResources.tileSet);
        GameEngine.setTileMap(GameResources.level1TileMap);
        player = new Bomber(50, 50);
        enemy = new Bomber(120, 100);
    }

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

    // simple dummy enemy movement
    private Direction enemyDir;
    private int enemySteps;
    private void updateEnemy()
    {
        if (enemySteps == 0)
        {
            enemyDir = Direction.random();
            enemySteps = (int)(Math.random() * 100);
        }
        if (!enemy.walk(enemyDir))
            enemyDir = Direction.random();
        enemySteps--;
    }

    public void update()
    {
        updateEnemy();
        player.walk(walkDir);
        GameEngine.setCenterCoord(player.x, player.y);
    }
}
