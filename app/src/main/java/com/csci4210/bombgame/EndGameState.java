package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class EndGameState extends State
{
    public static final EndGameState instance = new EndGameState();

    private static final int BUTTON_PLAY_AGAIN = 0;
    private static final int BUTTON_QUIT = 1;

    public void enter()
    {
        if(BattleState.instance.bombers[0] == null)
            GameEngine.setBackdrop(GameResources.lossMessage);
        else
            GameEngine.setBackdrop(GameResources.winMessage);

        GameEngine.createButton(
                GameEngine.screenWidth / 2, GameEngine.screenHeight / 2,
                300, 100, "Play Again", BUTTON_PLAY_AGAIN);
        GameEngine.createButton(
                GameEngine.screenWidth / 2, GameEngine.screenHeight / 2 + 200,
                300, 100, "Quit", BUTTON_QUIT);
    }

    public void onButton(int button)
    {
        switch (button)
        {
            case BUTTON_PLAY_AGAIN:
                GameEngine.setState(BattleState.instance);
                break;
            case BUTTON_QUIT:
                GameEngine.setState(TitleState.instance);
                break;
        }
    }
}
