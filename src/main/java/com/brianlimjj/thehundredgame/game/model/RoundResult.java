package com.brianlimjj.thehundredgame.game.model;

import java.util.List;

public record RoundResult(
    int round,
    String player1Id,
    List<Integer> player1Guess,
    String player2Id,
    List<Integer> player2Guess,
    String roundWinnerId) {
}
