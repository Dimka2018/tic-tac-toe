package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.LobbyMembersResponse;
import com.dimka.tictactoe.repository.UserStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component("requestLobbyMembers")
public class RequestLobbyMembersHandler implements Handler {

    private final UserStorage userStorage;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        User user = userStorage.getUser(session.getId());
        Lobby lobby = user.getLobby();
        Set<User> lobbyMembers = userStorage.getLobbyMembers(lobby.getId());
        Set<LobbyMember> members = lobbyMembers
                .stream()
                .map(usr -> new LobbyMember(usr.getId(), usr.getId(), usr.getId().equals(lobby.getHost())))
                .collect(Collectors.toSet());
        LobbyMembersResponse response = new LobbyMembersResponse();
        response.setMembers(members);

        TextMessage textMessage = new TextMessage(mapper.writeValueAsBytes(response));

        lobbyMembers.stream()
                .map(User::getSession)
                .forEach(s -> {
                    try {
                        s.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.warn("send message error", e);
                    }
                });
    }
}
