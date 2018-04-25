package com.csci4210.bombgame;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class EndGameState extends State
{
    public static final EndGameState instance = new EndGameState();

    private static final int BUTTON_PLAY_AGAIN = 0;
    private static final int BUTTON_QUIT = 1;
    private boolean won;

    public void setResult(boolean won)
    {
        this.won = won;
    }

    public void enter()
    {
        if(won){
            GameEngine.setBackdrop(GameResources.winMessage);
        }
        else {
            GameEngine.setBackdrop(GameResources.lossMessage);
        }
        GameEngine.createButton(
                GameEngine.screenWidth / 2, GameEngine.screenHeight / 2,
                300, 100, "PLAY AGAIN", BUTTON_PLAY_AGAIN);
        GameEngine.createButton(
                GameEngine.screenWidth / 2, GameEngine.screenHeight / 2 + 200,
                300, 100, "QUIT GAME", BUTTON_QUIT);
        for(int i=0; i<GameEngine.mediaPlayers.length; i++)
            GameEngine.mediaPlayers[i].stop();
    }

    public void onButton(int button)
    {
        switch (button)
        {
            case BUTTON_PLAY_AGAIN:
                GameEngine.setState(BattleState.instance);
                break;
            case BUTTON_QUIT:
                GameEngine.setState(null);
                break;
        }
    }
}
