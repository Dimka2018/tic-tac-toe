import React from "react";
import '../../css/board.css';
import Cell from "./cell";

class Board extends React.Component{

    render() {
        return (
            <div className="board container">
                <div className="line">
                    <Cell/>
                    <Cell/>
                    <Cell/>
                </div>
                <div className="line">
                    <Cell/>
                    <Cell/>
                    <Cell/>
                </div>
                <div className="line">
                    <Cell/>
                    <Cell/>
                    <Cell/>
                </div>
            </div>
        );
    }
}

export default Board;