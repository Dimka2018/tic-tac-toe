package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {

    private Event type;
}
