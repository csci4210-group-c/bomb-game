package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;

abstract class Bomber implements VaryingSprites
{
    final int WIDTH = 16;
    final int HEIGHT = 16;
    Sprite sprite;
    int x;
    int y;
    Direction direction;

    public abstract void update();

    @Override
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

    public boolean canWalkInDirection(Direction direction)
    {
        int newx = x;
        int newy = y;

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

                // top left
        return (GameEngine.getTileAtCoord(0,newx - WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
                // top right
                && GameEngine.getTileAtCoord(0,newx + WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
                // bottom left
                && GameEngine.getTileAtCoord(0,newx - WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS
                // bottom right
                && GameEngine.getTileAtCoord(0,newx + WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS);
    }

    public boolean walk(Direction direction)
    {
        int newx = x;
        int newy = y;

        if (direction == null)  // not moving
        {
            sprite.startAnimSequence(GameResources.bomberStillAnimSeq);
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

        sprite.setAnimSequence(GameResources.bomberWalkAnimSeq);

        // Do collision check with grid

        // top left
        if (GameEngine.getTileAtCoord(0,newx - WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
                // top right
                && GameEngine.getTileAtCoord(0,newx + WIDTH / 2, newy - HEIGHT / 2) == GameResources.TILE_GRASS
                // bottom left
                && GameEngine.getTileAtCoord(0,newx - WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS
                // bottom right
                && GameEngine.getTileAtCoord(0,newx + WIDTH / 2, newy + HEIGHT / 2) == GameResources.TILE_GRASS)
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

    public boolean isOnFireTile()
    {
        return (GameEngine.getTileAtCoord(1, x, y) == GameResources.TILE_FIRE);
    }
}
