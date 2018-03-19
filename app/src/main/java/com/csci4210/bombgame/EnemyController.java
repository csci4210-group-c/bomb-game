package com.csci4210.bombgame;

// This class contains the AI logic for an enemy character

// simple dummy enemy movement
public class EnemyController
{
    private Bomber bomber;

    private Direction direction;
    private int stepsRemaining;

    EnemyController(Bomber bomber)
    {
        this.bomber = bomber;
    }

    public void update()
    {
        if (stepsRemaining == 0)
        {
            direction = Direction.random();
            stepsRemaining = (int)(Math.random() * 100);
        }
        if (!bomber.walk(direction))
            direction = Direction.random();
        stepsRemaining--;
    }
}
