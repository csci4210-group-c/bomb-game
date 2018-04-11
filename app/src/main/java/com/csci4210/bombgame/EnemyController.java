package com.csci4210.bombgame;

// This class contains the AI logic for an enemy character

import com.csci4210.engine.GameEngine;

// simple dummy enemy movement
public class EnemyController
{
    private Bomber bomber;
    private BattleState battleState;

    private static final int maxDepth = 2;
    private int stepsRemaining;

    private static Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private static Direction bestDirection = null;


    EnemyController(Bomber bomber, BattleState battleState)
    {
        this.bomber = bomber;
        this.battleState = battleState;
    }

    public void update()
    {
        if (stepsRemaining == 0 || !bomber.walk(bestDirection))
        {
            int maxSum = 0;

            bestDirection = null;
            for (Direction d : dirs)
            {
                // Do not try directions in which we can't move
                if (!bomber.canWalkInDirection(d))
                    continue;

                int x = bomber.x;
                int y = bomber.y;
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
            /*
            Bomber player = battleState.player;
            int px = player.x;
            int py = player.y;
            int bx = bomber.x;
            int by = bomber.y;

            //trying to go away from the player
            if (px-bx > py-by)
            {
                if (px-bx > by-py)
                    bestDirection = Direction.LEFT;
                else
                    bestDirection = Direction.DOWN;
            }
            else
            {
                if (bx-px > py-by)
                    bestDirection = Direction.RIGHT;
                else
                    bestDirection = Direction.UP;
            }
            */

            //to go for max score state
            /*double maxScore = Double.NEGATIVE_INFINITY;
            for(int i=0; i<dirs.length; i++){
                Bomber newBomber = bomber;
                newBomber.walk(dirs[i]);
                double newScore = heuristic(newBomber.x, newBomber.y);
                if(newScore > maxScore){
                    maxScore = newScore;
                    bestDirection = dirs[i];
                }
            }*/


            //bestDirection = dirs[((int)(Math.random()*4))%3];

            //bestDirection = chooseDirection(bomber);

            stepsRemaining = (int)(Math.random() * 100);
        }
        stepsRemaining--;
    }

    //looks few steps in the future to choose max score state
    private Direction chooseDirection(Bomber enemy){

        int max = 0;

        double bestVal = alphabeta(enemy, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
        return bestDirection;
    }

    private double alphabeta(Bomber enemy, int depth, double a, double b, boolean enemyTurn) {
        if(depth == 0) {      //max depth is explored

            return heuristic(enemy.x, enemy.y);
        }

        if(enemyTurn) {                     //enemy's turn(maximizer)
            double val = Double.NEGATIVE_INFINITY;
            for(int i=0; i<dirs.length; i++) {    //running through possible directions
                Direction maxDirection = dirs[i];

                val = Math.max(val, alphabeta(enemy,depth-1, a, b, false));
                if(depth == maxDepth && Double.compare(val, a)>0) { //if we have a better value
                    bestDirection = maxDirection;
                }
                if(b<=a)                 //if we can't get any better
                    break;
                a = Math.max(a, val);    //updating alpha
            }
            return val;
        }
        else {                             //player's turn(minimizer)
            double val = Double.POSITIVE_INFINITY;
            for(int i=0; i<dirs.length; i++) {    //running through possible directions
                Direction minDirection = dirs[i];

                val = Math.min(val, alphabeta(enemy,depth-1, a, b, true));
                if(depth == maxDepth && Double.compare(val, b)<0) {
                    bestDirection = minDirection;
                }
                if(b<=a)
                    break;
                b = Math.min(b, val);
            }
            return val;
        }
    }

    //evaluates game state with a real number
    private double heuristic(int enemyX, int enemyY){
        double stateScore = 0;
        Bomber player = battleState.player;

        for(Bomb bomb : battleState.bombs) {
            if (bomb != null) {
                int bombX = bomb.getSprite().x;
                int bombY = bomb.getSprite().y;

                if (bombX == enemyX || bombY == enemyY) {
                    stateScore = -0.5;
                    if ((bombX == enemyX && Math.abs(bombY - enemyY) <= 75) || (bombY == enemyY && Math.abs(bombX - enemyX) <= 75)) {
                        stateScore -= 1.5;
                    }
                }

                double distanceFromBomb = Math.pow(Math.pow(Math.abs(bombX - enemyX), 2) + Math.pow(Math.abs(bombY - enemyY), 2), 0.5);
                stateScore += distanceFromBomb * 0.12;
            }
        }

        double distanceFromPlayer = Math.pow(Math.pow(Math.abs(player.x-enemyX), 2) + Math.pow(Math.abs(player.y-enemyY), 2), 0.5);
        stateScore = distanceFromPlayer*0.15;

        return stateScore;
    }
}
