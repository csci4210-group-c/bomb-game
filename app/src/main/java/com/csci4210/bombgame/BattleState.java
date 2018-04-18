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
        int tile = GameEngine.getTile(0, x, y);

        if (tile == GameResources.TILE_STONE)
            return false;
        if (tile == GameResources.TILE_BRICK)
            GameEngine.setTile(0, x, y, GameResources.TILE_GRASS);
        GameEngine.setTile(1, x, y, GameResources.TILE_FIRE);
        return true;
    }

    private boolean removeBlastTile(int x, int y)
    {
        int tile = GameEngine.getTile(1, x, y);

        if (tile == GameResources.TILE_FIRE)
        {
            GameEngine.setTile(1, x, y, (byte)0);
        }
        return true;
    }

    private void addBlast()
    {
        // get tile bomb is on
        int centerX = sprite.x / GameEngine.TILE_WIDTH;
        int centerY = sprite.y / GameEngine.TILE_HEIGHT;

        blastTile(centerX, centerY);

        // left
        for (int i = 1; i <= radius; i++)
            if (!blastTile(centerX - i, centerY))
                break;

        // right
        for (int i = 1; i <= radius; i++)
            if (!blastTile(centerX + i, centerY))
                break;

        // top
        for (int i = 1; i <= radius; i++)
            if (!blastTile(centerX, centerY - i))
                break;

        // bottom
        for (int i = 1; i <= radius; i++)
            if (!blastTile(centerX, centerY + i))
                break;
    }

    private void removeBlast()
    {
        // get tile bomb is on
        int centerX = sprite.x / GameEngine.TILE_WIDTH;
        int centerY = sprite.y / GameEngine.TILE_HEIGHT;

        removeBlastTile(centerX, centerY);

        // left
        for (int i = 1; i <= radius; i++)
            if (!removeBlastTile(centerX - i, centerY))
                break;

        // right
        for (int i = 1; i <= radius; i++)
            if (!removeBlastTile(centerX + i, centerY))
                break;

        // top
        for (int i = 1; i <= radius; i++)
            if (!removeBlastTile(centerX, centerY - i))
                break;

        // bottom
        for (int i = 1; i <= radius; i++)
            if (!removeBlastTile(centerX, centerY + i))
                break;
    }

    public void update()
    {
        timeRemaining--;

        // do explosion anim when time reaches zero
        if (timeRemaining == 0) {
            addBlast();
            sprite.startAnimSequence(GameResources.bombBlastAnimSeq);
            GameEngine.playSound(GameResources.detonateBomb);
        }

        // destroy sprite once explosion has finished
        if (explosionDone()) {
            removeBlast();
            GameEngine.destroySprite(sprite);
        }
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
    public static final BattleState instance = new BattleState();

    public Bomber bombers[] = new Bomber[10];
    private Player player;

    final int BUTTON_BOMB = 0;

    private final int MAX_BOMBS = 10;
    public Bomb bombs[];

    void addBomb(int x, int y)
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
        bombs = new Bomb[MAX_BOMBS];

        GameEngine.setBackdrop(GameResources.battleBackground);
        GameEngine.createButton(GameEngine.screenWidth - 100, GameEngine.screenHeight - 100,
                200, 200, "BOMB!", BUTTON_BOMB);
        GameEngine.setTileSet(GameResources.tileSet);
        GameEngine.setTileMap(GameResources.level1TileMap);

        player = new Player(50, 50);

        bombers[0] = player;

        for (int i = 1; i < bombers.length; i++)
            bombers[i] = new Enemy(120, 100, this);
    }

    public void update()
    {
        for (Bomber bomber : bombers)
            if (bomber != null)
                bomber.update();

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

        for (int i = 0; i < bombers.length; i++) {
            if (bombers[i] != null && bombers[i].isOnFireTile()) {
                GameEngine.destroySprite(bombers[i].sprite);
                bombers[i] = null;
            }
        }

        int aliveCount = 0;
        for (Bomber bomber : bombers)
            if (bomber != null)
                aliveCount++;

        if (bombers[0] == null)  // player lost
            GameEngine.setState(EndGameState.instance);
        else if (aliveCount <= 1)  // player won
            GameEngine.setState(EndGameState.instance);
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

    /*
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
    */

    public void onTouchDown(int x, int y)
    {
        player.onTouchDown(x, y);
    }

    public void onTouchUp(int x, int y) {
        player.onTouchUp(x, y);
    }
}
