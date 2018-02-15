package com.csci4210.bombgame;

import android.app.Activity;
import android.os.Bundle;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.State;

public class MainActivity extends Activity
{
    private State testState = new TestState();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        GameResources.load(this);

        GameEngine.init(this);
        GameEngine.setState(testState);
        GameEngine.mainLoop();
    }
}
