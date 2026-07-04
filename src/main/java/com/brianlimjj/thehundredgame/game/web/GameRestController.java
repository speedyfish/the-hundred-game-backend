package com.brianlimjj.thehundredgame.game.web;

import com.brianlimjj.thehundredgame.game.dto.CreateGameResponse;
import com.brianlimjj.thehundredgame.game.dto.GameStateResponse;
import com.brianlimjj.thehundredgame.game.dto.GuessRequest;
import com.brianlimjj.thehundredgame.game.dto.JoinGameRequest;
import com.brianlimjj.thehundredgame.game.model.Game;
import com.brianlimjj.thehundredgame.game.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameRestController {
    private final GameService gameService;

    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public CreateGameResponse createGame() {
        Game game = gameService.createGame();
        return new CreateGameResponse(game.getCode());
    }

    @PostMapping("/{code}/join")
    public GameStateResponse join(
            @PathVariable String code,
            @RequestBody JoinGameRequest request
    ) {
        gameService.joinGame(code, request);
        return gameService.getState(code);
    }
    @PostMapping("/{code}/guess")
    public GameStateResponse guess(
            @PathVariable String code,
            @RequestBody GuessRequest request
    ) {
        return gameService.makeGuess(code, request);
    }

    @GetMapping("/{code}")
    public GameStateResponse getState(@PathVariable String code) {
        return gameService.getState(code);
    }
}
