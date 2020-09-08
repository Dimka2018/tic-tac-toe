package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.dto.StartGameRequest;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("startGame")
public class StartGameHandler implements Handler {

    private final WebSocketSessionStorage sessionStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        StartGameRequest request = mapper.readValue(message.getPayload(), StartGameRequest.class);
        Lobby lobby = (Lobby) sessionStorage.getSessions().get(session.getId()).get("lobby");
        if (lobby.getHost().equals(request.getLobbyId())) {
            Game game = new Game();
            game.setHost(session.getId());
            game.setId(session.getId());
            game.setTurn(session.getId());
            game.setTotalGames(lobby.getGames());
            game.setCurrentGameNum(1);
            emitter.emmitGameStartedEvent(lobby.getId(), game);

            WebSocketSession enemy = emitter.getEnemy(game.getId(), session.getId());
            emitter.emmitEnemyTurnEvent(enemy);
            emitter.emmitMyTurnEvent(session);
        }
    }
}
