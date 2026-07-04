package com.brianlimjj.thehundredgame.game.web;

import com.brianlimjj.thehundredgame.game.dto.GameStateResponse;
import com.brianlimjj.thehundredgame.game.dto.GuessRequest;
import com.brianlimjj.thehundredgame.game.dto.JoinGameRequest;
import com.brianlimjj.thehundredgame.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

    private final GameService gameService;

    @MessageMapping("/games/{code}/join")
    @SendTo("/topic/games/{code}")
    public GameStateResponse joinGame(
            @DestinationVariable String code,
            JoinGameRequest request
    ) {
        gameService.joinGame(code, request);
        return gameService.getState(code);
    }

    @MessageMapping("/games/{code}/guess")
    @SendTo("/topic/games/{code}")
    public GameStateResponse makeGuess(
            @DestinationVariable String code,
            GuessRequest request
    ) {
        return gameService.makeGuess(code, request);
    }
}
