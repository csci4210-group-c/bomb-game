package com.csci4210.bombgame;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class TitleState extends State{

    final int BUTTON_START = 0;
    final int BUTTON_QUIT = 1;

    public void enter(){
        GameEngine.setBackdrop(GameResources.titleBackground);
        GameEngine.createButton(GameEngine.screenWidth/2, GameEngine.screenHeight/2, 300, 100, "PLAY GAME", BUTTON_START);
        GameEngine.createButton(GameEngine.screenWidth/2, (GameEngine.screenHeight/2) + 200, 300, 100, "QUIT", BUTTON_START);
    }

    public void exit(){
        GameEngine.destroyAllButtons();
    }

    public void update(){

    }

    public void onButton(int buttonId)
    {
        switch (buttonId)
        {
            case BUTTON_START:
                GameEngine.setState(MainActivity.testState);
                break;
            case BUTTON_QUIT:
                GameEngine.setState(null);
                break;
        }
    }
}
