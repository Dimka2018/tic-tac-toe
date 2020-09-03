import React from "react";
import '../../css/lobby_menu.css';

class LobbyMenu extends React.Component {

    constructor(props) {
        super(props);
        this.lobbyButtonCallback = props.lobbyButtonCallback;
    }

    render() {
        return (
            <div className="lobby-menu-container">
                <div className="row">
                    <button onClick={this.lobbyButtonCallback} className="create-lobby-button">New</button>
                </div>
            </div>
        );
    }
}

export default LobbyMenu;