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

    private static boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (initialized)
            return;

        super.onCreate(savedInstanceState);
        Log.d("test", "Hello World");

        if (savedInstanceState == null) {
            GameResources.load(this);

            GameEngine.init(this);
            GameEngine.setState(title);
            GameEngine.mainLoop();
        }

        initialized = true;
    }
}
