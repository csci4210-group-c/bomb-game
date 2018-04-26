package com.csci4210.engine;

import com.csci4210.bombgame.GameResources;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by Pr.Game on 4/23/2018.
 */
public class GameEngineTest {

    static final byte TILE_GRASS = 1;
    static final byte TILE_BRICK = 2;
    static final byte TILE_STONE = 3;


    static final byte level1TileMap[][] =  {
            {TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS},
            {TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_STONE, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS},
            {TILE_STONE, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE},
            {TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_STONE},
            {TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_BRICK, TILE_BRICK},
            {TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK},
            {TILE_GRASS, TILE_STONE, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_STONE, TILE_STONE, TILE_STONE, TILE_GRASS},
            {TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS},
            {TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_BRICK, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_GRASS, TILE_BRICK, TILE_GRASS},
    };

    @Before
    public void setUp(){
        GameEngine.setTileMap(level1TileMap);
    }
    @Test
    public void getTileAtCoord() throws Exception {


        int inputX = 32;
        int inputY=32;
        int inputMapNum = 0;
        int expectedOutput = 1;
        int output = GameEngine.getTileAtCoord(inputMapNum,inputX,inputY);
        assertEquals(expectedOutput,output);
    }

    @Test
    public void getTile () throws Exception {

        int inputX =4;
        int inputY=5;
        int inputMapNum = 0;

        int expectedOutput = 2;
        byte output = GameEngine.getTile(inputMapNum,inputX,inputY);
        assertEquals(expectedOutput,output);
    }

    @Test
    public void setTile() throws Exception{
        int inputX =1;
        int inputY=6;
        int inputMapNum = 0;
        byte tile = 2;
        GameEngine.setTile(inputMapNum,inputX,inputY,tile);
        byte outputTile = GameEngine.getTile(inputMapNum,inputX,inputY);
        assertEquals(tile,outputTile);

    }

}