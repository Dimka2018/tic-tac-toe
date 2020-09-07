package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.dto.CreateLobbyRequest;
import com.dimka.tictactoe.dto.CreateLobbyResponse;
import com.dimka.tictactoe.dto.LobbyListResponse;
import com.dimka.tictactoe.event.Event;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.dimka.tictactoe.state.UserState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component("createLobby")
public class CreateLobbyHandler implements Handler {

    private final WebSocketSessionStorage sessions;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        CreateLobbyRequest request = mapper.readValue(message.getPayload(), CreateLobbyRequest.class);

        sessions.getSessions().get(session.getId()).put("state", UserState.HOST_LOBBY);
        Lobby lobby = new Lobby();
        lobby.setHost(session.getId());
        lobby.setId(session.getId());
        lobby.setGames(request.getGames());
        sessions.getSessions().get(session.getId()).put("lobby", lobby);

        CreateLobbyResponse response = new CreateLobbyResponse();
        response.setType(Event.LOBBY_CREATED);
        response.setHost(session.getId());
        response.setId(session.getId());
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));

        emitter.emmitLobbyMemberListChangedEvent(lobby.getId());

        emitter.emmitLobbyListChanged(session);
    }
}
