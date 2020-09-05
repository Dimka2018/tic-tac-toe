package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.domain.Lobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LobbyListResponse extends MessageResponse {

    private List<Lobby> lobbies;
}
