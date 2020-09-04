package com.dimka.tictactoe.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@NoArgsConstructor
@Data
@Component
public class WebSocketSessionStorage {

    private Map<String, Map<String, Object>> sessions = new ConcurrentHashMap<>();

    public void putSession(WebSocketSession session) {
        sessions.put(session.getId(), Collections.singletonMap("session", session));
    }

    public void invalidateSession(WebSocketSession session) {
        sessions.remove(session.getId());
    }

}
