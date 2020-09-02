import React from "react";
import Counter from "./counter";
import '../../css/gamemenu.css'

class GameMenu extends React.Component {

    render() {
        return (
            <div className="game_menu">
                <Counter/>
                <div className="w-25 h-100 d-flex justify-content-end align-items-start">
                    <button className="leave_button">Leave</button>
                </div>

            </div>
        );
    }
}

export default GameMenu;