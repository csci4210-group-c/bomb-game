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
        GameEngine.createButton(300, 300, 300, 100, "Start", BUTTON_START);
        GameEngine.createButton(300, 500, 300, 100, "Start", BUTTON_START);
    }

    public void exit(){
        GameEngine.destroyAllButtons();
    }

    public void update(){

    }

    public void onTouch(int x, int y)
    {
        switch (GameEngine.buttonHitTest(x, y))
        {
            case BUTTON_START:
                GameEngine.setState(MainActivity.testState);
                break;
            case BUTTON_QUIT:
                break;
        }
    }
}
