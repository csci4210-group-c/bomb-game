package com.csci4210.bombgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.csci4210.engine.GameEngine;

public class MainActivity extends Activity
{
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
            GameEngine.setState(TitleState.instance);
            GameEngine.mainLoop();
        }

        initialized = true;
    }
}
