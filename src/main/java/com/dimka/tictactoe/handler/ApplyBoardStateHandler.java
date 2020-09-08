package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.dto.ApplyBoardStateRequest;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.WebSocketSessionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;

@AllArgsConstructor
@Component("applyBoardState")
public class ApplyBoardStateHandler implements Handler {

    private static final int[][] WHITE_BOARD = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private final WebSocketSessionStorage sessionStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;
    

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        ApplyBoardStateRequest request = mapper.readValue(message.getPayload(), ApplyBoardStateRequest.class);
        Game game = (Game) sessionStorage.getSessions().get(session.getId()).get("game");
        if (game.getTurn().equals(session.getId())) {
            int[][] cells = request.getCells();

            WebSocketSession enemy = emitter.getEnemy(game.getId(), session.getId());
            if (isWinner(cells)) {
                emitter.emmitBoardStateChangedEvent(WHITE_BOARD, session);
                emitter.emmitBoardStateChangedEvent(WHITE_BOARD, enemy);
            } else if (isDraw(cells)) {
                game.setCurrentGameNum(game.getCurrentGameNum() + 1);
                emitter.emmitBoardStateChangedEvent(WHITE_BOARD, session);
                emitter.emmitBoardStateChangedEvent(WHITE_BOARD, enemy);
            } else {
                int[][] invertedBoard = getInvertedBoard(cells);
                emitter.emmitBoardStateChangedEvent(invertedBoard, session);
            }

            game.setTurn(enemy.getId());
            emitter.emmitEnemyTurnEvent(session);
            emitter.emmitMyTurnEvent(enemy);
        }
    }

    private int[][] getInvertedBoard(int[][] cells) {
        int[][] revertedCells = new int[3][3];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j] == 1) {
                    revertedCells[i][j] = 2;
                } else if (cells[i][j] == 2) {
                    revertedCells[i][j] = 1;
                } else {
                    revertedCells[i][j] = 0;
                }
            }
        }
        return revertedCells;
    }

    public boolean isWinner(int[][] cells) {
        return (cells[0][0] == 1 && cells[0][1] == 1 && cells[0][2] == 1) ||
                (cells[1][0] == 1 && cells[1][1] == 1 && cells[1][2] == 1) ||
                (cells[2][0] == 1 && cells[2][1] == 1 && cells[2][2] == 1) ||
                (cells[0][0] == 1 && cells[1][0] == 1 && cells[2][0] == 1) ||
                (cells[0][1] == 1 && cells[1][1] == 1 && cells[2][1] == 1) ||
                (cells[0][2] == 1 && cells[1][2] == 1 && cells[2][2] == 1) ||
                (cells[0][0] == 1 && cells[1][1] == 1 && cells[2][2] == 1) ||
                (cells[0][2] == 1 && cells[1][1] == 1 && cells[2][0] == 1);
    }

    public boolean isDraw(int[][] cells) {
        boolean draw = true;
        for (int i = 0; i < cells.length; i ++) {
            for (int j = 0; j < cells[i].length; j++) {
                draw &= cells[i][j] == 1;
            }
        }
        return draw;
    }
}
