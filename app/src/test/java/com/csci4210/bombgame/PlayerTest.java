package com.csci4210.bombgame;

import android.app.Activity;
import android.content.Context;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/23/2018.
 */
public class PlayerTest {


    Player player ;

    @Before
    public void setUp(){
        player = new Player(50,50);

        GameEngine.screenHeight = 400;
        GameEngine.screenWidth=800;

    }
    @Test
    public void onTouchDown() throws Exception {

        int inputX=80;
        int inputY=50;
        player.onTouchDown(inputX,inputY);
        Direction expectedDirection = Direction.LEFT;
        Direction actualDirection = player.walkDir;
        assertEquals(expectedDirection,actualDirection);
    }

    @Test
    public void onTouchUp() throws Exception {
        int inputX=80;
        int inputY=50;
        player.onTouchUp(inputX,inputY);
        Direction expectedDirection = null;
        Direction actualDirection = player.walkDir;
        assertEquals(expectedDirection,actualDirection);

    }

}