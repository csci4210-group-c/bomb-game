package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/26/2018.
 */
public class EnemyTest {

    static final byte level1TileMap[][] = GameResources.level1TileMap;
    BattleState battleState;
    Enemy enemy  ;



    @Before
    public void setUp(){
        GameEngine.setTileMap(level1TileMap);
        enemy = new Enemy(30,30,battleState);
    }

    @Test
    public void getMaxInDirection(){
        // case 1
        int [] expecteOoutput = enemy.getMaxInDirection(Direction.DOWN);
        int [] actualOutput1 = {30,32};
        assertArrayEquals(expecteOoutput,actualOutput1);

        // case 2
         expecteOoutput = enemy.getMaxInDirection(Direction.UP);
         int [] actualOutput2 = {30,-1};
        assertArrayEquals(expecteOoutput,actualOutput2);

        // case 3
        expecteOoutput = enemy.getMaxInDirection(Direction.LEFT);
        int [] actualOutput3 = {-1,30};
        assertArrayEquals(expecteOoutput,actualOutput3);

        // case 4
        expecteOoutput = enemy.getMaxInDirection(Direction.RIGHT);
        int [] actualOutput4 = {96,30};
        assertArrayEquals(expecteOoutput,actualOutput4);

    }

}