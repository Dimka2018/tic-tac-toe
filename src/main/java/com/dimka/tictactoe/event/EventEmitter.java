package com.dimka.tictactoe.event;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.*;
import com.dimka.tictactoe.repository.UserStorage;
import com.dimka.tictactoe.state.UserState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Component
public class EventEmitter {

    private final UserStorage userStorage;
    private final ObjectMapper mapper;

    public void emmitLobbyMemberListChangedEvent(String lobbyId) throws Exception {
        LobbyMembersResponse membersResponse = new LobbyMembersResponse();
        Set<User> members = userStorage.getLobbyMembers(lobbyId);
        Set<LobbyMember> lobbyMembers = members.stream()
                .map(user -> new LobbyMember(user.getId(), user.getName(), user.getId().equals(user.getLobby().getHost())))
                .collect(Collectors.toSet());
        membersResponse.setMembers(lobbyMembers);
        membersResponse.setType(Event.LOBBY_MEMBERS_LIST_CHANGED);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(membersResponse));
        members.forEach(user -> sendMessage(message, user.getSession()));
    }

    public void emmitLobbyLeaveEvent(WebSocketSession session) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.LEAVE_LOBBY);
        session.sendMessage(new TextMessage(mapper.writeValueAsBytes(response)));
    }

    public void emmitLobbyDestroyedEvent(String lobbyId) throws JsonProcessingException {
        MessageResponse response = new MessageResponse();
        response.setType(Event.LOBBY_DESTROYED);
        Set<User> members = userStorage.getLobbyMembers(lobbyId);
        members.forEach(user -> user.setLobby(null));
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        members.forEach(user -> sendMessage(message, user.getSession()));
    }

    public void emmitLobbyListChanged(WebSocketSession session) throws Exception {
        LobbyListResponse lobbyListResponse = new LobbyListResponse();
        lobbyListResponse.setType(Event.LOBBY_LIST_CHANGED);
        lobbyListResponse.setLobbies(userStorage.getLobbyList());
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(lobbyListResponse));
        userStorage.getAllUsers()
                .stream()
                .filter(user -> user.getSession().isOpen())
                .filter(user -> !user.getId().equals(session.getId()))
                .filter(user -> user.getGame() == null && user.getLobby() == null)
                .forEach(user -> sendMessage(message, user.getSession()));
    }

    public void emmitKikEvent(String userId) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.KICK);
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), userStorage.getUser(userId).getSession());
    }

    public void emmitGameStartedEvent(String lobbyId, Game game) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.GAME_STARTED);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        Set<User> members = userStorage.getLobbyMembers(lobbyId);
        members.forEach(user -> user.setLobby(null));
        members.forEach(user -> user.setGame(game));
        members.forEach(user -> sendMessage(message, user.getSession()));
    }

    public void emmitBoardStateChangedEvent(int[][] cells, WebSocketSession session) throws Exception {
        ApplyBoardStateResponse response = new ApplyBoardStateResponse();
        response.setCells(cells);
        response.setType(Event.BOARD_CHANGED);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        User user = userStorage.getUser(session.getId());
        Game game = user.getGame();
        userStorage.getAllUsers()
                .stream()
                .filter(usr -> !usr.getId().equals(user.getId()))
                .filter(usr -> usr.getGame() != null)
                .filter(usr -> usr.getGame().getId().equals(game.getId()))
                .forEach(usr -> sendMessage(message, usr.getSession()));
    }

    public void emmitEnemyTurnEvent(WebSocketSession session) throws Exception {
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(new MessageResponse(Event.ENEMY_TURN_STARTED)));
        sendMessage(message, session);
    }

    public void emmitMyTurnEvent(WebSocketSession session) throws Exception {
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(new MessageResponse(Event.YOUR_TURN_STARTED)));
        sendMessage(message, session);
    }

    public User getLobbyEnemy(String lobbyId, String userId) {
        return userStorage.getAllUsers()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .filter(user -> user.getLobby() != null)
                .filter(user -> user.getLobby().getId().equals(lobbyId))
                .findFirst()
                .orElseThrow();
    }

    public void emmitLobbyCreatedEvent(WebSocketSession session) throws Exception {
        CreateLobbyResponse response = new CreateLobbyResponse();
        response.setType(Event.LOBBY_CREATED);
        response.setHost(session.getId());
        response.setId(session.getId());
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), session);
    }

    public void emmitJoinedToLobbyEvent(String lobbyId, WebSocketSession session) throws Exception {
        JoinLobbyResponse response = new JoinLobbyResponse();
        response.setLobbyId(lobbyId);
        response.setType(Event.JOINED_TO_LOBBY);
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), session);
    }

    public void emmitGamesChangedEvent(int currentGame, int totalGames, WebSocketSession... sessions) throws Exception {
        GameChangedResponse response = new GameChangedResponse();
        response.setType(Event.GAME_CHANGED);
        response.setCurrentGame(currentGame);
        response.setTotalGames(totalGames);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        Arrays.stream(sessions).forEach(session -> sendMessage(message, session));
    }

    public void emmitScoreChangedEvent(int you, int enemy, WebSocketSession session) throws Exception {
        ScoreResponse response = new ScoreResponse();
        response.setType(Event.SCORE_CHANGED);
        response.setYou(you);
        response.setEnemy(enemy);
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), session);
    }

    public void emmitYouWinEvent(WebSocketSession session) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.YOU_WIN);
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), session);
    }

    public void emmitYouLoseEvent(WebSocketSession session) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.YOU_LOSE);
        sendMessage(new TextMessage(mapper.writeValueAsBytes(response)), session);
    }

    public void emmitDrawEvent(WebSocketSession... sessions) throws Exception {
        MessageResponse response = new MessageResponse();
        response.setType(Event.DRAW);
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(response));
        Arrays.stream(sessions).forEach(session -> sendMessage(message, session));
    }

    public void emmitLeaveGameEvent(WebSocketSession... sessions) throws Exception {
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(new MessageResponse(Event.LEAVE_GAME)));
        Arrays.stream(sessions).forEach(session -> sendMessage(message, session));
    }

    public void emmitUsernameChangedEvent(WebSocketSession session) throws Exception {
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(new MessageResponse(Event.USERNAME_CHANGED)));
        sendMessage(message, session);
    }

    private void sendMessage(AbstractWebSocketMessage message, WebSocketSession session) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.warn("can't send message", e);
        }
    }
}
