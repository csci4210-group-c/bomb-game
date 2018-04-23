package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by Pr.Game on 4/22/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class BomberTest {

    @Mock
    GameEngine gameEngine = Mockito.mock(GameEngine.class);
    Bomber player = Mockito.mock(Player.class);


    @Test
    public void isOnFireTile() throws Exception {


        assertTrue(player.isOnFireTile());
    }

}