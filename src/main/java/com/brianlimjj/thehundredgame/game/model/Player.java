package com.brianlimjj.thehundredgame.game.model;

import lombok.Getter;

@Getter
public class Player {

  private final String id;
  private final String name;

  public Player(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
