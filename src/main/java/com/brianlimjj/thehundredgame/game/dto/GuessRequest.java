package com.brianlimjj.thehundredgame.game.dto;

import java.util.List;

public record GuessRequest(String playerId, List<Integer> guess) {}
