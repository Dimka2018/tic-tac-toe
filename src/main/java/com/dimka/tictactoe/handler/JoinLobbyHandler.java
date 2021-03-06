package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.JoinLobbyRequest;
import com.dimka.tictactoe.dto.JoinLobbyResponse;
import com.dimka.tictactoe.dto.LobbyMembersResponse;
import com.dimka.tictactoe.event.Event;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component("joinLobby")
public class JoinLobbyHandler implements Handler {

    private final UserStorage userStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        JoinLobbyRequest request = mapper.readValue(message.getPayload(), JoinLobbyRequest.class);

        Lobby lobby = userStorage.getLobby(request.getLobbyId());
        User user = userStorage.getUser(session.getId());
        user.setLobby(lobby);

        emitter.emmitJoinedToLobbyEvent(lobby.getId(), session);

        emitter.emmitLobbyMemberListChangedEvent(lobby.getId());
    }
}
