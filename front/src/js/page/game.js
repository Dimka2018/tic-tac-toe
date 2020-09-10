import React from "react";
import Board from "../directive/board";
import GameMenu from "../directive/game_menu";
import '../../css/game.css';

class Game extends React.Component{

    constructor(props) {
        super(props);
        this.controller = props.controller;
    }

    componentDidMount() {
        this.controller.subscribe('game', 'BOARD_CHANGED', message => {
            this.refs.board.setBoardState(message.cells);
        });
        this.controller.subscribe('game', 'YOUR_TURN_STARTED', message => {
            this.refs.menu.setYourTurn();
            this.refs.board.unBlock();
        });
        this.controller.subscribe('game', 'ENEMY_TURN_STARTED', message => {
            this.refs.menu.setEnemyTurn();
            this.refs.board.block();
        });
        this.controller.subscribe('game', 'GAME_CHANGED', message => {
            this.refs.menu.setGames(message);
        });
        this.controller.subscribe('game', 'SCORE_CHANGED', message => {
            this.refs.menu.setScore(message);
        });
        this.controller.subscribe('game', 'YOU_WIN', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('game', 'YOU_LOSE', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('game', 'DRAW', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('game', 'LEAVE_GAME', message => {
            this.controller.goLobbySearch();
        });
    }

    applyGameState(board) {
        this.controller.applyGameState(board);
    }

    leaveGame() {
        this.controller.leaveGame();
    }

    render() {
        return (
            <div className="game">
                <GameMenu ref="menu" onLeave={this.leaveGame.bind(this)}/>
                <Board ref="board" onBoardChanged={this.applyGameState.bind(this)}/>
            </div>
        )
    }
}

export default Game;