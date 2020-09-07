package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.dto.JoinLobbyRequest;
import com.dimka.tictactoe.dto.JoinLobbyResponse;
import com.dimka.tictactoe.dto.LobbyMembersResponse;
import com.dimka.tictactoe.event.Event;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.dimka.tictactoe.state.UserState;
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

    private final WebSocketSessionStorage sessionStorage;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        JoinLobbyRequest request = mapper.readValue(message.getPayload(), JoinLobbyRequest.class);

        sessionStorage.getSessions().get(session.getId()).put("state", UserState.IN_LOBBY);
        Lobby lobby = sessionStorage.getLobby(request.getLobbyId());
        sessionStorage.getSessions().get(session.getId()).put("lobby", lobby);

        JoinLobbyResponse response = new JoinLobbyResponse();
        response.setLobbyId(lobby.getId());
        response.setType(Event.JOINED_TO_LOBBY);
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));

        LobbyMembersResponse membersResponse = new LobbyMembersResponse();
        List<LobbyMember> members = sessionStorage.getLobbyMembers(lobby.getId())
                .keySet()
                .stream()
                .map(key -> new LobbyMember(key, key))
                .collect(Collectors.toList());
        membersResponse.setMembers(members);
        membersResponse.setType(Event.LOBBY_MEMBERS_LIST_CHANGED);
        TextMessage textMessage = new TextMessage(mapper.writeValueAsBytes(membersResponse));
        sessionStorage.getLobbyMembers(lobby.getId())
                .values()
                .stream()
                .map(user -> (WebSocketSession) user.get("session"))
                .forEach(s -> {
                    try {
                        s.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.warn("can't send message", e);
                    }
                });
    }
}
