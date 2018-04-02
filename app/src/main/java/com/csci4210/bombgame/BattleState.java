package com.csci4210.bombgame;

import android.util.Log;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;

import java.util.ArrayList;

class Bomb
{
    private Sprite sprite;
    private int timeRemaining;

    public Bomb(int x, int y)
    {
        sprite = GameEngine.createSprite(GameResources.blastSequence, GameResources.bombFlashAnimSeq,
                x, y, 32, 32);
        timeRemaining = 5 * 60;
    }

    public void update()
    {
        timeRemaining--;

        // do explosion anim when time reaches zero
        if (timeRemaining == 0) {
            sprite.startAnimSequence(GameResources.bombBlastAnimSeq);
            GameEngine.playSound(GameResources.detonateBomb);
        }

        // destroy sprite once explosion has finished
        if (explosionDone())
            GameEngine.destroySprite(sprite);
    }

    public boolean explosionDone()
    {
        return sprite.animEnded;
    }

    public Sprite getSprite(){
        return this.sprite;
    }
}

public class BattleState extends State
{
    public Bomber player;
    public Bomber enemy;

    private PlayerController playerController;
    private EnemyController enemyController;

    final int BUTTON_BOMB = 0;

    private final int MAX_BOMBS = 10;
    public Bomb bombs[] = new Bomb[MAX_BOMBS];

    private void addBomb(int x, int y)
    {
        for (int i = 0; i < bombs.length; i++)
        {
            if (bombs[i] == null) {
                bombs[i] = new Bomb(x, y);
                return;
            }
        }
    }

    private void removeBomb(Bomb bomb)
    {
        for (int i = 0; i < bombs.length; i++)
        {
            if (bombs[i] == bomb)
                bombs[i] = null;
        }
    }

    public void enter()
    {
        GameEngine.setBackdrop(GameResources.battleBackground);
        GameEngine.createButton(GameEngine.screenWidth - 100, GameEngine.screenHeight - 100,
                200, 200, "BOMB!", BUTTON_BOMB);
        GameEngine.setTileSet(GameResources.tileSet);
        GameEngine.setTileMap(GameResources.level1TileMap);

        player = new Bomber(50, 50);
        enemy = new Bomber(120, 100);

        playerController = new PlayerController(player);
        enemyController = new EnemyController(enemy, this);
    }

    public void update()
    {
        // Update enemy
        enemyController.update();

        // Update player
        playerController.update();

        // Update bombs
        for (Bomb bomb : bombs)
        {
            if (bomb != null)
            {
                bomb.update();
                if (bomb.explosionDone())
                    removeBomb(bomb);
            }
        }

        // center screen on player
        GameEngine.setCenterCoord(player.x, player.y);
    }

    public void exit()
    {
        GameEngine.setBackdrop(null);
        GameEngine.destroyAllButtons();
    }

    public void onButton(int buttonId)
    {
        switch (buttonId)
        {
            case BUTTON_BOMB: {
                // get center of player's tile
                int bombX = player.x / GameEngine.TILE_WIDTH * GameEngine.TILE_WIDTH + GameEngine.TILE_WIDTH / 2;
                int bombY = player.y / GameEngine.TILE_HEIGHT * GameEngine.TILE_HEIGHT + GameEngine.TILE_HEIGHT / 2;
                addBomb(bombX, bombY);
                GameEngine.playSound(GameResources.placeBomb);
            }
            break;
        }
    }

    public void onTouchDown(int x, int y)
    {
        playerController.onTouchDown(x, y);
    }

    public void onTouchUp(int x, int y) {
        playerController.onTouchUp(x, y);
    }
}
