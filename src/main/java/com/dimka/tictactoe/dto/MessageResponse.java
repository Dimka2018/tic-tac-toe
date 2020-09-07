package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.event.Event;
import lombok.Data;

@Data
public class MessageResponse {

    private Event type;
}
