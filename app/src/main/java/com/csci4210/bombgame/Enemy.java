package com.csci4210.bombgame;

// This class contains the AI logic for an enemy character

import com.csci4210.engine.GameEngine;

// simple dummy enemy movement
public class Enemy extends Bomber
{
    private BattleState battleState;

    private static final int maxDepth = 2;
    private int stepsRemaining;

    private Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Direction bestDirection = null;

    Enemy(int x, int y, BattleState battleState)
    {
        super(x, y);
        this.battleState = battleState;
        bestDirection = Direction.random();
    }

    public void update()
    {
        if (stepsRemaining == 0 || !walk(bestDirection))
        {
            int maxSum = 0;

            bestDirection = null;
            for (Direction d : dirs)
            {
                // Do not try directions in which we can't move
                if (!canWalkInDirection(d))
                    continue;

                int x = this.x;
                int y = this.y;
                final int offset = GameEngine.TILE_WIDTH * 5;
                int sum = 0;

                switch (d)
                {
                    case UP:    y -= offset; break;
                    case DOWN:  y += offset; break;
                    case LEFT:  x -= offset; break;
                    case RIGHT: x += offset; break;
                }

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
