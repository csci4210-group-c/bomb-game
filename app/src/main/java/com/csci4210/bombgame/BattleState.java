package com.csci4210.bombgame;

import android.util.Log;
import android.widget.Switch;

import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;

import java.util.ArrayList;

class Bomb
{
    private Sprite sprite;
    private int timeRemaining;
    private int radius = 3;

    public Bomb(int x, int y)
    {
        sprite = GameEngine.createSprite(GameResources.blastSequence, GameResources.bombFlashAnimSeq,
                x, y, 32, 32);
        timeRemaining = 5 * 60;
    }

    private boolean blastTile(int x, int y)
    {
        int tile = GameEngine.getTileAtCoord(0, x, y);

        if (tile == GameResources.TILE_STONE)
            return false;
        if (tile == GameResources.TILE_BRICK)
            GameEngine.setTile(0, x, y, GameResources.TILE_GRASS);
        GameEngine.setTile(1, x, y, GameResources.TILE_FIRE);
        return true;
    }

    private void addBlast()
    {
        // get tile bomb is on
        int centerX = sprite.x / GameEngine.TILE_WIDTH;
        int centerY = sprite.y / GameEngine.TILE_HEIGHT;

        // left
        for (int i = 1; i < radius; i++)
            if (blastTile(centerX - i, centerY))
                break;

        // right
        for (int i = 1; i < radius; i++)
            if (blastTile(centerX + i, centerY))
                break;

        // top
        for (int i = 1; i < radius; i++)
            if (blastTile(centerX, centerY - i))
                break;

        // bottom
        for (int i = 1; i < radius; i++)
            if (blastTile(centerX, centerY + i))
                break;

    }

    private void removeBlast()
    {
        // get tile bomb is on
        int centerX = sprite.x / GameEngine.TILE_WIDTH;
        int centerY = sprite.y / GameEngine.TILE_HEIGHT;

        for (int i = 1; i < radius; i++)
        {
            GameEngine.setTile(1, centerX - i, centerY, GameResources.TILE_STONE);
            GameEngine.setTile(1, centerX + i, centerY, GameResources.TILE_STONE);
            GameEngine.setTile(1, centerX, centerY - i, GameResources.TILE_STONE);
            GameEngine.setTile(1, centerX, centerY + i, GameResources.TILE_STONE);
        }
    }

    public void update()
    {
        timeRemaining--;

        // do explosion anim when time reaches zero
        if (timeRemaining == 0) {
            sprite.startAnimSequence(GameResources.bombBlastAnimSeq);
            GameEngine.playSound(GameResources.detonateBomb);
            addBlast();
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

    public  void explode(Bomb bomb){
        int tileX = bomb.getSprite().x/GameEngine.TILE_WIDTH;
        int tileY = bomb.getSprite().y/GameEngine.TILE_HEIGHT;
        for(int i=-1; i<2;i++){
            int j=0;
            switch (j){
                case -1:{
                    if(GameResources.level1TileMap[tileX+i][tileY+j]== GameResources.TILE_BRICK) {
                        GameResources.level1TileMap[tileX + i][tileY + j] = GameResources.TILE_GRASS;
                    }
                }
                case 0:{
                    if(GameResources.level1TileMap[tileX+i][tileY+j]== GameResources.TILE_BRICK) {
                        GameResources.level1TileMap[tileX + i][tileY + j] = GameResources.TILE_GRASS;
                    }
                }
                case 1:{
                    if(GameResources.level1TileMap[tileX+i][tileY+j]== GameResources.TILE_BRICK) {
                        GameResources.level1TileMap[tileX + i][tileY + j] = GameResources.TILE_GRASS;
                    }

                }


            }
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
