import React from "react";
import Board from "../directive/Board";
import GameMenu from "../directive/game_menu";
import '../../css/game.css';

class Game extends React.Component{

    render() {
        return (
            <div className="game">
                <GameMenu/>
                <Board/>
            </div>
        )
    }
}

export default Game;