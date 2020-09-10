package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@AllArgsConstructor
@Component("logout")
public class LogoutHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        User user = userStorage.getUser(session.getId());
        if (user.getLobby() != null) {
            Lobby lobby = user.getLobby();
            if (lobby.getHost().equals(user.getId())) {
                emitter.emmitLobbyDestroyedEvent(lobby.getId());
            } else {
                user.setLobby(null);
                emitter.emmitLobbyMemberListChangedEvent(lobby.getId());
            }
        }
        if (user.getGame() != null) {
            User enemy = userStorage.getGameEnemy(session.getId());
            user.setGame(null);
            enemy.setGame(null);
            emitter.emmitLeaveGameEvent(session, enemy.getSession());
        }
        userStorage.delete(session.getId());
    }
}
