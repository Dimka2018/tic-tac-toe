package com.dimka.tictactoe.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyBoardStateResponse extends MessageResponse {

    private int[][] cells;
}
