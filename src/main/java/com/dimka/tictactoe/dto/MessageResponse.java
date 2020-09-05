package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.handler.Event;
import lombok.Data;

@Data
public class MessageResponse {

    private Event type;
}
