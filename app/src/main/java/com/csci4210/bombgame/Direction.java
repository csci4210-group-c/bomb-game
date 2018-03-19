package com.csci4210.bombgame;

enum Direction
{
    UP, DOWN, LEFT, RIGHT;

    public static Direction random()
    {
        switch ((int)(Math.random() * 4))
        {
            default:
            case 0: return UP;
            case 1: return DOWN;
            case 2: return LEFT;
            case 3: return RIGHT;
        }
    }
}