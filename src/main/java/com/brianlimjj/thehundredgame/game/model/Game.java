package com.brianlimjj.thehundredgame.game.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {
    private final String code;
    private final int targetNumber;
    private final List<Player> players = new ArrayList<>();

    @Setter
    private GameStatus status = GameStatus.WAITING_FOR_SECOND_PLAYER;

    @Setter
    private String winnerId;

    public Game(String code, int targetNumber) {
        this.code = code;
        this.targetNumber = targetNumber;
    }
}