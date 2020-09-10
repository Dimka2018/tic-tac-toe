package com.dimka.tictactoe.dto;

import com.dimka.tictactoe.domain.LobbyMember;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class LobbyMembersResponse extends MessageResponse {

    private Set<LobbyMember> members;
}
