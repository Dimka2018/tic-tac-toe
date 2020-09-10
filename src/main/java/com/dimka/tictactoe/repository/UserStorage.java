package com.dimka.tictactoe.repository;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.Lobby;
import com.dimka.tictactoe.domain.LobbyMember;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.state.UserState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class UserStorage {

    private Map<String, User> users = new ConcurrentHashMap<>();

    public void delete(String id) {
        users.remove(id);
    }

    public Set<Lobby> getLobbyList() {
        return users.values()
                .stream()
                .map(User::getLobby)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<User> getLobbyMembers(String lobbyId) {
        return users.values()
                .stream()
                .filter(user -> user.getLobby() != null)
                .filter(user -> user.getLobby().getId().equals(lobbyId))
                .collect(Collectors.toSet());
    }

    public Lobby getLobby(String lobbyId) {
        return users.values()
                .stream()
                .map(User::getLobby)
                .filter(Objects::nonNull)
                .filter(lobby -> lobby.getId().equals(lobbyId))
                .findFirst()
                .orElseThrow();
    }

    public void save(User user) {
        this.users.put(user.getId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public User getGameEnemy(String userId) {
        Game game = users.get(userId).getGame();
        return users.values()
                .stream()
                .filter(user -> user.getGame() != null)
                .filter(user -> !user.getId().equals(userId))
                .filter(user -> user.getGame().getId().equals(game.getId()))
                .findFirst()
                .orElseThrow();
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

}
