package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Component("login")
public class LoginHandler implements Handler {

    private final WebSocketSessionStorage sessionStorage;

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) {
        sessionStorage.putSession(session);
    }
}
