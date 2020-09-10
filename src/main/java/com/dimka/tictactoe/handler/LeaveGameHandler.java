package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("leaveGame")
public class LeaveGameHandler implements Handler {

    private final WebSocketSessionStorage sessionStorage;
    private final EventEmitter emitter;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        Game game = (Game) sessionStorage.getSessions().get(session.getId()).get("game");
        WebSocketSession enemy = emitter.getEnemy(game.getId(), session.getId());
        sessionStorage.getSessions().get(session.getId()).remove("game");
        sessionStorage.getSessions().get(enemy.getId()).remove("game");
        emitter.emmitLeaveGameEvent(session, enemy);
    }
}
