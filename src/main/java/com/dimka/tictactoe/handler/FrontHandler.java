package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.dto.MessageRequest;
import com.dimka.tictactoe.dto.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.util.Map;

@AllArgsConstructor
@Slf4j
@Component
public class FrontHandler extends BinaryWebSocketHandler {

    private final Map<String, Handler> handlers;
    private final ObjectMapper mapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("new Connection");
        handlers.get("login").dispatch(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info(message.getPayload());
        try {
            MessageRequest msg = mapper.readValue(message.getPayload(), MessageRequest.class);
            handlers.get(msg.getType()).dispatch(message, session);
        } catch (Exception e) {
            log.error("can't dispatch message " + message.getPayload(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed");
        handlers.get("logout").dispatch(session);
    }
}
