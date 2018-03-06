package com.csci4210.engine;

/**
 * Created by cameron on 2/13/18.
 */

public abstract class State
{
    // called upon entering the state
    public void enter()
    {
    }

    // called upon exiting the state
    public void exit()
    {
    }

    // called once every game tick
    public void update()
    {
    }

    //called once screen is touched
    public void onTouchDown(int x, int y)
    {
    }

    //called once touch ceases
    public  void onTouchUp(int x, int y)
    {
    }

    public void onButton(int buttonId)
    {
    }

    public void onBackPressed()
    {
    }
}
