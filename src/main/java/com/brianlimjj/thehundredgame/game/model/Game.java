package com.brianlimjj.thehundredgame.game.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Game {
    private final String code;
    private final List<Player> players = new ArrayList<>();
    @Setter
    private GameStatus status = GameStatus.WAITING_FOR_SECOND_PLAYER;

    private int round = 1;
    // guesses for the current round: playerId -> guess
    private final Map<String, List<Integer>> currentGuesses = new HashMap<>();

    // optional: remember previous rounds
    private final List<RoundResult> history = new ArrayList<>();

    public Game(String code) {
        this.code = code;
    }

    public void nextRound() {
        round++;
        currentGuesses.clear();
    }
}
