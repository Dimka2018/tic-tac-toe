package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.dto.LobbyMembersResponse;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Component("requestLobbyMembers")
public class RequestLobbyMembersHandler implements Handler {

    private final WebSocketSessionStorage sessionStorage;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        Lobby lobby = (Lobby) sessionStorage.getSessions().get(session.getId()).get("lobby");
        Map<String, Map<String, Object>> lobbyMembers = sessionStorage.getLobbyMembers(lobby.getId());
        List<LobbyMember> members = lobbyMembers.keySet().stream().map(LobbyMember::new).collect(Collectors.toList());
        LobbyMembersResponse response = new LobbyMembersResponse();
        response.setMembers(members);

        TextMessage textMessage = new TextMessage(mapper.writeValueAsBytes(response));

        lobbyMembers.values()
                .stream()
                .map(user -> (WebSocketSession) user.get("session"))
                .forEach(s -> {
                    try {
                        s.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.warn("send message error", e);
                    }
                });
    }
}
