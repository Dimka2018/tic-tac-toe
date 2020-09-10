package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.domain.Lobby;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class LobbyListResponse extends MessageResponse {

    private Set<Lobby> lobbies;
}
