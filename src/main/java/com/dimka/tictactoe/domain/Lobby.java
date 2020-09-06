package com.dimka.tictactoe.domain;

import lombok.Data;

@Data
public class Lobby {

    private String id;
    private String host;
    private Integer games;
}
