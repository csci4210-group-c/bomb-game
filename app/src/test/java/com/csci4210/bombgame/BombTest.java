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



}