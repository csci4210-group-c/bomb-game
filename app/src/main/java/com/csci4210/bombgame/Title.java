package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class Title extends State{
    public void enter(){
        GameEngine.setBackdrop(GameResources.titleBackground);
    }
}
