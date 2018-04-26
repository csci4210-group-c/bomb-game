package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/26/2018.
 */
public class BombTest {

    static final byte level1TileMap[][]=GameResources.level1TileMap;

    Bomb bomb;
    @Before
    public void setUp(){
        GameEngine.setTileMap(level1TileMap);
        bomb = new Bomb(32,32);
    }

    @Test
    public void blastTile(){
        // case 1
        boolean expectedOutput = bomb.blastTile(3,1);
        boolean actualOutput = false;
        int expectedTile = GameEngine.getTile(0,3,1);
        int actualTile=3;
        assertEquals(expectedTile,actualTile);
        assertEquals(expectedOutput,actualOutput);

        // case 2
        expectedOutput = bomb.blastTile(2,1);
        actualOutput = true;
        expectedTile = GameEngine.getTile(0,2,1);
        actualTile=1;
        assertEquals(expectedTile,actualTile);
        assertEquals(expectedOutput,actualOutput);
    }





}