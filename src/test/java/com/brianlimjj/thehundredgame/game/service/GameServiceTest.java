package com.brianlimjj.thehundredgame.game.service;

import com.brianlimjj.thehundredgame.game.dto.GameStateResponse;
import com.brianlimjj.thehundredgame.game.dto.GuessRequest;
import com.brianlimjj.thehundredgame.game.dto.JoinGameRequest;
import com.brianlimjj.thehundredgame.game.model.Game;
import com.brianlimjj.thehundredgame.game.model.GameStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {

    private final GameService gameService = new GameService();

    @Test
    void createGame_shouldCreateGameWithCodeAndTargetNumberBetween1And3() {
        Game game = gameService.createGame();

        assertThat(game.getCode()).isNotNull().isNotEmpty();
        // you currently use random.nextInt(3) + 1 → 1..3
        assertThat(game.getTargetNumber()).isBetween(1, 3);
        assertThat(game.getStatus()).isEqualTo(GameStatus.WAITING_FOR_SECOND_PLAYER);
        assertThat(game.getPlayers()).isEmpty();
    }

    @Test
    void joinGame_shouldAllowTwoPlayersAndSetInProgress() {
        Game game = gameService.createGame();
        String code = game.getCode();

        JoinGameRequest p1 = new JoinGameRequest("p1", "Alice");
        JoinGameRequest p2 = new JoinGameRequest("p2", "Bob");

        gameService.joinGame(code, p1);
        Game afterSecondJoin = gameService.joinGame(code, p2);

        assertThat(afterSecondJoin.getPlayers()).hasSize(2);
        assertThat(afterSecondJoin.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void joinGame_shouldNotAllowMoreThanTwoPlayers() {
        Game game = gameService.createGame();
        String code = game.getCode();

        gameService.joinGame(code, new JoinGameRequest("p1", "Alice"));
        gameService.joinGame(code, new JoinGameRequest("p2", "Bob"));

        assertThrows(IllegalStateException.class, () ->
                gameService.joinGame(code, new JoinGameRequest("p3", "Charlie")));
    }

    @Test
    void makeGuess_wrongGuess_shouldKeepGameInProgress() {
        Game game = gameService.createGame();
        String code = game.getCode();

        gameService.joinGame(code, new JoinGameRequest("p1", "Alice"));
        gameService.joinGame(code, new JoinGameRequest("p2", "Bob"));

        int target = game.getTargetNumber();
        int wrongGuess = target == 1 ? 2 : 1; // ensure wrong within 1..3

        GameStateResponse state = gameService.makeGuess(
                code,
                new GuessRequest("p1", wrongGuess)
        );

        assertThat(state.status()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(state.winnerId()).isNull();
    }

    @Test
    void makeGuess_correctGuess_shouldFinishGameAndSetWinner() {
        Game game = gameService.createGame();
        String code = game.getCode();

        gameService.joinGame(code, new JoinGameRequest("p1", "Alice"));
        gameService.joinGame(code, new JoinGameRequest("p2", "Bob"));

        int target = game.getTargetNumber();

        GameStateResponse state = gameService.makeGuess(
                code,
                new GuessRequest("p1", target)
        );

        assertThat(state.status()).isEqualTo(GameStatus.FINISHED);
        assertThat(state.winnerId()).isEqualTo("p1");
    }

    @Test
    void getState_unknownGame_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                gameService.getState("UNKNOWN"));
    }

    @Test
    void makeGuess_whenGameNotInProgress_shouldThrow() {
        Game game = gameService.createGame();
        String code = game.getCode();
        // game is still WAITING_FOR_SECOND_PLAYER

        assertThrows(IllegalStateException.class, () ->
                gameService.makeGuess(code, new GuessRequest("p1", 1)));
    }
}
