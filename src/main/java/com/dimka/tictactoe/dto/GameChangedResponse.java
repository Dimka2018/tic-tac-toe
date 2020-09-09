package com.dimka.tictactoe.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GameChangedResponse extends MessageResponse {

    private int currentGame;
    private int totalGames;
}