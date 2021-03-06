package com.csci4210.bombgame;


import com.csci4210.engine.GameEngine;
import com.csci4210.engine.Sprite;
import com.csci4210.engine.State;
import com.csci4210.engine.TextLabel;

class Clock
{
    int ticksLeft;
    TextLabel label;

    Clock(int seconds)
    {
        ticksLeft = seconds * GameEngine.TICKS_PER_SECOND;
        label = GameEngine.createTextLabel(GameEngine.screenWidth / 2, 100, "");
    }

    void update()
    {
        ticksLeft--;
        if (ticksLeft < 0)
            ticksLeft = 0;

        int seconds = (ticksLeft / GameEngine.TICKS_PER_SECOND) % 60;
        int minutes = (ticksLeft / GameEngine.TICKS_PER_SECOND) / 60;

        label.setText(String.format("TIME LEFT %d:%02d", minutes, seconds));
    }
}

class Bomb
{
    private Sprite sprite;
    private int timeRemaining;
    private int radius = 3;

    public Bomb(int x, int y)
    {
        sprite = GameEngine.createSprite(GameResources.blastSequence, GameResources.bombFlashAnimSeq,
                x, y, 32, 32);
        timeRemaining = 275;
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

    public Bomber bombers[] = new Bomber[5];
    private Player player;

    final int BUTTON_BOMB = 0;

    private final int MAX_BOMBS = 10;
    public Bomb bombs[];
    private Clock clock;

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
        GameEngine.createButton(GameEngine.screenWidth - 70, GameEngine.screenHeight - 70,
                140, 140, "BOMB!", BUTTON_BOMB);
        GameEngine.setTileSet(GameResources.tileSet);
        GameEngine.setTileMap(GameResources.level1TileMap);

        player = new Player(50, 50);

        bombers[0] = player;

        for (int i = 1; i < bombers.length; i++)
            bombers[i] = new Enemy(16 + 96*i, 16 + 64*i, this);

        clock = new Clock(120);
    }

    public void update()
    {
        clock.update();

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

        if (clock.ticksLeft < 300)
            GameEngine.playSound(GameResources.timeWarning);

        if (clock.ticksLeft == 0 || bombers[0] == null)  // player lost
        {
            EndGameState.instance.setResult(false);
            GameEngine.setState(EndGameState.instance);
        }
        else if (aliveCount <= 1)  // player won
        {
            EndGameState.instance.setResult(true);
            GameEngine.setState(EndGameState.instance);
        }
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
        player.onTouchDown(x, y);
    }

    public void onTouchUp(int x, int y) {
        player.onTouchUp(x, y);
    }
}
