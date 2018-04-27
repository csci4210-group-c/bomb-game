package com.csci4210.bombgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        bomber = new Player(45,60);


    }
    @Test
    public void isOnFireTile() throws Exception {

        assertFalse(bomber.isOnFireTile());
    }

    @Test

    public void canWalkDirection(){

        // case 1 :
        direction = Direction.RIGHT;
        boolean expectedOutput = bomber.canWalkInDirection(direction);
        assertTrue(expectedOutput);
        // case 2 :
        direction = Direction.LEFT;
        expectedOutput = bomber.canWalkInDirection(direction);
        assertTrue(expectedOutput);
        // case 3
        direction = Direction.UP;
        expectedOutput = bomber.canWalkInDirection(direction);
        assertTrue(expectedOutput);
        //case 4
        direction = Direction.DOWN;
        expectedOutput = bomber.canWalkInDirection(direction);
        assertTrue(expectedOutput);

    }

    @Test

    public void walk(){

        // case 1
        direction = Direction.RIGHT;
        boolean expectedOutput = bomber.walk(direction);
        int expectedX = 46;
        int expectedY = 60;
        int actualX =bomber.x;
        int actualY=bomber.y;
        assertTrue(expectedOutput);
        assertEquals(expectedX,actualX);
        assertEquals(expectedY,actualY);
        // case 2
        direction = Direction.LEFT;
         expectedOutput = bomber.walk(direction);
         expectedX = 45;
         expectedY = 60;
         actualX =bomber.x;
         actualY=bomber.y;
        assertTrue(expectedOutput);
        assertEquals(expectedX,actualX);
        assertEquals(expectedY,actualY);
        // case 3
        direction = Direction.UP;
         expectedOutput = bomber.walk(direction);
         expectedX = 45;
         expectedY = 59;
         actualX =bomber.x;
         actualY=bomber.y;
        assertTrue(expectedOutput);
        assertEquals(expectedX,actualX);
        assertEquals(expectedY,actualY);
        // case 4
        direction = Direction.DOWN;
        expectedOutput = bomber.walk(direction);
        expectedX = 45;
        expectedY = 60;
        actualX =bomber.x;
        actualY=bomber.y;
        assertTrue(expectedOutput);
        assertEquals(expectedX,actualX);
        assertEquals(expectedY,actualY);


    }

    @Test
    // Have not been setUp correctly, to set up later
    public void faceDirection(){
        bomber.faceDirection(Direction.UP);
        Bitmap expected = bomber.sprite.spriteSheet;
        Bitmap actual = GameResources.bomberSpriteSheetLeft;
        System.out.println(expected);
        assertEquals(expected,actual);
    }
}