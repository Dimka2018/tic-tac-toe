import React from "react";
import Counter from "./counter";
import '../../css/gamemenu.css'

class GameMenu extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            message: '',
            currentGame: 0,
            totalGames: 0
        }
    }

    setYourTurn() {
        this.setState({message: 'Your turn'})
    }

    setEnemyTurn() {
        this.setState({message: 'Enemy\'s turn'});
    }

    setScore(score) {
        this.refs.counter.setCount(score.you, score.enemy)
    }

    setGames(games) {
        this.setState({currentGame: games.currentGame, totalGames: games.totalGames})
    }

    onLeaveClick() {
        this.props.onLeave();
    }

    render() {
        return (
            <div className="game_menu">
                <div className="w-50 h-100 flex justify-content-center align-items-center">
                    <Counter ref="counter"/>
                    <div className="text-center mt-3 font-weight-bold h3">Game: {this.state.currentGame}/{this.state.totalGames}</div>
                </div>
                <span className='game-turn'>{this.state.message}</span>
                <div className="w-25 h-100 d-flex justify-content-end align-items-start">
                    <button className="leave_button" onClick={this.onLeaveClick.bind(this)}>Leave</button>
                </div>

            </div>
        );
    }
}

export default GameMenu;