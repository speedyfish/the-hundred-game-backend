package com.brianlimjj.thehundredgame.game.service;

import com.brianlimjj.thehundredgame.game.dto.GameStateResponse;
import com.brianlimjj.thehundredgame.game.dto.GuessRequest;
import com.brianlimjj.thehundredgame.game.dto.JoinGameRequest;
import com.brianlimjj.thehundredgame.game.model.Game;
import com.brianlimjj.thehundredgame.game.model.GameStatus;
import com.brianlimjj.thehundredgame.game.model.Player;
import com.brianlimjj.thehundredgame.game.model.RoundResult;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

  private final Map<String, Game> games = new ConcurrentHashMap<>();
  private final Random random = new Random(); // maybe for future rules, can be removed

  public Game createGame() {
    String code = generateCode();
    Game game = new Game(code);
    games.put(code, game);
    log.info("Created game with code={}", code);
    return game;
  }

  public Game joinGame(String code, JoinGameRequest req) {
    Game game = getGameOrThrow(code);
    if (game.getPlayers().size() >= 2) {
      throw new IllegalStateException("Game is full");
    }
    game.getPlayers().add(new Player(req.playerId(), req.name()));
    if (game.getPlayers().size() == 2) {
      game.setStatus(GameStatus.IN_PROGRESS);
    }
    return game;
  }

  public GameStateResponse makeGuess(String code, GuessRequest req) {
    Game game = getGameOrThrow(code);
    if (game.getStatus() != GameStatus.IN_PROGRESS) {
      throw new IllegalStateException("Game not in progress");
    }

    // store/update this player's guess for the current round
    game.getCurrentGuesses().put(req.playerId(), req.guess());
    log.info(
        "Game {} round {}: player {} guessed {}",
        code,
        game.getRound(),
        req.playerId(),
        req.guess());

    boolean bothGuessed = game.getCurrentGuesses().size() == 2;
    String winnerId = null;

    if (bothGuessed) {
      var players = game.getPlayers();
      if (players.size() != 2) {
        throw new IllegalStateException("Game must have exactly 2 players in progress");
      }
      String p1Id = players.get(0).getId();
      String p2Id = players.get(1).getId();

      List<Integer> g1 = game.getCurrentGuesses().get(p1Id);
      List<Integer> g2 = game.getCurrentGuesses().get(p2Id);

      int p1Points = 0;
      int p2Points = 0;

      for (int i = 0; i < g1.size(); i++) {
        if (g1.get(i) > g2.get(i)) {
          p1Points++;
        } else if (g2.get(i) > g1.get(i)) {
          p2Points++;
        }
      }

      log.info("p1points: {}", p1Points);
      log.info("p2points: {}", p2Points);

      if (p1Points > p2Points) {
        winnerId = p1Id;
      } else if (p2Points > p1Points) {
        winnerId = p2Id;
      }

      game.getHistory().add(new RoundResult(game.getRound(), p1Id, g1, p2Id, g2, winnerId));

      boolean finished = checkAndMaybeFinishMatch(game);

      if (!finished) {
        game.nextRound();
        bothGuessed = false; // new round: nobody has guessed yet
      }
    }

    return new GameStateResponse(
        game.getCode(), game.getStatus(), game.getRound(), bothGuessed, winnerId, game.getCurrentGuesses(), game.getHistory());
  }

  /** Returns true if match is now finished. */
  private boolean checkAndMaybeFinishMatch(Game game) {
    int maxRounds = game.getMaxRounds();
    int currentRound = game.getRound();
    Map<String, Integer> wins = game.getWins();

    // wins map may not contain both players yet, so use 0 as default
    String p1Id = game.getPlayers().get(0).getId();
    String p2Id = game.getPlayers().get(1).getId();
    int p1Wins = wins.getOrDefault(p1Id, 0);
    int p2Wins = wins.getOrDefault(p2Id, 0);

    boolean someoneHasTwo = p1Wins == 2 || p2Wins == 2;
    boolean playedAllRounds = currentRound >= maxRounds;

    if (!someoneHasTwo && !playedAllRounds) {
      return false; // continue
    }

    // Decide overall winner or draw
    String winnerId = null;
    if (p1Wins > p2Wins) {
      winnerId = p1Id;
    } else if (p2Wins > p1Wins) {
      winnerId = p2Id;
    } // else equal -> draw (winnerId stays null)

    game.setWinnerId(winnerId);
    game.setStatus(GameStatus.FINISHED);
    return true;
  }

  public GameStateResponse getState(String code) {
    Game game = getGameOrThrow(code);
    boolean bothGuessed = game.getCurrentGuesses().size() == 2;
    // we don't expose history here for now
    return new GameStateResponse(
        game.getCode(), game.getStatus(), game.getRound(), bothGuessed, null,  game.getCurrentGuesses(), game.getHistory());
  }

  private Game getGameOrThrow(String code) {
    Game game = games.get(code);
    if (game == null) {
      throw new IllegalArgumentException("Game not found: " + code);
    }
    return game;
  }

  private String generateCode() {
    String letters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      sb.append(letters.charAt(random.nextInt(letters.length())));
    }
    return sb.toString();
  }
}
