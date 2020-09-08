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
        this.controller.subscribe('BOARD_CHANGED', message => {
            this.refs.board.setBoardState(message.cells);
        });
        this.controller.subscribe('YOUR_TURN_STARTED', message => {
            this.refs.menu.setYourTurn();
            this.refs.board.unBlock();
        });
        this.controller.subscribe('ENEMY_TURN_STARTED', message => {
            this.refs.menu.setEnemyTurn();
            this.refs.board.block();
        });
    }

    applyGameState(board) {
        this.controller.applyGameState(board);
    }

    render() {
        return (
            <div className="game">
                <GameMenu ref="menu"/>
                <Board ref="board" onBoardChanged={this.applyGameState.bind(this)}/>
            </div>
        )
    }
}

export default Game;