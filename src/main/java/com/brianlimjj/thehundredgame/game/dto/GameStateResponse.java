package com.brianlimjj.thehundredgame.game.dto;

import com.brianlimjj.thehundredgame.game.model.GameStatus;

public record GameStateResponse(String code,
                                GameStatus status,
                                String winnerId) {

}
