package com.dimka.tictactoe.domain;

import lombok.Data;

@Data
public class Lobby {

    private String id;
    private String hostName;
    private String host;
    private int games;
}
