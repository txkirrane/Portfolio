package dev.txkirrane.neumont.enums;

public enum PLAYER_COLOR {
    RED("X"), YELLOW("O"), NULL("-");

    public final String token;

    PLAYER_COLOR(String token) {
        this.token = token;
    }
}
