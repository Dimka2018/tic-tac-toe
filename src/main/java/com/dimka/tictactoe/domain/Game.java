package com.dimka.tictactoe.domain;

import lombok.Data;

@Data
public class Game {

    private String id;
    private String host;
    private String turn;
    private int currentGameNum;
    private int totalGames;
}
