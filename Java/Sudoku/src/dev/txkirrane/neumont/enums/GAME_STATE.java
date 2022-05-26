package dev.txkirrane.neumont.enums;

public enum GAME_STATE {
    PENDING(null), WIN_RED(PLAYER_COLOR.RED), WIN_YELLOW(PLAYER_COLOR.YELLOW), DRAW(null);

    public final PLAYER_COLOR color;

    GAME_STATE(PLAYER_COLOR color) {
        this.color = color;
    }
}
