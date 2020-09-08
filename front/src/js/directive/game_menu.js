import React from "react";
import Counter from "./counter";
import '../../css/gamemenu.css'

class GameMenu extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            message: ''
        }
    }

    setYourTurn() {
        this.setState({message: 'Your turn'})
    }

    setEnemyTurn() {
        this.setState({message: 'Enemy\'s turn'});
    }

    render() {
        return (
            <div className="game_menu">
                <Counter/>
                <span className='game-turn'>{this.state.message}</span>
                <div className="w-25 h-100 d-flex justify-content-end align-items-start">
                    <button className="leave_button">Leave</button>
                </div>

            </div>
        );
    }
}

export default GameMenu;