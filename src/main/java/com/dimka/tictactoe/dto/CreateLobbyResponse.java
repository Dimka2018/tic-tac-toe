package com.dimka.tictactoe.dto;

import lombok.Data;

@Data
public class CreateLobbyResponse extends MessageResponse {

    private String id;
    private String host;
}
