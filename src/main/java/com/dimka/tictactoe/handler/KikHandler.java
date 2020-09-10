package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.KickRequest;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import com.dimka.tictactoe.state.UserState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("kik")
public class KikHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        KickRequest request = mapper.readValue(message.getPayload(), KickRequest.class);
        User kickedUser = userStorage.getUser(request.getId());
        Lobby lobby = kickedUser.getLobby();
        if (session.getId().equals(lobby.getHost()) && !lobby.getHost().equals(kickedUser.getId())) {
            kickedUser.setLobby(null);
            emitter.emmitKikEvent(request.getId());
            emitter.emmitLobbyMemberListChangedEvent(lobby.getId());
        }

    }
}
