package com.dimka.tictactoe.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface Handler {

    default void dispatch(TextMessage message, WebSocketSession session) throws Exception {};

}
