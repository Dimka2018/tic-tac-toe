package com.dimka.tictactoe.domain;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class User {

    private String id;
    private Lobby lobby;
    private Game game;
    private WebSocketSession session;
}
