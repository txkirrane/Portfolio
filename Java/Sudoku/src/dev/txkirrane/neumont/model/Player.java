package dev.txkirrane.neumont.model;

import dev.txkirrane.neumont.enums.PLAYER_COLOR;

public abstract class Player {

    private String playerName;
    private PLAYER_COLOR playerColor;

    public void initPlayer(String name, PLAYER_COLOR pColor){
        this.playerName = name;
        this.playerColor = pColor;
    }

    public String getName(){
        return this.playerName;
    }

    public PLAYER_COLOR getColor(){
        return this.playerColor;
    }
}
