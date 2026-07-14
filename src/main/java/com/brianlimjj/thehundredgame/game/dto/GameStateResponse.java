package com.brianlimjj.thehundredgame.game.dto;

import com.brianlimjj.thehundredgame.game.model.GameStatus;
import com.brianlimjj.thehundredgame.game.model.RoundResult;

import java.util.List;
import java.util.Map;

public record GameStateResponse(
        String code, GameStatus status, int round, boolean bothGuessedThisRound, String winnerId, Map<String, List<Integer>> currentGuesses, List<RoundResult> history) {}
