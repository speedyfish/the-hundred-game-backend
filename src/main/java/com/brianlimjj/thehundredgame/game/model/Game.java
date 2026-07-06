package com.brianlimjj.thehundredgame.game.model;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Game {

  private final String code;
  private final List<Player> players = new ArrayList<>();

  @Setter private GameStatus status = GameStatus.WAITING_FOR_SECOND_PLAYER;

  private int round = 1;
  private final int maxRounds = 3;
  // guesses for the current round: playerId -> guess
  private final Map<String, List<Integer>> currentGuesses = new HashMap<>();

  // total round wins per player
  private final Map<String, Integer> wins = new HashMap<>();

  // optional: remember previous rounds
  private final List<RoundResult> history = new ArrayList<>();

  @Setter private String winnerId;

  public Game(String code) {
    this.code = code;
  }

  public void nextRound() {
    round++;
    currentGuesses.clear();
  }
}
