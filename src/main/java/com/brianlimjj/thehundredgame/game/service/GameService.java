package com.brianlimjj.thehundredgame.game.service;

import com.brianlimjj.thehundredgame.game.dto.GameStateResponse;
import com.brianlimjj.thehundredgame.game.dto.GuessRequest;
import com.brianlimjj.thehundredgame.game.dto.JoinGameRequest;
import com.brianlimjj.thehundredgame.game.model.Game;
import com.brianlimjj.thehundredgame.game.model.GameStatus;
import com.brianlimjj.thehundredgame.game.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;



@Slf4j
@Service
public class GameService {
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public Game createGame() {
        String code = generateCode();
        int target = random.nextInt(3) + 1;
        Game game = new Game(code, target);
        games.put(code, game);
        log.info("Created game with code={} and targetNumber={}", code, target);
        return game;
    }

    public Game joinGame(String code, JoinGameRequest req) {
        Game game = getGameOrThrow(code);
        if (game.getPlayers().size() >= 2) {
            throw new IllegalStateException("Game is full");
        }
        game.getPlayers().add(new Player(req.playerId(), req.name()));
        if (game.getPlayers().size() == 2){
            game.setStatus(GameStatus.IN_PROGRESS);
        }
        return game;

    }

    public GameStateResponse makeGuess(String code, GuessRequest req) {
        Game game = getGameOrThrow(code);
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game not in progress");
        }
        if (req.guess() == game.getTargetNumber()) {
            game.setStatus(GameStatus.FINISHED);
            game.setWinnerId(req.playerId());
        }
        return new GameStateResponse(game.getCode(), game.getStatus(), game.getWinnerId());
    }

    public GameStateResponse getState(String code) {
        Game game = getGameOrThrow(code);
        return new GameStateResponse(game.getCode(), game.getStatus(), game.getWinnerId());
    }

    private Game getGameOrThrow(String code) {
        Game game = games.get(code);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + code);
        }
        return game;
    }

    private String generateCode() {
        // simple 6‑char code, improve later
        String letters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }
}


