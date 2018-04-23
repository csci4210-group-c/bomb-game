package com.csci4210.bombgame;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.csci4210.engine.GameEngine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/23/2018.
 */


@RunWith(AndroidJUnit4.class)

public class BomberTest {


    private Context context;

    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
        //GameResources.load(context);
      //  MainActivity activity = new MainActivity();
        //GameResources.load(context);


        GameEngine.init((Activity)context);
        //GameEngine.setState(TitleState.instance);
        //GameEngine.mainLoop();
    }

    @Test
    public void isOnFireTile() throws Exception {

        int x =BattleState.instance.bombers[1].x;
        int y =BattleState.instance.bombers[1].y;



        Bomber bomber = new Player(x,y);
        int tile = GameEngine.getTileAtCoord(1,x,y);
        boolean testMethod = bomber.isOnFireTile();
        boolean expected = tile == GameResources.TILE_FIRE;
        assertEquals(testMethod,expected);
    }

}