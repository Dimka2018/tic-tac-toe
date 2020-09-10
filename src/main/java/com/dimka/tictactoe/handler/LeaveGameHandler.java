package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("leaveGame")
public class LeaveGameHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        User user = userStorage.getUser(session.getId());
        User enemy = userStorage.getGameEnemy(session.getId());
        user.setGame(null);
        enemy.setGame(null);
        emitter.emmitLeaveGameEvent(session, enemy.getSession());
    }
}
