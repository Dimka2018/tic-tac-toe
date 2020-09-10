package com.dimka.tictactoe.handler;

import com.dimka.tictactoe.domain.Game;
import com.dimka.tictactoe.domain.User;
import com.dimka.tictactoe.dto.ApplyBoardStateRequest;
import com.dimka.tictactoe.event.EventEmitter;
import com.dimka.tictactoe.repository.UserStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@AllArgsConstructor
@Component("applyBoardState")
public class ApplyBoardStateHandler implements Handler {

    private static final int[][] WHITE_BOARD = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    private final UserStorage userStorage;
    private final EventEmitter emitter;
    private final ObjectMapper mapper;
    

    @Override
    public void dispatch(TextMessage message, WebSocketSession session) throws Exception {
        ApplyBoardStateRequest request = mapper.readValue(message.getPayload(), ApplyBoardStateRequest.class);
        User user = userStorage.getUser(session.getId());
        Game game = user.getGame();
        if (game.getTurn().equals(session.getId())) {
            int[][] cells = request.getCells();

            User enemy = userStorage.getGameEnemy(session.getId());
            if (isWinner(cells) || isDraw(cells)) {
                if (game.getCurrentGameNum() < game.getTotalGames()) {
                    emitter.emmitGamesChangedEvent(game.getCurrentGameNum(), game.getTotalGames(), session, enemy.getSession());
                    Map<String, Integer> score = game.getScore();
                    int enemyScore = score.get(enemy.getId());
                    int myScore = score.get(session.getId()) + 1;
                    score.put(session.getId(), myScore);
                    emitter.emmitScoreChangedEvent(myScore, enemyScore, session);
                    emitter.emmitScoreChangedEvent(enemyScore, myScore, enemy.getSession());
                    emitter.emmitBoardStateChangedEvent(WHITE_BOARD, session);
                    emitter.emmitBoardStateChangedEvent(WHITE_BOARD, enemy.getSession());
                } else {
                    user.setGame(null);
                    enemy.setGame(null);
                    int myScore = game.getScore().get(session.getId()) + 1;
                    int enemyScore = game.getScore().get(enemy.getId());

                    if (myScore > enemyScore) {
                        emitter.emmitYouWinEvent(session);
                        emitter.emmitYouLoseEvent(enemy.getSession());
                    } else if (myScore == enemyScore) {
                        emitter.emmitDrawEvent(session, enemy.getSession());
                    } else {
                        emitter.emmitYouWinEvent(enemy.getSession());
                        emitter.emmitYouLoseEvent(session);
                    }
                }
                game.setCurrentGameNum(game.getCurrentGameNum() + 1);
                emitter.emmitGamesChangedEvent(game.getCurrentGameNum(), game.getTotalGames(), session, enemy.getSession());
            } else {
                int[][] invertedBoard = getInvertedBoard(cells);
                emitter.emmitBoardStateChangedEvent(invertedBoard, session);
            }

            game.setTurn(enemy.getId());
            emitter.emmitEnemyTurnEvent(session);
            emitter.emmitMyTurnEvent(enemy.getSession());
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

    private boolean isWinner(int[][] cells) {
        return (cells[0][0] == 1 && cells[0][1] == 1 && cells[0][2] == 1) ||
                (cells[1][0] == 1 && cells[1][1] == 1 && cells[1][2] == 1) ||
                (cells[2][0] == 1 && cells[2][1] == 1 && cells[2][2] == 1) ||
                (cells[0][0] == 1 && cells[1][0] == 1 && cells[2][0] == 1) ||
                (cells[0][1] == 1 && cells[1][1] == 1 && cells[2][1] == 1) ||
                (cells[0][2] == 1 && cells[1][2] == 1 && cells[2][2] == 1) ||
                (cells[0][0] == 1 && cells[1][1] == 1 && cells[2][2] == 1) ||
                (cells[0][2] == 1 && cells[1][1] == 1 && cells[2][0] == 1);
    }

    private boolean isDraw(int[][] cells) {
        boolean draw = true;
        for (int[] row : cells) {
            for (int cell : row) {
                draw &= cell == 1;
            }
        }
        return draw;
    }
}
