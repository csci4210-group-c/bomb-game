package com.csci4210.bombgame;

// This class contains the AI logic for an enemy character

import com.csci4210.engine.GameEngine;

// simple dummy enemy movement
public class Enemy extends Bomber
{
    private BattleState battleState;

    private int stepsRemaining;

    private Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Direction bestDirection = null;

    Enemy(int x, int y, BattleState battleState)
    {
        super(x, y);
        this.battleState = battleState;
        bestDirection = Direction.random();
    }

    private int[] getMaxInDirection(Direction d)
    {
        int destCoords[] = new int[2];
        int xinc = 0;
        int yinc = 0;

        switch (d)
        {
            case UP:    yinc = -1; break;
            case DOWN:  yinc = +1; break;
            case LEFT:  xinc = -1; break;
            case RIGHT: xinc = +1; break;
        }

        destCoords[0] = x;
        destCoords[1] = y;
        for (int i = 0; i < GameEngine.TILE_WIDTH * 5; i++)
        {
            if (GameEngine.getTileAtCoord(0, destCoords[0], destCoords[1]) != GameResources.TILE_GRASS)
                break;
            destCoords[0] += xinc;
            destCoords[1] += yinc;
        }

        return destCoords;
    }

    public void update()
    {
        /*if (Math.random() < 0.001)
            BattleState.instance.addBomb(x, y);*/
        if (stepsRemaining == 0 || !walk(bestDirection))
        {
            int maxSum = 0;

            bestDirection = null;
            for (Direction d : dirs)
            {
                // Do not try directions in which we can't move
                if (!canWalkInDirection(d))
                    continue;

                int sum = 0;
                /*
                int x = this.x;
                int y = this.y;
                final int offset = GameEngine.TILE_WIDTH * 5;

                switch (d)
                {
                    case UP:    y -= offset; break;
                    case DOWN:  y += offset; break;
                    case LEFT:  x -= offset; break;
                    case RIGHT: x += offset; break;
                }
                */
                int destCoords[] = getMaxInDirection(d);
                int x = destCoords[0];
                int y = destCoords[1];

                for (Bomb b : battleState.bombs)
                {
                    if (b != null)
                    {
                        sum += Math.abs(b.getSprite().x - x);
                        sum += Math.abs(b.getSprite().y - y);
                    }
                }

                // choose best direction based on bombs
                if (sum > maxSum)
                {
                    maxSum = sum;
                    bestDirection = d;
                }

                // if there are no bombs, choose a random direction
                if (bestDirection == null)
                    bestDirection = Direction.random();
            }

            stepsRemaining = (int)(Math.random() * 100);
        }
        stepsRemaining--;
    }
}
