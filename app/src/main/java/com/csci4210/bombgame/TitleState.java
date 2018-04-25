package com.csci4210.bombgame;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;
import com.csci4210.engine.TextLabel;

class GameName
{
    TextLabel label;

    GameName(String name)
    {
        label = GameEngine.createTextLabel(GameEngine.screenWidth / 2, 100, name);
    }

}
public class TitleState extends State{

    final int BUTTON_START = 0;
    final int BUTTON_QUIT = 1;

    public static final TitleState instance = new TitleState();
    private GameName gameName;

    public void enter(){

        gameName = new GameName("BOMBERMAN RETURNS");
        GameEngine.setBackdrop(GameResources.titleBackground);
        GameEngine.createButton(GameEngine.screenWidth/2, GameEngine.screenHeight/2, 300, 100, "PLAY", BUTTON_START);
        GameEngine.createButton(GameEngine.screenWidth/2, (GameEngine.screenHeight/2) + 200, 300, 100, "QUIT GAME", BUTTON_QUIT);
    }

    public void update(){

    }

    public void onButton(int buttonId)
    {
        switch (buttonId)
        {
            case BUTTON_START:
                GameEngine.setState(BattleState.instance);
                break;
            case BUTTON_QUIT:
                GameEngine.setState(null);
                break;
        }
    }
}
