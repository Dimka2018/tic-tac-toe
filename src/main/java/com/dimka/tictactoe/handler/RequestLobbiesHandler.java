package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.dto.LobbyListResponse;
import com.dimka.tictactoe.event.Event;
import com.dimka.tictactoe.repository.UserStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component("requestLobbies")
public class RequestLobbiesHandler implements Handler {

    private final UserStorage userStorage;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        Set<Lobby> lobbies = userStorage.getLobbyList();
        LobbyListResponse response = new LobbyListResponse();
        response.setLobbies(lobbies);
        response.setType(Event.LOBBY_LIST_CHANGED);
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));
    }
}
