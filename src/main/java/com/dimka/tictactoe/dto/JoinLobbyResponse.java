package com.dimka.tictactoe.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class JoinLobbyResponse extends MessageResponse {

    private String lobbyId;
}
