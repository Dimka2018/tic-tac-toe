package com.dimka.tictactoe.repository;

import com.dimka.tictactoe.domain.Lobby;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@Component
public class WebSocketSessionStorage {

    private Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();

    public void putSession(WebSocketSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("session", session);
        sessions.put(session.getId(), map);
    }

    public void invalidateSession(WebSocketSession session) {
        sessions.remove(session.getId());
    }

    public List<Lobby> getLobbyList() {
        return sessions
                .values()
                .stream()
                .filter(user -> "hostLobby".equals(user.get("state")))
                .map(user -> (Lobby) user.get("lobby"))
                .collect(Collectors.toList());
    }

}
