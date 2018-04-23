package com.csci4210.bombgame;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/23/2018.
 */
public class PlayerTest {

    private MainActivity activity;
    private Context context;

    private Player player;

    @Before
    public void setUp(){
        activity = (MainActivity) InstrumentationRegistry.getTargetContext();
        
         player = new Player(5,50);

    }

    @Test
    public void onTouchDown() throws Exception {
        int inputX = GameEngine.screenWidth - 10;
        int inputY = GameEngine.screenHeight;

        player.onTouchDown(inputX, inputY);
        assertEquals(player.walkDir, Direction.LEFT);
    }

    @Test
    public void onTouchUp() throws Exception {
    }

}