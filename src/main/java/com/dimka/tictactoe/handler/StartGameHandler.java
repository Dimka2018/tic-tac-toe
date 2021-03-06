package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.StartGameRequest;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("startGame")
public class StartGameHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        StartGameRequest request = mapper.readValue(message.getPayload(), StartGameRequest.class);
        Lobby lobby = userStorage.getUser(session.getId()).getLobby();
        if (lobby.getHost().equals(request.getLobbyId())) {
            Game game = new Game();
            game.setHost(session.getId());
            game.setId(session.getId());
            game.setTurn(session.getId());
            game.setTotalGames(lobby.getGames());
            game.setCurrentGameNum(1);
            game.getScore().put(session.getId(), 0);
            User enemy = emitter.getLobbyEnemy(session.getId(), session.getId());
            game.getScore().put(enemy.getId(), 0);
            emitter.emmitGameStartedEvent(lobby.getId(), game);

            emitter.emmitGamesChangedEvent(game.getCurrentGameNum(), game.getTotalGames(), session, enemy.getSession());

            emitter.emmitEnemyTurnEvent(enemy.getSession());
            emitter.emmitMyTurnEvent(session);
        }
    }
}
