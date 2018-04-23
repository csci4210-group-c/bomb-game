package com.csci4210.engine;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/23/2018.
 */
public class GameEngineTest {


    @Test
    public void getTileAtCoord() throws Exception {
        int inputX = 50;
        int inputY=50;
        int inputMapNum = 1;
        int expectedOutput = 2;
        int output = GameEngine.getTileAtCoord(inputMapNum,inputX,inputY);
        assertEquals(expectedOutput,output);
    }

}