package com.dimka.tictactoe.event;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.dto.*;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.dimka.tictactoe.state.UserState;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@AllArgsConstructor
@Slf4j
@Component
public class EventEmitter {

    private final WebSocketSessionStorage sessionStorage;
    private final ObjectMapper mapper;

    public void emmitLobbyMemberListChangedEvent(String lobbyId) throws Exception {
        LobbyMembersResponse membersResponse = new LobbyMembersResponse();
        List<LobbyMember> members = sessionStorage.getLobbyMembers(lobbyId)
                .keySet()
                .stream()
                .map(key -> new LobbyMember(key, key))
                .collect(Collectors.toList());
        membersResponse.setMembers(members);
        membersResponse.setType(Event.LOBBY_MEMBERS_LIST_CHANGED);
        TextMessage textMessage = new TextMessage(mapper.writeValueAsBytes(membersResponse));
        sessionStorage.getLobbyMembers(lobbyId)
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

    public void emmitLobbyLeaveEvent(WebSocketSession session) throws Exception {
        LeaveLobbyResponse response = new LeaveLobbyResponse();
        response.setType(Event.LEAVE_LOBBY);
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));
    }

    public void emmitLobbyDestroyedEvent(String lobbyId) throws JsonProcessingException {
        MessageResponse response = new MessageResponse();
        response.setType(Event.LOBBY_DESTROYED);
        List<String> ids = sessionStorage.getSessions().entrySet()
                .stream()
                .filter(entry -> UserState.IN_LOBBY.equals(entry.getValue().get("state")))
                .filter(entry -> ((Lobby) entry.getValue().get("lobby")).getId().equals(lobbyId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        ids.stream()
                .peek(id -> sessionStorage.getSessions().get(id).remove("lobby"))
        .peek(id -> sessionStorage.getSessions().get(id).put("state", UserState.SEARCH_LOBBY))
        .map(id -> (WebSocketSession) sessionStorage.getSessions().get(id).get("session"))
        .forEach(session -> {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.warn("can't send message", e);
            }
        });
    }

    public void emmitLobbyListChanged(WebSocketSession session) throws Exception {
        LobbyListResponse lobbyListResponse = new LobbyListResponse();
        lobbyListResponse.setType(Event.LOBBY_LIST_CHANGED);
        lobbyListResponse.setLobbies(sessionStorage.getLobbyList());
        TextMessage textMessage = new TextMessage(mapper.writeValueAsBytes(lobbyListResponse));
        sessionStorage.getSessions()
                .values()
                .stream()
                .filter(user -> ((WebSocketSession)user.get("session")).isOpen())
                .filter(user -> !session.getId().equals(((WebSocketSession)user.get("session")).getId()))
                .map(user -> (WebSocketSession)user.get("session"))
                .forEach(s -> {
                    try {
                        s.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.warn("cant send message", e);
                    }
                });
    }

    public void emmitKikEvent(String userId) throws Exception {
        KickResponse response = new KickResponse();
        response.setType(Event.KICK);
        ((WebSocketSession) sessionStorage.getSessions().get(userId).get("session")).sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));
    }

    public void emmitGameStartedEvent(String lobbyId, Game game) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.GAME_STARTED);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        sessionStorage.getSessions()
                .values()
                .stream()
                .filter(user -> user.containsKey("lobby"))
                .filter(user -> ((Lobby) user.get("lobby")).getId().equals(lobbyId))
                .peek(user -> user.remove("lobby"))
                .peek(user -> user.put("state", UserState.IN_GAME))
                .peek(user -> user.put("game", game))
                .map(user -> (WebSocketSession) user.get("session"))
                .forEach(session -> {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        log.warn("can't send message");
                    }
                });

    }
}