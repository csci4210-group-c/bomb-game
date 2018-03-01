package com.csci4210.bombgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class MainActivity extends Activity
{
    static State title = new TitleState();
    static State testState = new TestState();
    static State battleState = new BattleState();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
            return;
        super.onCreate(savedInstanceState);
        Log.d("test", "Hello World");

        GameResources.load(this);

        GameEngine.init(this);
        GameEngine.setState(title);
        GameEngine.mainLoop();
    }
}
