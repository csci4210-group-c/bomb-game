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

    private Direction chooseDirection(Bomber currentEnemy){

        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        int max = 0;
        for(Direction dir: dirs){

        }
    }

    //evaluates game state with a real number
    private double heuristic(int enemyX, int enemyY){
        double stateScore = 0;

        double playerX;
        double playerY;
        double distanceFromBomb;
        Bomb[] bombs;


        for(int i=0; i<bombs; i++) {
            double bombX;
            double bombY;

            if (bombX == enemyX || bombY == enemyY) {
                stateScore = -0.5;
                if ((bombX == enemyX && Math.abs(bombY - enemyY) <= 75) || (bombY == enemyY && Math.abs(bombX - enemyX) <= 75)) {
                    stateScore -= 1.5;
                }
            }

            distanceFromBomb = Math.pow(Math.pow(Math.abs(bombX-enemyX), 2) + Math.pow(Math.abs(bombY-enemyY), 2), 0.5);
            stateScore += distanceFromBomb*0.12;
        }

        double distanceFromPlayer = Math.pow(Math.pow(Math.abs(playerX-enemyX), 2) + Math.pow(Math.abs(playerY-enemyY), 2), 0.5);
        stateScore = distanceFromPlayer*0.15;


        return stateScore;
    }
}
