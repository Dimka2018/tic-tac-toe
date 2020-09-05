package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.dto.CreateLobbyResponse;
import com.dimka.tictactoe.dto.LobbyListResponse;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("createLobby")
public class CreateLobbyHandler implements Handler {

    private final WebSocketSessionStorage sessions;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        sessions.getSessions().get(session.getId()).put("state", "inLobby");
        Lobby lobby = new Lobby();
        lobby.setHost(session.getId());
        lobby.setId(session.getId());
        sessions.getSessions().get(session.getId()).put("lobby", lobby);

        CreateLobbyResponse response = new CreateLobbyResponse();
        response.setType(Event.LOBBY_CREATED);
        response.setHost(session.getId());
        response.setId(session.getId());
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));


        LobbyListResponse lobbyListResponse = new LobbyListResponse();
        lobbyListResponse.setType(Event.LOBBY_LIST_CHANGED);
        lobbyListResponse.setLobbies(sessions.getLobbyList());
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));
    }
}
