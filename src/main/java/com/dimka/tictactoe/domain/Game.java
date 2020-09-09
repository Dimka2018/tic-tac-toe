package com.dimka.tictactoe.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Game {

    private String id;
    private String host;
    private String turn;
    private int currentGameNum;
    private int totalGames;
    private Map<String, Integer> score = new HashMap<>();
}
