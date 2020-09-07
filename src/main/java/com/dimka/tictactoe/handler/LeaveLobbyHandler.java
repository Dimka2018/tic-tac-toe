package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
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

    private final WebSocketSessionStorage sessionStorage;
    private final EventEmitter emitter;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        Map<String, Object> user = sessionStorage.getSessions().get(session.getId());
        Lobby lobby = (Lobby) user.get("lobby");
        user.remove("lobby");
        user.put("state", UserState.SEARCH_LOBBY);
        emitter.emmitLobbyLeaveEvent(session);
        if (session.getId().equals(lobby.getHost())) {
            emitter.emmitLobbyDestroyedEvent(lobby.getId());
            emitter.emmitLobbyListChanged(session);
        } else {
            emitter.emmitLobbyMemberListChangedEvent(lobby.getId());
        }
    }
}
