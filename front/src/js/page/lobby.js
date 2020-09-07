import React from "react";
import '../../css/lobby.css';

class Lobby extends React.Component {

    constructor(props) {
        super(props);
        this.controller = this.props.controller;
        let params = new URL(window.location.href).searchParams;
        this.state = {
            host: params.get('host'),
            id: params.get('id'),
            users: []
        }
    }

    componentDidMount() {
        this.controller.subscribe('LOBBY_MEMBERS_LIST_CHANGED', message => {
            this.setMembers(message.members)
        });
        this.controller.subscribe('LEAVE_LOBBY', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('LOBBY_DESTROYED', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('KICK', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('GAME_STARTED', message => {
            this.controller.goGame();
        });
    }

    setMembers(members) {
        this.setState({users: members})
    }

    leaveLobby() {
        this.controller.leaveLobby();
    }

    render() {
        const users = this.state.users.map((user, index) => <div className="user" key={index}>
            <span>{index + 1}</span>
            <span className="user-name">{user.name}</span>
            {(this.state.host === 'false') || (index === 0) ? <span/> : <span className="kik-button" onClick={() => this.controller.kik(user.id)}>KIK</span>}
        </div>);
        return (
            <div className="background">
                <span className="title">USERS</span>
                <div className="user-container">
                    {users}
                </div>

                <div className="button-container">
                    {this.state.host === 'true' && <button className="lobby-button" onClick={() => this.controller.startGame(this.state.id)} >START</button>}
                    <button className="lobby-button" onClick={this.leaveLobby.bind(this)}>LEAVE</button>
                </div>
            </div>
        );
    }
}

export default Lobby;