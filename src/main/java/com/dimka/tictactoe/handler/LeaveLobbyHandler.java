package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import com.dimka.tictactoe.state.UserState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component("leaveLobby")
public class LeaveLobbyHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        User user = userStorage.getUser(session.getId());
        Lobby lobby = user.getLobby();
        user.setLobby(null);
        emitter.emmitLobbyLeaveEvent(session);
        if (session.getId().equals(lobby.getHost())) {
            emitter.emmitLobbyDestroyedEvent(lobby.getId());
            emitter.emmitLobbyListChanged(session);
        } else {
            emitter.emmitLobbyMemberListChangedEvent(lobby.getId());
        }
    }
}
