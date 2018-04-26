package com.csci4210.bombgame;

import android.os.Bundle;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.KeyStore;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/22/2018.
 */
public class BomberTest {

    static final byte level1TileMap[][] = GameResources.level1TileMap;

    Bomber bomber ;
    Direction direction;



    @Before
    public void setUp(){
        GameEngine.setTileMap(level1TileMap);
        bomber = new Player(30,30);


    }
    @Test
    public void isOnFireTile() throws Exception {

        assertFalse(bomber.isOnFireTile());
    }

    @Test
    // to be fixed with correct values
    public void canWalkDirection(){
        // case 1 :
        direction = Direction.RIGHT;
        boolean expectedOutput = bomber.canWalkInDirection(direction);
        assertFalse(expectedOutput); // when actual output should be false
        // case 2 :
        direction = Direction.LEFT;
        expectedOutput = bomber.canWalkInDirection(direction);
        assertTrue(expectedOutput); // when actual output should be true

    }

    @Test
    // to be fixed with correct values
    public void walk(){
        direction = Direction.RIGHT;
        boolean expectedOutput = bomber.walk(direction);
        int expectedX = 0;
        int expectedY = 0;
        int actualX =bomber.x;
        int actualY=bomber.y;
        assertTrue(expectedOutput);
        assertEquals(expectedX,actualX);
        assertEquals(expectedY,actualY);

    }
}