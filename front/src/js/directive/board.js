import React from "react";
import '../../css/board.css';
import Cell from "./cell";

class Board extends React.Component {

    constructor(props) {
        super(props);
        this.boardState = [
            [0, 0, 0],
            [0, 0, 0],
            [0, 0, 0]
        ]
    }

    onBoardChanged(row, col) {
        this.boardState[row][col] = 1;
        this.props.onBoardChanged(this.boardState);
    }

    block() {
        this.refs.cel00.setInactive();
        this.refs.cel01.setInactive();
        this.refs.cel02.setInactive();
        this.refs.cel10.setInactive();
        this.refs.cel11.setInactive();
        this.refs.cel12.setInactive();
        this.refs.cel20.setInactive();
        this.refs.cel21.setInactive();
        this.refs.cel22.setInactive();
    }

    unBlock() {
        this.refs.cel00.setActive();
        this.refs.cel01.setActive();
        this.refs.cel02.setActive();
        this.refs.cel10.setActive();
        this.refs.cel11.setActive();
        this.refs.cel12.setActive();
        this.refs.cel20.setActive();
        this.refs.cel21.setActive();
        this.refs.cel22.setActive();
    }

    setBoardState(cells) {
        this.boardState = cells;
        this.refs.cel00.setActiveState(cells[0][0]);
        this.refs.cel01.setActiveState(cells[0][1]);
        this.refs.cel02.setActiveState(cells[0][2]);
        this.refs.cel10.setActiveState(cells[1][0]);
        this.refs.cel11.setActiveState(cells[1][1]);
        this.refs.cel12.setActiveState(cells[1][2]);
        this.refs.cel20.setActiveState(cells[2][0]);
        this.refs.cel21.setActiveState(cells[2][1]);
        this.refs.cel22.setActiveState(cells[2][2]);
    }

    render() {
        return (
            <div className="board container">
                <div className="line">
                    <Cell ref='cel00' row={0} col={0} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel01' row={0} col={1} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel02' row={0} col={2} onActivate={this.onBoardChanged.bind(this)}/>
                </div>
                <div className="line">
                    <Cell ref='cel10' row={1} col={0} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel11' row={1} col={1} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel12' row={1} col={2} onActivate={this.onBoardChanged.bind(this)}/>
                </div>
                <div className="line">
                    <Cell ref='cel20' row={2} col={0} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel21' row={2} col={1} onActivate={this.onBoardChanged.bind(this)}/>
                    <Cell ref='cel22' row={2} col={2} onActivate={this.onBoardChanged.bind(this)}/>
                </div>
            </div>
        );
    }
}

export default Board;